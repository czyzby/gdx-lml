package com.github.czyzby.websocket.serialization.impl;

import com.github.czyzby.websocket.serialization.ArrayProvider;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;

/** Object deserializer. Provides custom deserialization that works on GWT - allows to deserialize objects serialized
 * with {@link Serializer} class. Variables have to be deserialized in the same order as they were serialized. Java 6
 * and GWT-compatible. Not thread-safe.
 *
 * @author MJ */
public class Deserializer {
    // Package-private as most deserialization methods are in Size enum.
    byte[] serializedData;
    int currentByteArrayIndex;

    public Deserializer() {
        this(null);
    }

    public Deserializer(final byte[] serializedData) {
        this.serializedData = serializedData;
    }

    /** Resets the deserializer, allowing to use it for another serialized object.
     *
     * @param serializedData will replace current serialized object.
     * @return this, for chaining. */
    public Deserializer setSerializedData(final byte[] serializedData) {
        this.serializedData = serializedData;
        currentByteArrayIndex = 0;
        return this;
    }

    /** @param currentByteArrayIndex byte index at which the deserializer should read values. Can be set manually if
     *            many serialized objects are merged into one byte array. */
    public void setCurrentByteArrayIndex(final int currentByteArrayIndex) {
        this.currentByteArrayIndex = currentByteArrayIndex;
    }

    /** @param bytesAmountToBeDeserialized amount of bytes needed for deserialization of the given variable.
     * @throws SerializationException if serialized data array doesn't have enough bytes. */
    void validateBytesAmountToDeserialize(final int bytesAmountToBeDeserialized) throws SerializationException {
        if (currentByteArrayIndex + bytesAmountToBeDeserialized > serializedData.length) {
            throw new SerializationException(
                    "Received data is corrupted or invalid operation was executed. Too few bytes to deserialize requested variable.");
        }
    }

    /** @return value deserialized from 1 byte.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public boolean deserializeBoolean() throws SerializationException {
        validateBytesAmountToDeserialize(Size.BYTE.getBytesAmount());
        return Size.BYTE.deserializeBoolean(this);
    }

    /** @return value deserialized from 1 byte.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public byte deserializeByte() throws SerializationException {
        validateBytesAmountToDeserialize(Size.BYTE.getBytesAmount());
        return Size.BYTE.deserializeByte(this);
    }

    /** @return value deserialized from 2 bytes.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public short deserializeShort() throws SerializationException {
        return deserializeShort(Size.SHORT);
    }

    /** @param size amount of bytes used to serialize the number. Note that if the number of bytes is bigger than the
     *            actual number, the actual number of number's bytes will be used instead.
     * @return deserialized value.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public short deserializeShort(final Size size) throws SerializationException {
        validateBytesAmountToDeserialize(Math.min(size.getBytesAmount(), Size.SHORT.getBytesAmount()));
        return size.deserializeShort(this);
    }

    /** @return value deserialized from 4 bytes.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public int deserializeInt() throws SerializationException {
        return deserializeInt(Size.INT);
    }

    /** @param size amount of bytes used to serialize the number. Note that if the number of bytes is bigger than the
     *            actual number, the actual number of number's bytes will be used instead.
     * @return deserialized value.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public int deserializeInt(final Size size) throws SerializationException {
        validateBytesAmountToDeserialize(Math.min(size.getBytesAmount(), Size.INT.getBytesAmount()));
        return size.deserializeInt(this);
    }

    /** @return value deserialized from 8 bytes.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public long deserializeLong() throws SerializationException {
        return deserializeLong(Size.LONG);
    }

    /** @param size amount of bytes used to serialize the number.
     * @return deserialized value.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public long deserializeLong(final Size size) throws SerializationException {
        validateBytesAmountToDeserialize(size.getBytesAmount());
        return size.deserializeLong(this);
    }

    /** @return value deserialized from 4 bytes.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public float deserializeFloat() throws SerializationException {
        return deserializeFloat(Size.INT);
    }

    /** @param size amount of bytes used to serialize the number.
     * @return deserialized value.
     * @throws SerializationException if too few bytes to deserialize the value or size too small to store the
     *             number. */
    public float deserializeFloat(final Size size) throws SerializationException {
        validateBytesAmountToDeserialize(size.getBytesAmount());
        return size.deserializeFloat(this);
    }

