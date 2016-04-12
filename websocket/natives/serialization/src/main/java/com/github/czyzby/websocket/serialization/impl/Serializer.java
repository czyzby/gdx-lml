package com.github.czyzby.websocket.serialization.impl;

import java.io.UnsupportedEncodingException;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;

/** Object serializer that does not use reflection. Provides custom serialization that works on GWT. Java 6 and
 * GWT-compatible. Most methods return "this" for chaining. Not thread-safe.
 *
 * @author MJ */
public class Serializer {
    private static final int DEFAULT_BYTES_AMOUNT_ESTIMATION = 32;

    // Package private, as most serialization methods are in Size enum.
    int currentByteArrayIndex;
    byte[] serializedData;

    public Serializer() {
        this(DEFAULT_BYTES_AMOUNT_ESTIMATION);
    }

    /** @param estimatedBytesAmount will create an array of bytes using this size. If the serialized object will have
     *            more bytes, the array will be resized. If it has less - the relevant bytes will be copied to a new,
     *            shorter array upon final serializing method. Has to be positive. */
    public Serializer(final int estimatedBytesAmount) {
        serializedData = new byte[estimatedBytesAmount];
    }

    /** Changes current byte index, effectively using current wrapped byte array to serialize another object. */
    public void reset() {
        currentByteArrayIndex = 0;
    }

    /** Resizes original byte array if the object turns out to be bigger than expected.
     *
     * @param bytesAmount amount of bytes that needs to be added to the array. */
    void ensureCapacity(final int bytesAmount) {
        if (currentByteArrayIndex + bytesAmount >= serializedData.length) {
            // +1 ensures proper behavior for 0 estimate.
            int newSerializedDataArrayLength = (serializedData.length + 1) * 2;
            while (currentByteArrayIndex + bytesAmount >= newSerializedDataArrayLength) {
                newSerializedDataArrayLength = newSerializedDataArrayLength * 2;
            }
            final byte[] newSerializedDataArray = new byte[newSerializedDataArrayLength];
            // Arrays.copyOf is not emulated on GWT.
            System.arraycopy(serializedData, 0, newSerializedDataArray, 0, currentByteArrayIndex);
            serializedData = newSerializedDataArray;
        }
    }

    /** @param value will be serialized with 1 byte. Custom, more efficient boolean serializations must be implemented
     *            manually.
     * @return this (for chaining). */
    public Serializer serializeBoolean(final boolean value) {
        ensureCapacity(Size.BYTE.getBytesAmount());
        Size.BYTE.serializeBoolean(value, this);
        return this;
    }

    /** @param value will be serialized with 1 byte.
     * @return this (for chaining). */
    public Serializer serializeByte(final byte value) {
        ensureCapacity(Size.BYTE.getBytesAmount());
        Size.BYTE.serializeByte(value, this);
        return this;
    }

    /** @param value will be serialized with 2 bytes.
     * @return this (for chaining). */
    public Serializer serializeShort(final short value) {
        return serializeShort(value, Size.SHORT);
    }

    /** @param value will be serialized.
     * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate. If
     *            bigger than actual number size, will ignore the size and serialize with the actual size needed to
     *            store all bytes of number data.
     * @return this (for chaining). */
    public Serializer serializeShort(final short value, final Size size) {
        ensureCapacity(size.getBytesAmount());
        size.serializeShort(value, this);
        return this;
    }

    /** @param value will be serialized with 4 bytes.
     * @return this (for chaining). */
    public Serializer serializeInt(final int value) {
        return serializeInt(value, Size.INT);
    }

    /** @param value will be serialized.
     * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate. If
     *            bigger than actual number size, will ignore the size and serialize with the actual size needed to
     *            store all bytes of number data.
     * @return this (for chaining). */
    public Serializer serializeInt(final int value, final Size size) {
        ensureCapacity(size.getBytesAmount());
        size.serializeInt(value, this);
        return this;
    }

    /** @param value will be serialized with 8 bytes.
     * @return this (for chaining). */
    public Serializer serializeLong(final long value) {
        return serializeLong(value, Size.LONG);
    }

    /** @param value will be serialized.
     * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate.
     * @return this (for chaining). */
    public Serializer serializeLong(final long value, final Size size) {
        ensureCapacity(size.getBytesAmount());
        size.serializeLong(value, this);
        return this;
    }

    /** @param value will be serialized with 4 bytes.
     * @return this (for chaining). */
    public Serializer serializeFloat(final float value) {
        try {
            return serializeFloat(value, Size.INT);
        } catch (final SerializationException exception) {
            // Should never happen, INT is big enough to store a float.
            throw new RuntimeException("Unexpected serialization exception.", exception);
        }
    }

