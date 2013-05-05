package com.gigamonkeys.go.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Code for manipulating genetic material represented as byte arrays.
 */
public class Genetics {

    private final Random random = new Random();

    // The parameters that control mutation and crossing. There are a
    // number of ways we can mutate both during copying and after the
    // genomes have been split into chunks.

    private double bitMutationRate  = 0.001;
    private double byteMutationRate = 0.0001;
    private double copyMutationRate = 0.1;
    private int maxCopyMutation     = 20;

    /**
     * Number of chunks we split the gene into for crossing.
     */
    private int chunks = 10;

    /**
     * Get a random string of genetic material of the given length.
     */
    public byte[] randomGenes(int length) {
        byte[] genes = new byte[length];
        random.nextBytes(genes);
        return genes;
    }

    /**
     * Breed two sets of genes to prodce the given number of
     * offspring.
     */
    public List<byte[]> breed(byte[] p1, byte[] p2, int offspring) {

        List<byte[]> children = new ArrayList<byte[]>(offspring);

        for (int i = 0; i < (offspring + 1) / 2; i++) {
            List<byte[]> chunks1 = split(copyWithMutations(p1));
            List<byte[]> chunks2 = split(copyWithMutations(p2));

            children.add(cross(chunks1, chunks2));
            if (children.size() < offspring) {
                children.add(cross(chunks2, chunks1));
            }
        }

        return children;
    }

    static class GeneBuilder {

        private byte[] genes;

        private int idx = 0;

        GeneBuilder(int size) { this.genes = new byte[size]; }

        private void maybeExpand(int length) {
            if (idx + length > genes.length) {
                byte[] tmp = new byte[genes.length * 2];
                System.arraycopy(genes, 0, tmp, 0, idx);
                genes = tmp;
            }
        }

        public void copy(byte[] other, int pos, int length) {
            maybeExpand(length);
            System.arraycopy(other, pos, genes, idx, length);
            idx += length;
        }

        public void insert(byte[] other) {
            copy(other, 0, other.length);
        }

        public void permute(byte[] other, int pos, int length, Random random) {
            maybeExpand(length);
            // In place Fisher-Yates shuffle.
            for (int i = 0; i < length; i++) {
                int j = random.nextInt(i + 1);
                genes[idx + i] = genes[idx + j];
                genes[idx + j] = other[pos + i];
            }
        }

        public byte[] genes() {
            byte[] tmp = new byte[idx];
            System.arraycopy(genes, 0, tmp, 0, idx);
            return tmp;
        }
    }

    private byte[] copyWithMutations(byte[] genes) {

        GeneBuilder newgenes = new GeneBuilder(genes.length);

        int prev = 0;
        int i    = 0;

        while (i < genes.length) {

            if (random.nextDouble() < copyMutationRate) {

                newgenes.copy(genes, prev, i - prev);
                prev = i;

                int length = random.nextInt(maxCopyMutation);

                switch (random.nextInt(3)) {
                case 0: // Insertion
                    newgenes.insert(randomGenes(length));
                    i++;
                    break;
                case 1: // Deletion (a.k.a. skip)
                    length = Math.min(length, genes.length - i);
                    i += length;
                    prev = i;
                    break;
                case 2: // Permutation
                    length = Math.min(length, genes.length - i);
                    newgenes.permute(genes, i, length, random);
                    i += length;
                    prev = i;
                    break;
                }
            } else {
                i++;
            }
        }

        if (prev < i) {
            newgenes.copy(genes, prev, i - prev);
        }

        return pointMutations(newgenes.genes());
    }

    private byte[] pointMutations(byte[] genes) {
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < bitMutationRate) {
                genes[i] = flipRandomBit(genes[i]);
            }
            if (random.nextDouble() < byteMutationRate) {
                genes[i] = (byte)(0xff & random.nextInt());
            }
        }
        return genes;
    }

    private byte flipRandomBit(byte b) {
        return (byte)(b ^ (1 << random.nextInt(8)));
    }

    /**
     * Split a genetic sequence into chunks. Could reasonably split
     * into N chunks, into chunks of a certain mean length. May be
     * possible to implement more sophisticated splitting algorithm
     * that actually let the genes control where splits are more or
     * less likely.
     */
    public List<byte[]> split(byte[] genes) {
        int chunkSize = genes.length / chunks;
        int extra     = genes.length % chunks;

        List<byte[]> splits = new ArrayList<byte[]>(chunks);

        for (int i = 0; i < chunks; i++) {
            int size     = chunkSize + (i == chunks - 1 ? extra : 0);
            byte[] chunk = new byte[size];
            System.arraycopy(genes, i * chunkSize, chunk, 0, size);
            splits.add(chunk);
        }

        return splits;
    }

    /**
     * Given two lists of chunks, cross them by taking odd number
     * chunks from the first list and even number chunks from the
     * second list.
     */
    private byte[] cross(List<byte[]> chunks1, List<byte[]> chunks2) {
        assert chunks1.size() == chunks2.size();
        int length = 0;
        for (int i = 0; i < chunks1.size(); i++) {
            byte[] chunk = i % 2 == 0 ? chunks1.get(i) : chunks2.get(i);
            length += chunk.length;
        }

        byte[] crossed = new byte[length];
        int idx = 0;

        for (int i = 0; i < chunks1.size(); i++) {
            byte[] chunk = i % 2 == 0 ? chunks1.get(i) : chunks2.get(i);
            System.arraycopy(chunk, 0, crossed, idx, chunk.length);
            idx += chunk.length;
        }

        return crossed;
    }

    public void dump(byte[] genes) {
        for (byte b: genes) {
            System.out.print(String.format("%02x", b & 0xff));
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int l1 = Integer.parseInt(args[0]);
        int l2 = Integer.parseInt(args[1]);

        Genetics g = new Genetics();

        byte[] g1 = g.randomGenes(l1);
        byte[] g2 = g.randomGenes(l2);

        List<byte[]> offspring = g.breed(g1, g2, 10);

        g.dump(g1);
        g.dump(g2);
        System.out.println();
        for (byte[] o: offspring) {
            g.dump(o);
        }


    }
}