    /** @return value deserialized from 8 bytes.
     * @throws SerializationException if too few bytes to deserialize the value. */
    public double deserializeDouble() throws SerializationException {
        return deserializeDouble(Size.LONG);
    }

    /** @param size amount of bytes used to serialize the number.
     * @return deserialized value.
     * @throws SerializationException if too few bytes to deserialize the value or size too small to store the
     *             number. */
    public double deserializeDouble(final Size size) throws SerializationException {
        validateBytesAmountToDeserialize(size.getBytesAmount());
        return size.deserializeDouble(this);
    }

    /** @param enumValues all values of the enum. Should be retrieved with {@link java.lang.Enum}.values().
     * @return enum constant with the ordinal deserialized from 4 bytes.
     * @param <Type> type of enum.
     * @throws SerializationException if unable to deserialize enum value. */
    public <Type extends Enum<Type>> Type deserializeEnum(final Type[] enumValues) throws SerializationException {
        return deserializeEnum(enumValues, Size.INT);
    }

    /** @param enumValues all values of the enum. Should be retrieved with {@link java.lang.Enum}.values().
     * @param enumLengthSize amount of bytes used to serialize enum ordinal. Defaults to 4 (int size).
     * @return enum constant with the ordinal deserialized from the data.
     * @param <Type> type of enum.
     * @throws SerializationException if unable to deserialize enum value. */
    public <Type extends Enum<Type>> Type deserializeEnum(final Type[] enumValues, final Size enumLengthSize)
            throws SerializationException {
        final int ordinal = deserializeInt(enumLengthSize);
        validateEnumOrdinal(ordinal, enumValues);
        return enumValues[ordinal];
    }

