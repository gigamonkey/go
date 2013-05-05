package com.gigamonkeys.go;

import com.gigamonkeys.go.genetics.Genetics;
import java.util.List;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Unit test for com.gigamonkeys.go.ga.CompilerDump.
 */
public class CompilerDumpTest {

    public static void main(String[] args) {

        byte[] genes = new Genetics().randomGenes(Integer.parseInt(args[0]));

        List<Op> ops = new VM(0,0,0,0).compile(genes);

        for (Op op: ops) {
            System.out.println(op.address + ": " + op);
        }
    }

}
