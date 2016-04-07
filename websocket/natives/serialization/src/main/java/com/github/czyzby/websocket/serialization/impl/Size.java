package com.github.czyzby.websocket.serialization.impl;

import com.github.czyzby.websocket.serialization.SerializationException;

/** Contains number sizes handled by the {@link Serializer} and {@link Deserializer}. Allows to convert different number
 * sizes for the needs of serialization.
 *
 * @author MJ */
public enum Size {
    /** Uses 1 byte to serialize numbers. Truncates shorts, ints and longs. Cannot serialize floats or doubles. As array
     * length size, can serialize array lengths up to {@link java.lang.Byte#MAX_VALUE}. */
    BYTE(1, Byte.MAX_VALUE) {
        @Override
        void serializeShort(final short value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeInt(final int value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeLong(final long value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeFloat(final float value, final Serializer serializer) throws SerializationException {
            throw new SerializationException("Byte is too small to store float data. Cannot truncate.");
        }

        @Override
        void serializeDouble(final double value, final Serializer serializer) throws SerializationException {
            throw new SerializationException("Byte is too small to store double data. Cannot truncate");
        }

        @Override
        short deserializeShort(final Deserializer deserializer) {
            return deserializeByte(deserializer);
        }

        @Override
        int deserializeInt(final Deserializer deserializer) {
            return deserializeByte(deserializer);
        }

        @Override
        long deserializeLong(final Deserializer deserializer) {
            return deserializeByte(deserializer);
        }

        @Override
        float deserializeFloat(final Deserializer deserializer) throws SerializationException {
            throw new SerializationException("Byte is too small to store float data. Cannot deserialize.");
        }

        @Override
        double deserializeDouble(final Deserializer deserializer) throws SerializationException {
            throw new SerializationException("Byte is too small to store double data. Cannot deserialize.");
        }
    },
    /** Uses 2 bytes to serialize numbers. Uses 1 byte to serialize bytes and booleans. Truncates ints and longs. Cannot
     * serialize floats or doubles. As array length size, can serialize array lengths up to
     * {@link java.lang.Short#MAX_VALUE}. */
    SHORT(2, Short.MAX_VALUE) {
        @Override
        void serializeInt(final int value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeLong(final long value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeFloat(final float value, final Serializer serializer) throws SerializationException {
            throw new SerializationException("Short is too small to store float data. Cannot truncate.");
        }

        @Override
        void serializeDouble(final double value, final Serializer serializer) throws SerializationException {
            throw new SerializationException("Short is too small to store double data. Cannot truncate");
        }

        @Override
        int deserializeInt(final Deserializer deserializer) {
            return deserializeShort(deserializer);
        }

        @Override
        long deserializeLong(final Deserializer deserializer) {
            return deserializeShort(deserializer);
        }

        @Override
        float deserializeFloat(final Deserializer deserializer) throws SerializationException {
            throw new SerializationException("Short is too small to store float data. Cannot deserialize.");
        }

        @Override
        double deserializeDouble(final Deserializer deserializer) throws SerializationException {
            throw new SerializationException("Short is too small to store double data. Cannot deserialize.");
        }
    },
    /** Uses 4 bytes to serialize numbers. Uses 2 bytes to serialize shorts and 1 byte to serialize bytes and booleans.
     * Truncates longs and doubles. As array length size, can serialize array lengths up to
     * {@link java.lang.Integer#MAX_VALUE}. */
    INT(4, Integer.MAX_VALUE) {
        @Override
        void serializeLong(final long value, final Serializer serializer) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 24);
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 16);
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
            serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
        }

        @Override
        void serializeFloat(final float value, final Serializer serializer) {
            serializeInt(Float.floatToIntBits(value), serializer);
        }

        @Override
        void serializeDouble(final double value, final Serializer serializer) {
            serializeFloat((float) value, serializer);
        }

        @Override
        long deserializeLong(final Deserializer deserializer) {
            return deserializeInt(deserializer);
        }

        @Override
        float deserializeFloat(final Deserializer deserializer) throws SerializationException {
            return Float.intBitsToFloat(deserializeInt(deserializer));
        }

        @Override
        double deserializeDouble(final Deserializer deserializer) throws SerializationException {
            return deserializeFloat(deserializer);
        }
    },
    /** Uses 8 bytes to serialize numbers. Uses 4 bytes to serialize ints, 2 bytes to serialize shorts and 1 byte to
     * serialize bytes and booleans. Expands floats to doubles, potentially allowing to store more exact data than
     * floats serialized as ints. As array length size, can serialize array lengths up to
     * {@link java.lang.Integer#MAX_VALUE} - since Java uses ints as array length indexes, there's no point in
     * serializing array lengths as longs. */
    LONG(8, Integer.MAX_VALUE) {
        @Override
        void serializeDouble(final double value, final Serializer serializer) {
            serializeLong(Double.doubleToLongBits(value), serializer);
        }

        @Override
        void serializeFloat(final float value, final Serializer serializer) {
            serializeDouble(value, serializer);
        }

        @Override
        float deserializeFloat(final Deserializer deserializer) {
            return (float) deserializeDouble(deserializer);
        }

        @Override
        double deserializeDouble(final Deserializer deserializer) {
            return Double.longBitsToDouble(deserializeLong(deserializer));
        }
    };

    static final int NULL_ARRAY_ID = -1;

    private final int bytesAmount;
    private final int maxArrayLength;

    private Size(final int bytesAmount, final int maxArrayLength) {
        this.bytesAmount = bytesAmount;
        this.maxArrayLength = maxArrayLength;
    }

    /** @return smallest number size big enough to store all possible array lengths in Java: {@link #INT}. */
    public static Size getDefaultArrayLengthSize() {
        return Size.INT;
    }

    /** @return bytes amount needed to serialize the number. */
    public int getBytesAmount() {
        return bytesAmount;
    }

    /** @return when using this number size for serializing array lengths, this is the maximum value of array length
     *         that can be stored with this size's bytes amount. */
    public int getMaxArrayLength() {
        return maxArrayLength;
    }

    // Number serialization methods:

    void serializeBoolean(final boolean value, final Serializer serializer) {
        if (value) {
            serializer.serializedData[serializer.currentByteArrayIndex++] = 1;
        } else {
            serializer.serializedData[serializer.currentByteArrayIndex++] = 0;
        }
    }

    void serializeByte(final byte value, final Serializer serializer) {
        serializer.serializedData[serializer.currentByteArrayIndex++] = value;
    }

    void serializeShort(final short value, final Serializer serializer) {
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
    }

    void serializeInt(final int value, final Serializer serializer) {
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 24);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 16);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
    }

