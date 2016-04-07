package com.github.czyzby.websocket.serialization.impl;

/** Since empty arrays are immutable, this class was created as an utility to limit unnecessary object allocation, as
 * trivial as it might be.
 *
 * @author MJ */
public class EmptyArrays {
    private EmptyArrays() {
    }

    public static final boolean[] BOOLEAN = new boolean[0];
    public static final byte[] BYTE = new byte[0];
    public static final short[] SHORT = new short[0];
    public static final char[] CHAR = new char[0];
    public static final int[] INT = new int[0];
    public static final long[] LONG = new long[0];
    public static final float[] FLOAT = new float[0];
    public static final double[] DOUBLE = new double[0];
    public static final String[] STRING = new String[0];
}