    private static void validateEnumOrdinal(final int ordinal, final Enum<?>[] enumValues)
            throws SerializationException {
        if (ordinal < 0 || enumValues.length <= ordinal) {
            throw new SerializationException("Invalid enum ordinal: " + ordinal);
        }
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 1
     *         byte.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public boolean[] deserializeBooleanArray() throws SerializationException {
        return deserializeBooleanArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 1 byte.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public boolean[] deserializeBooleanArray(final Size arrayLengthSize) throws SerializationException {
        return Size.BYTE.deserializeBooleanArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeBooleanArray(final boolean[] result) throws SerializationException {
        return deserializeBooleanArray(result, Size.getDefaultArrayLengthSize());
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeBooleanArray(final boolean[] result, final Size arrayLengthSize)
            throws SerializationException {
        return Size.BYTE.deserializeBooleanArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 1
     *         byte.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public byte[] deserializeByteArray() throws SerializationException {
        return deserializeByteArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 1 byte.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public byte[] deserializeByteArray(final Size arrayLengthSize) throws SerializationException {
        return Size.BYTE.deserializeByteArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeByteArray(final byte[] result) throws SerializationException {
        return deserializeByteArray(result, Size.getDefaultArrayLengthSize());
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeByteArray(final byte[] result, final Size arrayLengthSize) throws SerializationException {
        return Size.BYTE.deserializeByteArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 2
     *         bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public short[] deserializeShortArray() throws SerializationException {
        return deserializeShortArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 2 bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public short[] deserializeShortArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeShortArray(arrayLengthSize, Size.SHORT);
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return deserialized array of values.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public short[] deserializeShortArray(final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeShortArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeShortArray(final short[] result) throws SerializationException {
        return deserializeShortArray(result, Size.getDefaultArrayLengthSize(), Size.SHORT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeShortArray(final short[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeShortArray(result, arrayLengthSize, Size.SHORT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeShortArray(final short[] result, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeShortArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 4
     *         bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public int[] deserializeIntArray() throws SerializationException {
        return deserializeIntArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 4 bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public int[] deserializeIntArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeIntArray(arrayLengthSize, Size.INT);
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return deserialized array of values.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public int[] deserializeIntArray(final Size arrayLengthSize, final Size elementSize) throws SerializationException {
        return elementSize.deserializeIntArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeIntArray(final int[] result) throws SerializationException {
        return deserializeIntArray(result, Size.getDefaultArrayLengthSize(), Size.INT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeIntArray(final int[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeIntArray(result, arrayLengthSize, Size.INT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeIntArray(final int[] result, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeIntArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 8
     *         bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public long[] deserializeLongArray() throws SerializationException {
        return deserializeLongArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 8 bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public long[] deserializeLongArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeLongArray(arrayLengthSize, Size.LONG);
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value.
     * @return deserialized array of values.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public long[] deserializeLongArray(final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeLongArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeLongArray(final long[] result) throws SerializationException {
        return deserializeLongArray(result, Size.getDefaultArrayLengthSize(), Size.LONG);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeLongArray(final long[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeLongArray(result, arrayLengthSize, Size.LONG);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeLongArray(final long[] result, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeLongArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 4
     *         bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public float[] deserializeFloatArray() throws SerializationException {
        return deserializeFloatArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 4 bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public float[] deserializeFloatArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeFloatArray(arrayLengthSize, Size.INT);
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value.
     * @return deserialized array of values.
     * @throws SerializationException if too few bytes to deserialize the array or element size is to small to store the
     *             number. */
    public float[] deserializeFloatArray(final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeFloatArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeFloatArray(final float[] result) throws SerializationException {
        return deserializeFloatArray(result, Size.getDefaultArrayLengthSize(), Size.INT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeFloatArray(final float[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeFloatArray(result, arrayLengthSize, Size.INT);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeFloatArray(final float[] result, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeFloatArray(this, arrayLengthSize, result);
    }

    /** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 8
     *         bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public double[] deserializeDoubleArray() throws SerializationException {
        return deserializeDoubleArray(Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return deserialized array of values, each stored in 8 bytes.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public double[] deserializeDoubleArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeDoubleArray(arrayLengthSize, Size.LONG);
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value.
     * @return deserialized array of values.
     * @throws SerializationException if too few bytes to deserialize the array or element size is to small to store the
     *             number. */
    public double[] deserializeDoubleArray(final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeDoubleArray(this, arrayLengthSize);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeDoubleArray(final double[] result) throws SerializationException {
        return deserializeDoubleArray(result, Size.getDefaultArrayLengthSize(), Size.LONG);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeDoubleArray(final double[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeDoubleArray(result, arrayLengthSize, Size.LONG);
    }

    /** @param result cached array. Will be filled with deserialized values; note that values above the original
     *            serialized index are not changed. This means that if serialized array had 3 element and result array's
     *            length is 16, only first 3 values will be changed.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
     * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the
     *            number, actual bytes amount will be used.
     * @return length of the deserialized array. 0 if the serialized array was empty or null.
     * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
    public int deserializeDoubleArray(final double[] result, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        return elementSize.deserializeDoubleArray(this, arrayLengthSize, result);
    }

    /** @return string deserialized from byte array, with 4 bytes used to deserialize array length.
     * @throws SerializationException if too few bytes to deserialize the string. */
    public String deserializeString() throws SerializationException {
        return deserializeString(Size.getDefaultArrayLengthSize());
    }

    /** @param stringLengthSize estimated amount of bytes needed to serialize length of string's byte array. Each
     *            character translates roughly to 1 byte.
     * @return string deserialized from byte array.
     * @throws SerializationException if too few bytes to deserialize the string. */
    public String deserializeString(final Size stringLengthSize) throws SerializationException {
        final byte[] stringAsBytes = Size.BYTE.deserializeByteArray(this, stringLengthSize);
        if (stringAsBytes == null) {
            return null;
        }
        return new String(stringAsBytes);
    }

    /** @return string array deserialized from array of byte arrays, using 4 bytes to determine main array length and 4
     *         bytes for each byte array length.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public String[] deserializeStringArray() throws SerializationException {
        return deserializeStringArray(Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return string array deserialized from array of byte arrays, using 4 bytes for each byte array length.
     * @throws SerializationException if too few bytes to deserialize the array. */
    public String[] deserializeStringArray(final Size arrayLengthSize) throws SerializationException {
        return deserializeStringArray(arrayLengthSize, Size.getDefaultArrayLengthSize());
    }

    /** @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @param stringLengthSize estimated amount of bytes needed to serialize length of each string's byte array. Each
     *            character translates roughly to 1 byte.
     * @return deserialized string array.
     * @throws SerializationException if too few bytes to deserialize the array; */
    public String[] deserializeStringArray(final Size arrayLengthSize, final Size stringLengthSize)
            throws SerializationException {
        final int arrayLength = deserializeInt(arrayLengthSize);
        if (arrayLength == Size.NULL_ARRAY_ID) {
            return null;
        }
        if (arrayLength == 0) {
            return EmptyArrays.STRING;
        }
        final String[] array = new String[arrayLength];
        for (int index = 0; index < arrayLength; index++) {
            array[index] = deserializeString(stringLengthSize);
        }
        return array;
    }

    /** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized
     *            array, all the values above original array max index will be set as null.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeStringArray(final String[] result) throws SerializationException {
        return deserializeStringArray(result, Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
    }

    /** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized
     *            array, all the values above original array max index will be set as null.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeStringArray(final String[] result, final Size arrayLengthSize) throws SerializationException {
        return deserializeStringArray(result, arrayLengthSize, Size.getDefaultArrayLengthSize());
    }

    /** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized
     *            array, all the values above original array max index will be set as null.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @param stringLengthSize estimated amount of bytes needed to serialize length of each string's byte array. Each
     *            character translates roughly to 1 byte.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeStringArray(final String[] result, final Size arrayLengthSize, final Size stringLengthSize)
            throws SerializationException {
        final int arrayLength = deserializeInt(arrayLengthSize);
        if (arrayLength <= 0) {
            clearArray(result);
            return 0;
        }
        Size.validateCachedArrayLength(result.length, arrayLength);
        int index;
        for (index = 0; index < arrayLength; index++) {
            result[index] = deserializeString(stringLengthSize);
        }
        clearArray(result, index);
        return arrayLength;
    }

    /** @param array will contain only null values. Cannot be null. */
    private static void clearArray(final Object[] array) {
        for (int index = 0, length = array.length; index < length; index++) {
            array[index] = null;
        }
    }

    /** @param array will contain only null values starting from the passed index. Cannot be null.
     * @param fromIndex will start clearing from this index. */
    private static void clearArray(final Object[] array, int fromIndex) {
        if (fromIndex < array.length) {
            for (final int length = array.length; fromIndex < length; fromIndex++) {
                array[fromIndex] = null;
            }
        }
    }

    /** @param transferable example instance of transferable, used to invoke deserialization method.
     * @return new instance of transferable deserialized with this deserializer.
     * @throws SerializationException if unable to deserialize transferable.
     * @param <Type> type of transferable. */
    public <Type extends Transferable<Type>> Type deserializeTransferable(final Transferable<Type> transferable)
            throws SerializationException {
        return transferable.deserialize(this);
    }

    /** @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than
     *            Object or Transferable array.
     * @return deserialized transferables array, using 4 bytes to determine array length.
     * @param <Type> type of transferable.
     * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its
     *             elements. */
    public <Type extends Transferable<Type>> Type[] deserializeTransferableArray(final Transferable<Type> transferable,
            final ArrayProvider<Type> arrayProvider) throws SerializationException {
        return deserializeTransferableArray(transferable, arrayProvider, Size.getDefaultArrayLengthSize());
    }

    /** @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than
     *            Object or Transferable array.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return deserialized transferables array.
     * @param <Type> type of transferable.
     * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its
     *             elements. */
    public <Type extends Transferable<Type>> Type[] deserializeTransferableArray(final Transferable<Type> transferable,
            final ArrayProvider<Type> arrayProvider, final Size arrayLengthSize) throws SerializationException {
        final int arraySize = deserializeInt(arrayLengthSize);
        if (arraySize == Size.NULL_ARRAY_ID) {
            return null;
        }
        final Type[] array = arrayProvider.getArray(arraySize);
        if (arraySize == 0) {
            return array;
        }
        for (int index = 0; index < arraySize; index++) {
            array[index] = deserializeTransferable(transferable);
        }
        return array;
    }

    /** @param result will contain the deserialized transferables. If this array turns out to be longer than the
     *            serialized array, all the values above original array max index will be set as null.
     * @param transferable example instance of transferable, used to invoke deserialization method.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeTransferableArray(final Transferable<?>[] result, final Transferable<?> transferable)
            throws SerializationException {
        return deserializeTransferableArray(result, transferable, Size.getDefaultArrayLengthSize());
    }

    /** @param result will contain the deserialized transferables. If this array turns out to be longer than the
     *            serialized array, all the values above original array max index will be set as null.
     * @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeTransferableArray(final Transferable<?>[] result, final Transferable<?> transferable,
            final Size arrayLengthSize) throws SerializationException {
        final int arraySize = deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            clearArray(result);
            return 0;
        }
        int index;
        for (index = 0; index < arraySize; index++) {
            result[index] = deserializeTransferable(transferable);
        }
        clearArray(result, index);
        return arraySize;
    }

    /** @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than
     *            Object or Transferable array.
     * @return deserialized transferables array, using 4 bytes to determine array length.
     * @param <Type> type of transferable.
     * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its
     *             elements. */
    public <Type extends Transferable<Type>> Type[] deserializeTransferableArrayWithPossibleNulls(
            final Transferable<Type> transferable, final ArrayProvider<Type> arrayProvider)
                    throws SerializationException {
        return deserializeTransferableArrayWithPossibleNulls(transferable, arrayProvider,
                Size.getDefaultArrayLengthSize());
    }

    /** @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than
     *            Object or Transferable array.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return deserialized transferables array.
     * @param <Type> type of transferable.
     * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its
     *             elements. */
    public <Type extends Transferable<Type>> Type[] deserializeTransferableArrayWithPossibleNulls(
            final Transferable<Type> transferable, final ArrayProvider<Type> arrayProvider, final Size arrayLengthSize)
                    throws SerializationException {
        final int arraySize = deserializeInt(arrayLengthSize);
        if (arraySize == Size.NULL_ARRAY_ID) {
            return null;
        }
        final Type[] array = arrayProvider.getArray(arraySize);
        if (arraySize == 0) {
            return array;
        }
        for (int index = 0; index < arraySize; index++) {
            final int id = deserializeInt(Size.BYTE);
            array[index] = id == Size.NULL_ARRAY_ID ? null : deserializeTransferable(transferable);
        }
        return array;
    }

    /** @param result will contain the deserialized transferables. If this array turns out to be longer than the
     *            serialized array, all the values above original array max index will be set as null.
     * @param transferable example instance of transferable, used to invoke deserialization method.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeTransferableArrayWithPossibleNulls(final Transferable<?>[] result,
            final Transferable<?> transferable) throws SerializationException {
        return deserializeTransferableArrayWithPossibleNulls(result, transferable, Size.getDefaultArrayLengthSize());
    }

    /** @param result will contain the deserialized transferables. If this array turns out to be longer than the
     *            serialized array, all the values above original array max index will be set as null.
     * @param transferable example instance of transferable, used to invoke deserialization method.
     * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
     * @return length of the deserialized array. 0 if empty or null.
     * @throws SerializationException if unable to deserialize of the passed array was too small. */
    public int deserializeTransferableArrayWithPossibleNulls(final Transferable<?>[] result,
            final Transferable<?> transferable, final Size arrayLengthSize) throws SerializationException {
        final int arraySize = deserializeInt(arrayLengthSize);
        if (arraySize <= 0) {
            clearArray(result);
            return 0;
        }
        int index;
        for (index = 0; index < arraySize; index++) {
            final int id = deserializeInt(Size.BYTE);
            result[index] = id == Size.NULL_ARRAY_ID ? null : deserializeTransferable(transferable);
        }
        clearArray(result, index);
        return arraySize;
    }
}