    void serializeLong(final long value, final Serializer serializer) {
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 56);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 48);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 40);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 32);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 24);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 16);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) (value >>> 8);
        serializer.serializedData[serializer.currentByteArrayIndex++] = (byte) value;
    }

    abstract void serializeFloat(final float value, final Serializer serializer) throws SerializationException;

    abstract void serializeDouble(final double value, final Serializer serializer) throws SerializationException;

    // Array serialization methods:

    /** @param arrayLength array length to serialize with the selected size.
     * @throws SerializationException if array length is too big. */
    protected void validateArrayLengthToSerialize(final int arrayLength) throws SerializationException {
        if (maxArrayLength < arrayLength) {
            throw new SerializationException(name() + " number size is to small to store array length of: "
                    + arrayLength + ". Data would be lost upon serialization.");
        }
    }

    private void prepareArray(final Size arrayLengthSize, final Serializer serializer, final int arrayLength)
            throws SerializationException {
        // Checking if length is smaller than the max value that can be serialized with this number size:
        arrayLengthSize.validateArrayLengthToSerialize(arrayLength);
        // Ensuring capacity for serializing array length and each element:
        // (This might oversize the serialized data array, but only if the user requested to serialize the
        // array with number size bigger than the actual numbers - so that's basically programmer's mistake.)
        serializer.ensureCapacity(arrayLengthSize.bytesAmount + arrayLength * bytesAmount);
        // Serializing array length:
        serializer.serializeInt(arrayLength, arrayLengthSize);
    }

    void serializeBooleanArray(final boolean[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final boolean value : array) {
            serializeBoolean(value, serializer);
        }
    }

    void serializeByteArray(final byte[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        final int arrayLength = array.length;
        prepareArray(arrayLengthSize, serializer, arrayLength);

        if (arrayLength > 0) {
            System.arraycopy(array, 0, serializer.serializedData, serializer.currentByteArrayIndex, arrayLength);
            serializer.currentByteArrayIndex += arrayLength;
        }
    }

    void serializeShortArray(final short[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final short value : array) {
            serializeShort(value, serializer);
        }
    }

    void serializeIntArray(final int[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final int value : array) {
            serializeInt(value, serializer);
        }
    }

    void serializeLongArray(final long[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final long value : array) {
            serializeLong(value, serializer);
        }
    }

    void serializeFloatArray(final float[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final float value : array) {
            serializeFloat(value, serializer);
        }
    }

    void serializeDoubleArray(final double[] array, final Size arrayLengthSize, final Serializer serializer)
            throws SerializationException {
        if (array == null) {
            serializer.serializeInt(NULL_ARRAY_ID, arrayLengthSize);
            return;
        }
        prepareArray(arrayLengthSize, serializer, array.length);
        for (final double value : array) {
            serializeDouble(value, serializer);
        }
    }

    // Number deserialization methods:

    boolean deserializeBoolean(final Deserializer deserializer) {
        return deserializer.serializedData[deserializer.currentByteArrayIndex++] > 0;
    }

    byte deserializeByte(final Deserializer deserializer) {
        return deserializer.serializedData[deserializer.currentByteArrayIndex++];
    }

    short deserializeShort(final Deserializer deserializer) {
        return (short) ((deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 8
                | deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF);
    }

    int deserializeInt(final Deserializer deserializer) {
        return deserializer.serializedData[deserializer.currentByteArrayIndex++] << 24
                | (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 16
                | (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 8
                | deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF;
    }

    long deserializeLong(final Deserializer deserializer) {
        return (long) deserializer.serializedData[deserializer.currentByteArrayIndex++] << 56
                | (long) (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 48
                | (long) (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 40
                | (long) (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 32
                | (long) (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 24
                | (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 16
                | (deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF) << 8
                | deserializer.serializedData[deserializer.currentByteArrayIndex++] & 0xFF;
    }

    abstract float deserializeFloat(Deserializer deserializer) throws SerializationException;

    abstract double deserializeDouble(Deserializer deserializer) throws SerializationException;

    // Array deserialization methods:

    private static void validateArrayLengthToCreate(final int arraySize) throws SerializationException {
        if (arraySize < 0) {
            throw new SerializationException("Cannot deserialize array. Negative array length: " + arraySize);
        }
    }

    boolean[] deserializeBooleanArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        } else if (arraySize == 0) {
            return EmptyArrays.BOOLEAN;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize);
        final boolean[] array = new boolean[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeBoolean(deserializer);
        }
        return array;
    }

    int deserializeBooleanArray(final Deserializer deserializer, final Size arrayLengthSize, final boolean[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize);
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeBoolean(deserializer);
        }
        return arraySize;
    }

    static void validateCachedArrayLength(final int resultArrayLength, final int arraySize)
            throws SerializationException {
        if (resultArrayLength < arraySize) {
            throw new SerializationException("Passed array has is too small to contain serialized array data. Length: "
                    + resultArrayLength + ", required: " + arraySize);
        }
    }

    byte[] deserializeByteArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.BYTE;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize);
        final byte[] array = new byte[arraySize];
        System.arraycopy(deserializer.serializedData, deserializer.currentByteArrayIndex, array, 0, arraySize);
        deserializer.currentByteArrayIndex += arraySize;
        return array;
    }

    int deserializeByteArray(final Deserializer deserializer, final Size arrayLengthSize, final byte[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize);
        validateCachedArrayLength(result.length, arraySize);
        System.arraycopy(deserializer.serializedData, deserializer.currentByteArrayIndex, result, 0, arraySize);
        deserializer.currentByteArrayIndex += arraySize;
        return arraySize;
    }

    short[] deserializeShortArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.SHORT;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize * Math.min(bytesAmount, SHORT.bytesAmount));
        final short[] array = new short[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeShort(deserializer);
        }
        return array;
    }

    int deserializeShortArray(final Deserializer deserializer, final Size arrayLengthSize, final short[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize * Math.min(bytesAmount, SHORT.bytesAmount));
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeShort(deserializer);
        }
        return arraySize;
    }

    int[] deserializeIntArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.INT;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize * Math.min(bytesAmount, INT.bytesAmount));
        final int[] array = new int[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeInt(deserializer);
        }
        return array;
    }

    int deserializeIntArray(final Deserializer deserializer, final Size arrayLengthSize, final int[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize * Math.min(bytesAmount, INT.bytesAmount));
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeInt(deserializer);
        }
        return arraySize;
    }

    long[] deserializeLongArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.LONG;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        final long[] array = new long[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeLong(deserializer);
        }
        return array;
    }

    int deserializeLongArray(final Deserializer deserializer, final Size arrayLengthSize, final long[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeLong(deserializer);
        }
        return arraySize;
    }

    float[] deserializeFloatArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.FLOAT;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        final float[] array = new float[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeFloat(deserializer);
        }
        return array;
    }

    int deserializeFloatArray(final Deserializer deserializer, final Size arrayLengthSize, final float[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeFloat(deserializer);
        }
        return arraySize;
    }

    double[] deserializeDoubleArray(final Deserializer deserializer, final Size arrayLengthSize)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize == NULL_ARRAY_ID) {
            return null;
        }
        if (arraySize == 0) {
            return EmptyArrays.DOUBLE;
        }
        validateArrayLengthToCreate(arraySize);
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        final double[] array = new double[arraySize];
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeDouble(deserializer);
        }
        return array;
    }

    int deserializeDoubleArray(final Deserializer deserializer, final Size arrayLengthSize, final double[] result)
            throws SerializationException {
        final int arraySize = deserializer.deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            return 0;
        }
        deserializer.validateBytesAmountToDeserialize(arraySize * bytesAmount);
        validateCachedArrayLength(result.length, arraySize);
        for (int index = 0; index < arraySize; index++) {
            result[index] = deserializeDouble(deserializer);
        }
        return arraySize;
    }
}
