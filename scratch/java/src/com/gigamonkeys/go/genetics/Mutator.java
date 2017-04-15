package com.gigamonkeys.go.genetics;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Interface implemented by all kind of mutators, e.g. point mutators,
 * copy mutators, and any others we come up with. Each mutator is
 * responsible for determining the mutation rate (perhaps by having it
 * be a parameter to the mutator's constructor). And implementations
 * of the mutate method are allowed to destructively modify the passed
 * in byte[] and may or may not return the same byte[].
 */
public interface Mutator {

  public byte[] mutate(byte[] genes);

}
