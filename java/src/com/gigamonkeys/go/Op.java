package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

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
public class Op implements Comparable<Op> {

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
        return VM.PUSH <= opcode && opcode <= VM.CALL;
    }

    public boolean isBranchOrJump() {
        return VM.IFZERO <= opcode && opcode <= VM.CALL;
    }

    public String toString() {
        String name = VM.NAMES[opcode];
        if (!takesOperand()) {
            return name;
        } else if (isBranchOrJump()) {
            return name + " <" + next2.address + ">";
        } else {
            return name + " " + operand;
        }
    }
}
