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

    // N.B. Certain aspects of the structure of this file are
    // necessary for the script vm.pl to be able to act as a special
    // purpose macro processor for this one file. Lovely, I know.

    // No op.
    public final static byte NOP            = 0;

    // Memory ops
    public final static byte LOAD           = 1;
    public final static byte STORE          = 2;

    // Math ops
    public final static byte ADD            = 3;
    public final static byte SUB            = 4;
    public final static byte MUL            = 5;
    public final static byte DIV            = 6;
    public final static byte MOD            = 7;
    public final static byte INC            = 8;
    public final static byte DEC            = 9;
    public final static byte RAND           = 10;

    // Logic ops
    public final static byte AND            = 11;
    public final static byte OR             = 12;
    public final static byte XOR            = 13;
    public final static byte NOT            = 14;

    // Stack ops [1]
    public final static byte POP            = 15;
    public final static byte SWAP           = 16;
    public final static byte ROT            = 17;
    public final static byte DUP            = 18;
    public final static byte OVER           = 19;
    public final static byte TUCK           = 20;
    public final static byte PUSH           = 21;

    // Flow control ops
    public final static byte IFZERO         = 22;
    public final static byte IFPOS          = 23;
    public final static byte IFNEG          = 24;
    public final static byte IFNZERO        = 25;
    public final static byte IFNPOS         = 26;
    public final static byte IFNNEG         = 27;
    public final static byte GOTO           = 28;
    public final static byte CALL           = 29;
    public final static byte RET            = 30;
    public final static byte STOP           = 31;

    // Movement ops
    public final static byte FORWARD        = 32;
    public final static byte TURN_AROUND    = 33;
    public final static byte TURN_RIGHT     = 34;
    public final static byte TURN_LEFT      = 35;
    public final static byte POSITION       = 36;

    // Turn in a particular direction based on the gradients overlayed
    // on the board. E.g. MY_UPHILL picks the neighboring point with
    // the maximum MY gradient value (or a random one if there are
    // ties)
    public final static byte MY_UPHILL      = 37;
    public final static byte THEIR_UPHILL   = 38;
    public final static byte EMPTY_UPHILL   = 39;
    public final static byte MY_DOWNHILL    = 40;
    public final static byte THEIR_DOWNHILL = 41;
    public final static byte EMPTY_DOWNHILL = 42;

    // Perception ops
    public final static byte MINE           = 43;
    public final static byte THEIRS         = 44;
    public final static byte EMPTY          = 45;
    public final static byte MY_GRADIENT    = 46;
    public final static byte THEIR_GRADIENT = 47;
    public final static byte EMPTY_GRADIENT = 48;


    public final static String[] NAMES = {
	"NOP",
	"LOAD",
	"STORE",
	"ADD",
	"SUB",
	"MUL",
	"DIV",
	"MOD",
	"INC",
	"DEC",
	"RAND",
	"AND",
	"OR",
	"XOR",
	"NOT",
	"POP",
	"SWAP",
	"ROT",
	"DUP",
	"OVER",
	"TUCK",
	"PUSH",
	"IFZERO",
	"IFPOS",
	"IFNEG",
	"IFNZERO",
	"IFNPOS",
	"IFNNEG",
	"GOTO",
	"CALL",
	"RET",
	"STOP",
	"FORWARD",
	"TURN_AROUND",
	"TURN_RIGHT",
	"TURN_LEFT",
	"POSITION",
	"MY_UPHILL",
	"THEIR_UPHILL",
	"EMPTY_UPHILL",
	"MY_DOWNHILL",
	"THEIR_DOWNHILL",
	"EMPTY_DOWNHILL",
	"MINE",
	"THEIRS",
	"EMPTY",
	"MY_GRADIENT",
	"THEIR_GRADIENT",
	"EMPTY_GRADIENT",
    }; // End NAMES

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
	    case RAND:
		// Implement
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
	    case POP:
		tos = stack[--sp];
		break;
	    case SWAP:
		tmp = tos;
		tos = stack[sp - 1];
		stack[sp - 1] = tmp;
		break;
	    case ROT:
		tmp = stack[sp - 3];
		stack[sp - 3] = stack[sp - 2];
		stack[sp - 1] = tos;
		tos = tmp;
		break;
	    case DUP:
		stack[sp++] = tos;
		break;
	    case OVER:
		stack[sp++] = tos;
		tos = stack[sp - 2];
		break;
	    case TUCK:
		stack[sp] = stack[sp - 1];
		stack[sp - 1] = tos;
		sp++;
		break;
	    case PUSH:
		stack[sp++] = tos;
		tos = op.operand;
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
	    case IFNPOS:
		next = tos <= 0 ? op.next2 : op.next;
		tos = stack[--sp];
		break;
	    case IFNNEG:
		next = tos >= 0 ? op.next2 : op.next;
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
	    case STOP:
		return position;
	    case FORWARD:
		// Implement
		break;
	    case TURN_AROUND:
		// Implement
		break;
	    case TURN_RIGHT:
		// Implement
		break;
	    case TURN_LEFT:
		// Implement
		break;
	    case POSITION:
		stack[sp++] = tos;
		tos = position;
		break;
	    case MY_UPHILL:
		// Implement
		break;
	    case THEIR_UPHILL:
		// Implement
		break;
	    case EMPTY_UPHILL:
		// Implement
		break;
	    case MY_DOWNHILL:
		// Implement
		break;
	    case THEIR_DOWNHILL:
		// Implement
		break;
	    case EMPTY_DOWNHILL:
		// Implement
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
	    case MY_GRADIENT:
		// Implement
		break;
	    case THEIR_GRADIENT:
		// Implement
		break;
	    case EMPTY_GRADIENT:
		// Implement
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