    /** @param value will be serialized.
     * @param size amount of bytes used to serialize the number.
     * @throws SerializationException if the size is too small to store this number.
     * @return this (for chaining). */
    public Serializer serializeFloat(final float value, final Size size) throws SerializationException {
        ensureCapacity(size.getBytesAmount());
        size.serializeFloat(value, this);
        return this;
    }

    /** @param value will be serialized with 8 bytes.
     * @return this (for chaining). */
    public Serializer serializeDouble(final double value) {
        try {
            return serializeDouble(value, Size.LONG);
        } catch (final SerializationException exception) {
            // Should never happen, LONG is big enough to store a double.
            throw new RuntimeException("Unexpected serialization exception.", exception);
        }
    }

    /** @param value will be serialized.
     * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate.
     * @throws SerializationException if the size is too small to store this number.
     * @return this (for chaining). */
    public Serializer serializeDouble(final double value, final Size size) throws SerializationException {
        ensureCapacity(size.getBytesAmount());
        size.serializeDouble(value, this);
        return this;
    }

    /** @param value will serialize its ordinal number using 4 bytes.
     * @return this (for chaining). */
    public Serializer serializeEnum(final Enum<?> value) {
        return serializeInt(value.ordinal());
    }

    /** @param value will serialize its ordinal number.
     * @param enumLengthSize should be able to hold the biggest ordinal number of the passed enum. One byte is enough to
     *            serialize most enums and in most applications there are hardly ever enough enum constants to surpass
     *            two bytes length.
     * @return this (for chaining). */
    public Serializer serializeEnum(final Enum<?> value, final Size enumLengthSize) {
        return serializeInt(value.ordinal(), enumLengthSize);
    }

