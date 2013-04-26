package com.gigamonkeys.go;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public final static int POP      = 23;
    public final static int SWAP     = 24;
    public final static int ROT      = 25;
    public final static int DUP      = 26;
    public final static int OVER     = 27;
    public final static int TUCK     = 28;

    // Arrange to have all the instructions with operands consecutive,
    // PUSH to CALL.
    public final static int PUSH     = 29;
    public final static int IFZERO   = 30;
    public final static int IFPOS    = 31;
    public final static int IFNEG    = 32;
    public final static int IFNZERO  = 33;
    public final static int IFNPOS   = 34;
    public final static int IFNNEG   = 35;
    public final static int GOTO     = 36;
    public final static int CALL     = 37;
    public final static int RET      = 38;

    private static class Op implements Comparable<Op> {

        public final int opcode;
        public final int address;

        public int operand;
        public Op next;  // Always the next instruction in the bytecode
        public Op next2; // The other sucessor for branching and jumping instructions.
        
        Op(int opcode, int address) {
            this.opcode  = opcode;
            this.address = address;
        }

        public int compareTo(Op other) {
            return this.address - other.address;
        }
    }

    /**
     * Compile bytecodes and return the Op object representing the first instruction.
     */
    public Op compile(byte[] bytecodes) {
        // We're going to collect the Ops so we can resolve jump
        // targets. But in the end we only need to return the starting
        // Op.
        List<Op> ops = new ArrayList<Op>();

        // Null object so we don't have to check previous == null all
        // the time.
        Op previous = new Op(NOP,-1);

        // First pass we pick out the actual opcodes and generate Op
        // objects for each of them. For ops that take an in-bytecode
        // operand (PUSH and all the branching ops) we store it in
        // operand as a number. 
        int i = 0;
        while (i < bytecodes.length) {
            byte b = bytecodes[i++];
            if (isOpcode(b)) {
                Op op = new Op(b, i - 1);
                try {
                    if (hasOperand(b)) {
                        op.operand = ((bytecodes[i++] & 0xff) << 8) | (bytecodes[i++] & 0xff);
                    }
                    ops.add(op);
                    previous.next = op;
                    previous      = op;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    // Ended with op code for op with operand but not
                    // enough bytes left. We'll just ignore that op.
                }
            }
        }
        
        // Fill in next2 pointers by finding the first actual Op at an
        // address >= to the address in the bytecode. We add a STOP
        // instruction at the end of the list of ops and then clamp
        // the possible addresses so that anything that jumps past the
        // end of the bytecodes will instead jump to the STOP.
        ops.add(new Op(STOP, bytecodes.length));
        for (Op op: ops) {
            if (isBranchOrJump(op.opcode)) {
                op.next2 = findTarget(Math.min(op.operand, bytecodes.length), ops);
            }
        }
        return ops.get(0);
    }

    private Op findTarget(int address, List<Op> ops) {
        int pos = Collections.binarySearch(ops, new Op(NOP, address));
        return pos >= 0 ? ops.get(pos) : ops.get(-1 * (pos + 1));
    }

    private boolean isOpcode(byte b) {
        // NOP is an opcode that execute actually can execute but we
        // don't generate an Op for it. And everything greater than
        // the last opcode is also a no-op.
        return NOP < b && b <= RET;
    }

    private boolean hasOperand(byte b) {
        return PUSH <= b && b <= CALL;
    }

    private boolean isBranchOrJump(int b) {
        return IFZERO <= b && b <= CALL;
    }

    public int execute(Op start, int stackdepth, int callstackdepth, int memorysize, int maxCycles, Board board, Color color, int startPosition) {
        int[] stack    = new int[stackdepth];
        Op[] callstack = new Op[callstackdepth];
        int[] memory   = new int[memorysize];
        int position   = startPosition;

        int tos        = 0;
        int sp         = 0;
        int csp        = 0;
        int cycles     = 0;

        int tmp;

        Op op = start;

        while (op != null) {
            if (cycles++ > maxCycles) break;

            Op next = null;

            switch (op.opcode) {
            case NOP:
                break;
            case STOP:
                return position;
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
                next = tos == 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case IFPOS:
                next = tos > 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case IFNEG:
                next = tos < 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case IFNZERO:
                next = tos != 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case IFNNEG:
                next = tos >= 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case IFNPOS:
                next = tos <= 0 ? op.next2 : op.next;
                tos = stack[--sp];
                break;
            case GOTO:
                next = op.next2;
                break;
            case CALL:
                callstack[csp++] = op.next;
                next = op.next2;
                break;
            case RET:
                next = callstack[--csp];
                break;
            default:
                throw new RuntimeException("Illegal opcode: " + op.opcode);
            }
            // Default case, if next hasn't been set, is to simply
            // move to next instruction.
            op = next != null ? next : op.next;
        }
        return position;
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
