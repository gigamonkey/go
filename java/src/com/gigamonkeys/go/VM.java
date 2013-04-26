package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * The virtual machine for running compiled critters. we actually
 * lightly "compile" the critters, mostly to remove NOPs (so the VM
 * technically doesn't have to support NOP but it does) and to make
 * sure all targets are actual start of instructions. (E.g. some
 * instructions, e.g. PUSH have operands and we don't want anyone to
 * jump to the operand and treat it as an instruction. We obviously
 * could let the VM chew on such bytecodes with no problem but if we
 * later decide to do a more serious optimizing compilation of some
 * sort, these semantics will be a lot easier to recreate.)
 */
public class VM {

    public final static int NOP      = 0;
    public final static int STOP     = 1;

    public final static int NORTH    = 2;
    public final static int SOUTH    = 3;
    public final static int EAST     = 4;
    public final static int WEST     = 5;
    public final static int POSITION = 6;

    public final static int LOAD     = 7;
    public final static int STORE    = 8;

    public final static int ADD      = 9;
    public final static int SUB      = 10;
    public final static int MUL      = 11;
    public final static int DIV      = 12;
    public final static int MOD      = 13;
    public final static int INC      = 14;
    public final static int DEC      = 15;

    public final static int AND      = 16;
    public final static int OR       = 17;
    public final static int XOR      = 18;
    public final static int NOT      = 19;

    public final static int MINE     = 20;
    public final static int THEIRS   = 21;
    public final static int EMPTY    = 22;

    public final static int PUSH     = 23;
    public final static int POP      = 24;
    public final static int SWAP     = 25;
    public final static int ROT      = 26;
    public final static int DUP      = 27;
    public final static int OVER     = 28;
    public final static int TUCK     = 29;

    public final static int IFZERO   = 30;
    public final static int IFPOS    = 31;
    public final static int IFNEG    = 32;
    public final static int IFNZERO  = 33;
    public final static int GOTO     = 34;
    public final static int CALL     = 35;
    public final static int RET      = 36;


    private static class Op {
        public int opcode;
        public int operand;
        public Op next;
        public Op next2;
    }

    /**
     * Compile bytecodes and return the Op object representing the first instruction.
     */
    private Op compile(byte[] bytecodes) {
        throw new Error("nyi");
    }

    public void run(byte[] bytecodes, int stackdepth, int callstackdepth, int memorysize, int maxCycles, Board board, Color color, int start) {
        int tos        = 0;
        int sp         = 0;
        int csp        = 0;
        int[] stack    = new int[stackdepth];
        Op[] callstack = new Op[callstackdepth];
        int[] memory   = new int[memorysize];
        int position   = start;
        int cycles     = 0;

        int tmp;

        Op op = compile(bytecodes);

        while (op != null) {
            if (cycles++ > maxCycles) return;

            Op next = null;

            switch (op.opcode) {
            case NOP:
                break;
            case STOP:
                return;
            case NORTH:
                position = board.north(position);
                break;
            case SOUTH:
                position = board.south(position);
                break;
            case EAST:
                position = board.east(position);
                break;
            case WEST:
                position = board.west(position);
                break;
            case POSITION:
                stack[sp++] = tos;
                tos = position;
                break;
            case LOAD:
                tos = memory[tos % memorysize];
                break;
            case STORE:
                memory[tos % memorysize] = stack[--sp];
                tos = stack[--sp];
                break;
            case ADD:
                tos += stack[--sp];
                break;
            case SUB:
                tos -= stack[--sp];
                break;
            case MUL:
                tos *= stack[-sp];
                break;
            case DIV:
                tmp = stack[--sp];
                tos = tmp == 0 ? 0 : tos / tmp;
                break;
            case MOD:
                tmp = stack[--sp];
                tos = tmp == 0 ? 0 : tos % tmp;
                break;
            case INC:
                tos++;
                break;
            case DEC:
                tos--;
                break;
            case AND:
                tos = tos & stack[--sp];
                break;
            case OR:
                tos = tos | stack[--sp];
                break;
            case XOR:
                tos = tos ^ stack[--sp];
                break;
            case NOT:
                tos = ~tos;
                break;
            case MINE:
                stack[sp++] = tos;
                tos = board.mine(color);
                break;
            case THEIRS:
                stack[sp++] = tos;
                tos = board.theirs(color);
                break;
            case EMPTY:
                stack[sp++] = tos;
                tos = board.empty(color);
                break;
            case PUSH: // add constant value to stack
                stack[sp++] = tos;
                tos = op.operand;
                break;
            case POP:
                tos = stack[--sp];
                break;
            case SWAP: // swap top two items
                tmp = tos;
                tos = stack[sp - 1];
                stack[sp - 1] = tmp;
                break;
            case ROT: // Rotate top three items so third from top ends up on top.
                tmp = stack[sp - 3];
                stack[sp - 3] = stack[sp - 2];
                stack[sp - 1] = tos;
                tos = tmp;
                break;
            case DUP: // duplicate top item of stack.
                stack[sp++] = tos;
                break;
            case OVER: // Copy the second item on stack to top.
                stack[sp++] = tos;
                tos = stack[sp - 2];
                break;
            case TUCK: // Copy top of stack to 3rd position.
                stack[sp] = stack[sp - 1];
                stack[sp - 1] = tos;
                sp++;
                break;
            case IFZERO:
                next = tos == 0 ? op.next : op.next2;
                tos = stack[--sp];
                break;
            case IFPOS:
                next = tos > 0 ? op.next : op.next2;
                tos = stack[--sp];
                break;
            case IFNEG:
                next = tos < 0 ? op.next : op.next2;
                tos = stack[--sp];
                break;
            case IFNZERO:
                next = tos != 0 ? op.next : op.next2;
                tos = stack[--sp];
                break;
            case GOTO:
                next = op.next;
                break;
            case CALL:
                // Next is the following instruction, next2 is where
                // we're calling to.
                callstack[csp++] = op.next;
                next = op.next2;
                break;
            case RET:
                next = callstack[--csp];
                break;
            }
            // Default case is to simply move to next instruction.
            op = next != null ? next : op.next;
        }
    }

    private int safeDiv(int n, int d) {
        return d == 0 ? 0 : n / d;
    }

    private int safeMod(int n, int d) {
        return d == 0 ? 0 : n % d;
    }


    // Movement: NORTH, SOUTH, EAST, WEST.                     (4)

    // Memory: LOAD, STORE.                                    (2)

    // Math: ADD, SUB, MUL, DIV, MOD, INC, DEC                 (7)

    // Logic: AND, OR, XOR, NOT                                (4)

    // Perception: MINE, THEIRS, EMPTY                         (3)

    // Stack: PUSH, POP, SWAP, ROT, DUP, OVER, TUCK[1]         (7)

    // Control: IFZERO, IFPOS, IFNEG, IFNZERO, GOTO, CALL, RET (7)

    // Other: NOP                                              (1)








    // [1] see http://galileo.phys.virginia.edu/classes/551.jvn.fall01/primer.htm#param)

    
    
    



}