    /** @param array will be serialized, using 1 byte to store each value and 4 bytes to store array size. Note that
     *            more efficient implementations need to be custom.
     * @return this (for chaining). */
    public Serializer serializeBooleanArray(final boolean[] array) {
        try {
            return serializeBooleanArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 1 byte to store each value. Note that more efficient implementations need
     *            to be custom.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeBooleanArray(final boolean[] array, final Size arrayLengthSize)
            throws SerializationException {
        Size.BYTE.serializeBooleanArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 1 byte to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeByteArray(final byte[] array) {
        try {
            return serializeByteArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 1 byte to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeByteArray(final byte[] array, final Size arrayLengthSize) throws SerializationException {
        Size.BYTE.serializeByteArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 2 bytes to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeShortArray(final short[] array) {
        try {
            return serializeShortArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 2 bytes to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeShortArray(final short[] array, final Size arrayLengthSize)
            throws SerializationException {
        return serializeShortArray(array, arrayLengthSize, Size.SHORT);
    }

    /** @param array will be serialized.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will
     *            truncate. If bigger than actual number size, will ignore the size and serialize with the actual size
     *            needed to store all bytes of number data.
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeShortArray(final short[] array, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        elementSize.serializeShortArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 4 bytes to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeIntArray(final int[] array) {
        try {
            return serializeIntArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 4 bytes to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeIntArray(final int[] array, final Size arrayLengthSize) throws SerializationException {
        return serializeIntArray(array, arrayLengthSize, Size.INT);
    }

    /** @param array will be serialized.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will
     *            truncate. If bigger than actual number size, will ignore the size and serialize with the actual size
     *            needed to store all bytes of number data.
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeIntArray(final int[] array, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        elementSize.serializeIntArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 8 bytes to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeLongArray(final long[] array) {
        try {
            return serializeLongArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 8 bytes to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeLongArray(final long[] array, final Size arrayLengthSize) throws SerializationException {
        return serializeLongArray(array, arrayLengthSize, Size.LONG);
    }

    /** @param array will be serialized.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will
     *            truncate.
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeLongArray(final long[] array, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        elementSize.serializeLongArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 4 bytes to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeFloatArray(final float[] array) {
        try {
            return serializeFloatArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 4 bytes to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeFloatArray(final float[] array, final Size arrayLengthSize)
            throws SerializationException {
        return serializeFloatArray(array, arrayLengthSize, Size.INT);
    }

    /** @param array will be serialized.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @param elementSize amount of bytes used to serialize each array element.
     * @throws SerializationException if array length is longer than the maximum expected array length or selected
     *             element size is to small to store the number.
     * @return this (for chaining). */
    public Serializer serializeFloatArray(final float[] array, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        elementSize.serializeFloatArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param array will be serialized, using 8 bytes to store each value and 4 bytes to store array size.
     * @return this (for chaining). */
    public Serializer serializeDoubleArray(final double[] array) {
        try {
            return serializeDoubleArray(array, Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Default array length size is big enough to store any array size.
            throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
        }
    }

    /** @param array will be serialized, using 8 bytes to store each value.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @throws SerializationException if array length is longer than the maximum expected array length.
     * @return this (for chaining). */
    public Serializer serializeDoubleArray(final double[] array, final Size arrayLengthSize)
            throws SerializationException {
        return serializeDoubleArray(array, arrayLengthSize, Size.LONG);
    }

    /** @param array will be serialized.
     * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
     *            {@link Size#getMaxArrayLength()} .
     * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will
     *            truncate.
     * @throws SerializationException if array length is longer than the maximum expected array length or selected
     *             element size is to small to store the number.
     * @return this (for chaining). */
    public Serializer serializeDoubleArray(final double[] array, final Size arrayLengthSize, final Size elementSize)
            throws SerializationException {
        elementSize.serializeDoubleArray(array, arrayLengthSize, this);
        return this;
    }

    /** @param value will be serialized as byte array with 4 bytes to store array length.
     * @return this (for chaining). */
    public Serializer serializeString(final String value) {
        final byte[] stringAsBytes = value == null ? null : value.getBytes();
        return serializeByteArray(stringAsBytes);
    }

    /** @param value will be serialized as byte array.
     * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store the string. Each
     *            character translates roughly to 1 byte.
     * @throws SerializationException if string's byte array is longer than the estimated max size.
     * @return this (for chaining). */
    public Serializer serializeString(final String value, final Size stringLengthSize) throws SerializationException {
        byte[] stringAsBytes;
        try {
            stringAsBytes = value == null ? null : value.getBytes("UTF-8");
        } catch (final UnsupportedEncodingException exception) {
            throw new SerializationException("Unexpected: UTF-8 not supported.", exception);
        }
        return serializeByteArray(stringAsBytes, stringLengthSize);
    }

    /** @param array will be serialized as array of byte arrays using 4 bytes to store byte array lengths and 4 bytes to
     *            store main array length.
     * @return this (for chaining). */
    public Serializer serializeStringArray(final String[] array) {
        try {
            return serializeStringArray(array, Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
        } catch (final SerializationException exception) {
            // Should never happen. Array length sizes should be able to store any string arrays.
            throw new RuntimeException("Unexpected exception. Unable to serialize string array.", exception);
        }
    }

    /** @param array will be serialized as array of byte arrays using 4 bytes to store each byte array length.
     * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot
     *            exceed {@link Size#getMaxArrayLength()}
     * @throws SerializationException if string's byte array is longer than the estimated max size.
     * @return this (for chaining). */
    public Serializer serializeStringArray(final String[] array, final Size arrayLengthSize)
            throws SerializationException {
        return serializeStringArray(array, arrayLengthSize, Size.getDefaultArrayLengthSize());
    }

    /** @param array will be serialized as array of byte arrays.
     * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot
     *            exceed {@link Size#getMaxArrayLength()} .
     * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store each string. Each
     *            character translates roughly to 1 byte.
     * @throws SerializationException if string's byte array is longer than the estimated max size or any of the strings
     *             is too long for the given string length size.
     * @return this (for chaining). */
    public Serializer serializeStringArray(final String[] array, final Size arrayLengthSize,
            final Size stringLengthSize) throws SerializationException {
        if (array == null) {
            serializeInt(Size.NULL_ARRAY_ID, arrayLengthSize);
            return this;
        }
        arrayLengthSize.validateArrayLengthToSerialize(array.length);
        serializeInt(array.length, arrayLengthSize);
        for (final String value : array) {
            serializeString(value, stringLengthSize);
        }
        return this;
    }

    /** @param array will be serialized as array of byte arrays.
     * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot
     *            exceed {@link Size#getMaxArrayLength()} .
     * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store each string. Each
     *            character translates roughly to 1 byte.
     * @param start first index from which strings should be serialized.
     * @param count amount of strings to serialize.
     * @throws SerializationException if string's byte array is longer than the estimated max size or any of the strings
     *             is too long for the given string length size.
     * @return this (for chaining). */
    public Serializer serializeStringArray(final String[] array, final Size arrayLengthSize,
            final Size stringLengthSize, final int start, final int count) throws SerializationException {
        if (array == null) {
            serializeInt(Size.NULL_ARRAY_ID, arrayLengthSize);
            return this;
        }
        final int length = start + count;
        arrayLengthSize.validateArrayLengthToSerialize(length);
        serializeInt(length, arrayLengthSize);
        for (int index = start; index < length; index++) {
            serializeString(array[index], stringLengthSize);
        }
        return this;
    }

    /** @param transferable will be serialized. Cannot be null.
     * @throws SerializationException if unable to serialize object or the object is null.
     * @return this (for chaining). */
    public Serializer serializeTransferable(final Transferable<?> transferable) throws SerializationException {
        if (transferable == null) {
            throw new SerializationException("Cannot serialize transferable: null object received.",
                    new NullPointerException());
        }
        transferable.serialize(this);
        return this;
    }

    /** @param transferables will be serialized using 4 bytes to store array length. None of the transferables can be
     *            null.
     * @throws SerializationException if unable to serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArray(final Transferable<?>[] transferables) throws SerializationException {
        return serializeTransferableArray(transferables, 0, transferables.length, Size.getDefaultArrayLengthSize());
    }

    /** @param transferables will be serialized. None of the transferables can be null.
     * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
     * @throws SerializationException is array length size is too small to store array's length or if unable to
     *             serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArray(final Transferable<?>[] transferables, final Size arrayLengthSize)
            throws SerializationException {
        return serializeTransferableArray(transferables, 0, transferables.length, arrayLengthSize);
    }

    /** @param transferables will be serialized. None of the transferables can be null.
     * @param start index from which the transferables should be serialized.
     * @param count amount of transferables to serialize.
     * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
     * @throws SerializationException is array length size is too small to store array's length or if unable to
     *             serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArray(final Transferable<?>[] transferables, final int start,
            final int count, final Size arrayLengthSize) throws SerializationException {
        if (transferables == null) {
            serializeInt(Size.NULL_ARRAY_ID, arrayLengthSize);
            return this;
        }
        final int length = start + count;
        arrayLengthSize.validateArrayLengthToSerialize(length);
        serializeInt(length, arrayLengthSize);
        for (int index = start; index < length; index++) {
            serializeTransferable(transferables[index]);
        }
        return this;
    }

    /** @param transferables will be serialized using 4 bytes to store array length. Will use 1 extra byte for each
     *            transferable to detect nullability.
     * @throws SerializationException if unable to serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArrayWithPossibleNulls(final Transferable<?>[] transferables)
            throws SerializationException {
        return serializeTransferableArrayWithPossibleNulls(transferables, Size.getDefaultArrayLengthSize());
    }

    /** @param transferables will be serialized. Will use 1 extra byte for each transferable to detect nullability.
     * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
     * @throws SerializationException is array length size is too small to store array's length or if unable to
     *             serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArrayWithPossibleNulls(final Transferable<?>[] transferables,
            final Size arrayLengthSize) throws SerializationException {
        if (transferables == null) {
            serializeInt(Size.NULL_ARRAY_ID, arrayLengthSize);
            return this;
        }
        arrayLengthSize.validateArrayLengthToSerialize(transferables.length);
        serializeInt(transferables.length, arrayLengthSize);
        for (final Transferable<?> transferable : transferables) {
            if (transferable != null) {
                serializeByte(Byte.MAX_VALUE);
                serializeTransferable(transferable);
            } else {
                serializeInt(Size.NULL_ARRAY_ID, Size.BYTE);
            }
        }
        return this;
    }

    /** @param transferables will be serialized. Will use 1 extra byte for each transferable to detect nullability.
     * @param start index from which the transferables should be serialized.
     * @param count amount of transferables to serialize.
     * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
     * @throws SerializationException is array length size is too small to store array's length or if unable to
     *             serialize any of the transferables.
     * @return this (for chaining). */
    public Serializer serializeTransferableArrayWithPossibleNulls(final Transferable<?>[] transferables,
            final int start, final int count, final Size arrayLengthSize) throws SerializationException {
        if (transferables == null) {
            serializeInt(Size.NULL_ARRAY_ID, arrayLengthSize);
            return this;
        }
        final int length = start + count;
        arrayLengthSize.validateArrayLengthToSerialize(length);
        serializeInt(length, arrayLengthSize);
        for (int index = start; index < length; index++) {
            final Transferable<?> transferable = transferables[index];
            if (transferable != null) {
                serializeByte(Byte.MAX_VALUE);
                serializeTransferable(transferable);
            } else {
                serializeInt(Size.NULL_ARRAY_ID, Size.BYTE);
            }
        }
        return this;
    }

    /** Finishes serialization, returning the object as a byte array.
     *
     * @return serialized object as byte array. */
    public byte[] serialize() {
        if (currentByteArrayIndex == 0) {
            return new byte[0];
        }
        final byte[] extractedSerializedData = new byte[currentByteArrayIndex];
        System.arraycopy(serializedData, 0, extractedSerializedData, 0, currentByteArrayIndex);
        return extractedSerializedData;
    }

    /** Finishes serialization, returning the object as a byte array. Contrary to {@link #serialize()}, this method
     * might return the internal serializer's byte array reference, which might get modified if the serializer is reset
     * and used to serialize another object. Safe to use if a new instance of Serializer is used for each serialization.
     *
     * @return serialized object as byte array. Might be the internal serializer's byte array. */
    public byte[] serializeUnsafe() {
        if (currentByteArrayIndex == serializedData.length) {
            return serializedData;
        }
        return serialize();
    }
}
