package com.gigamonkeys.go;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * The virtual machine for running compiled critters. we actually
 * lightly "compile" the critters, mostly to remove NOPs which
 * includes the actual NOP opcode and also the 217 byte values that
 * aren't legal opcodes, and to make sure all targets are actual start
 * of instructions. (E.g. some instructions, e.g. PUSH have operands
 * and we don't want anyone to jump to the operand and treat it as an
 * instruction. We obviously could let the VM chew on such bytecodes
 * with no problem but if we later decide to do a more serious
 * optimizing compilation of some sort, these semantics will be a lot
 * easier to recreate so instead the address in the bytes is
 * translated to the address of the first instruction at an address
 * greater than or equal to the encoded address.
 */
public class VM {

    // No op.
    public final static byte NOP      = 0;

    // Movement ops
    public final static byte NORTH    = 1;
    public final static byte SOUTH    = 2;
    public final static byte EAST     = 3;
    public final static byte WEST     = 4;
    public final static byte POSITION = 5;

    // Memory ops
    public final static byte LOAD     = 6;
    public final static byte STORE    = 7;

    // Math ops
    public final static byte ADD      = 8;
    public final static byte SUB      = 9;
    public final static byte MUL      = 10;
    public final static byte DIV      = 11;
    public final static byte MOD      = 12;
    public final static byte INC      = 13;
    public final static byte DEC      = 14;

    // Logic ops
    public final static byte AND      = 15;
    public final static byte OR       = 16;
    public final static byte XOR      = 17;
    public final static byte NOT      = 18;

    // Perception ops
    public final static byte MINE     = 19;
    public final static byte THEIRS   = 20;
    public final static byte EMPTY    = 21;

    // Stack ops [1]
    public final static byte POP      = 22;
    public final static byte SWAP     = 23;
    public final static byte ROT      = 24;
    public final static byte DUP      = 25;
    public final static byte OVER     = 26;
    public final static byte TUCK     = 27;
    public final static byte PUSH     = 28;

    // Flow control ops
    public final static byte IFZERO   = 29;
    public final static byte IFPOS    = 30;
    public final static byte IFNEG    = 31;
    public final static byte IFNZERO  = 32;
    public final static byte IFNPOS   = 33;
    public final static byte IFNNEG   = 34;
    public final static byte GOTO     = 35;
    public final static byte CALL     = 36;
    public final static byte RET      = 37;
    public final static byte STOP     = 38;

    public final static String[] NAMES = {
	// No op.
	"NOP",

	// Movement ops
	"NORTH", "SOUTH", "EAST", "WEST", "POSITION",

	// Memory ops
	"LOAD", "STORE",

	// Math ops
	"ADD", "SUB", "MUL", "DIV", "MOD", "INC", "DEC",

	// Logic ops
	"AND", "OR", "XOR", "NOT",

	// Perception ops
	"MINE", "THEIRS", "EMPTY",

	// Stack ops [1]
	"POP", "SWAP", "ROT", "DUP", "OVER", "TUCK", "PUSH",

	// Flow control ops
	"IFZERO", "IFPOS", "IFNEG", "IFNZERO", "IFNPOS", "IFNNEG",
	"GOTO", "CALL", "RET", "STOP",
    };

    private final int stackDepth;
    private final int callstackDepth;
    private final int memorySize;
    private final int maxCycles;

    VM(int stackDepth, int callstackDepth, int memorySize, int maxCycles) {
	this.stackDepth     = stackDepth;
	this.callstackDepth = callstackDepth;
	this.memorySize     = memorySize;
	this.maxCycles      = maxCycles;
    }


    /**
     * The representation of an actual Op. The compile function will
     * convert a byte[] into a DAG of these and return the initial
     * Op. The execute method will then take that Op and execute the
     * DAG. Note that we use the operand field to hold the int value
     * encoded in the two bytes following certain instructions but for
     * all but the PUSH op, that operand is actually an address which
     * the compiler translates into a direct link to the appropriate
     * Op object and stores in the next2 field.
     */
    public static class Op implements Comparable<Op> {

	public final byte opcode;
	public final int address;

	public int operand;
	public Op next;  // Always the next instruction in the bytecode
	public Op next2; // The other sucessor for branching and jumping instructions.

	Op(byte opcode, int address) {
	    this.opcode  = opcode;
	    this.address = address;
	}

	public int compareTo(Op other) {
	    return this.address - other.address;
	}

	public boolean takesOperand() {
	    // Opcodes are arranged to have all the instructions with
	    // operands consecutive, making this more efficient.
	    return PUSH <= opcode && opcode <= CALL;
	}

	private boolean isBranchOrJump() {
	    return IFZERO <= opcode && opcode <= CALL;
	}

	public String toString() {
	    String name = NAMES[opcode];
	    if (!takesOperand()) {
		return name;
	    } else if (isBranchOrJump()) {
		return name + " <" + next2.address + ">";
	    } else {
		return name + " " + operand;
	    }
	}

    }

    /**
     * Compile bytecodes and return the Op object representing the first instruction.
     */
    public List<Op> compile(byte[] bytecodes) {
	// We're going to collect the Ops so we can resolve jump
	// targets. But in the end we only need to return the starting
	// Op.
	List<Op> ops = new ArrayList<Op>();

	// Null object so we don't have to check previous == null all
	// the time during the first loop.
	Op previous = new Op(NOP,-1);

	// First pass we pick out the actual opcodes and generate Op
	// objects for each of them. For ops that take an in-bytecode
	// operand (PUSH and all the branching ops) we just store it
	// in operand as a number for now.
	int i = 0;
	while (i < bytecodes.length) {
	    byte b = bytecodes[i++];
	    if (isOpcode(b)) {
		Op op = new Op(b, i - 1);
		try {
		    if (op.takesOperand()) {
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
	    if (op.isBranchOrJump()) {
		op.next2 = findTargetOp(Math.min(op.operand, bytecodes.length), ops);
	    }
	}
	return ops;
    }

    private Op findTargetOp(int address, List<Op> ops) {
	int pos = Collections.binarySearch(ops, new Op(NOP, address));
	return pos >= 0 ? ops.get(pos) : ops.get(-1 * (pos + 1));
    }

    private boolean isOpcode(byte b) {
	// NOP is an opcode that execute actually can execute but we
	// don't generate an Op for it. And everything greater than
	// the last opcode is also a no-op.
	return NOP < b && b <= RET;
    }

    public int execute(Op start, Board board, Color color, int startPosition) {
	int[] stack    = new int[stackDepth];
	Op[] callstack = new Op[callstackDepth];
	int[] memory   = new int[memorySize];
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
		tos = memory[tos % memory.length];
		break;
	    case STORE:
		memory[tos % memory.length] = stack[--sp];
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

    // [1] see http://galileo.phys.virginia.edu/classes/551.jvn.fall01/primer.htm#param)

}
