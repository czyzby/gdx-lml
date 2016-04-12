package com.github.czyzby.websocket.serialization.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.czyzby.websocket.serialization.ArrayProvider;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;

/** Tests {@link Serializer} and {@link Deserializer}. These can be hardly tested separately, as we have no way to
 * validate the correctness of serialized data without actual deserializer to process it.
 *
 * @author MJ */
public class SerializerDeserializerTest {
    private static final float FLOAT_ERROR_TOLERANCE = 0.00001f;
    private final Serializer serializer = new Serializer();
    private final Deserializer deserializer = new Deserializer();

    @Before
    public void initiate() {
        serializer.reset();
    }

    // Primitives:

    @Test
    public void shouldSerializeBoolean() throws Exception {
        serializer.serializeBoolean(false);
        serializer.serializeBoolean(true);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertFalse(deserializer.deserializeBoolean());
        Assert.assertTrue(deserializer.deserializeBoolean());
    }

    @Test
    public void shouldSerializeByte() throws Exception {
        serializer.serializeByte(Byte.MIN_VALUE);
        serializer.serializeByte((byte) 42);
        serializer.serializeByte(Byte.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Byte.MIN_VALUE, deserializer.deserializeByte());
        Assert.assertEquals((byte) 42, deserializer.deserializeByte());
        Assert.assertEquals(Byte.MAX_VALUE, deserializer.deserializeByte());
    }

    @Test
    public void shouldSerializeShort() throws Exception {
        serializer.serializeShort(Short.MIN_VALUE);
        serializer.serializeShort((short) 10042);
        serializer.serializeShort(Short.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Short.MIN_VALUE, deserializer.deserializeShort());
        Assert.assertEquals((short) 10042, deserializer.deserializeShort());
        Assert.assertEquals(Short.MAX_VALUE, deserializer.deserializeShort());
    }

    @Test
    public void shouldSerializeShortAsByte() throws Exception {
        serializer.serializeShort(Byte.MIN_VALUE, Size.BYTE);
        serializer.serializeShort((short) 42, Size.BYTE);
        serializer.serializeShort(Byte.MAX_VALUE, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Byte.MIN_VALUE, deserializer.deserializeShort(Size.BYTE));
        Assert.assertEquals((short) 42, deserializer.deserializeShort(Size.BYTE));
        Assert.assertEquals(Byte.MAX_VALUE, deserializer.deserializeShort(Size.BYTE));
    }

    // Serializing short as int is pointless and the serializer can actually choose to ignore the request and
    // serializes a short instead. This is expected, as methods should be used in pairs:
    // serializer.serializeShort(value, NumberSize.INT) should be deserialized with
    // deserializer.deserializeShort(NumberSize.INT), not deserializer.deserializeInt() - the first
    // deserialization method will provide the correct result. Omitting checks for values longer than the
    // actual primitive, as such calls are unexpected and considered wrong use of the API.

    @Test
    public void shouldSerializeInt() throws Exception {
        serializer.serializeInt(Integer.MIN_VALUE);
        serializer.serializeInt(100042);
        serializer.serializeInt(Integer.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Integer.MIN_VALUE, deserializer.deserializeInt());
        Assert.assertEquals(100042, deserializer.deserializeInt());
        Assert.assertEquals(Integer.MAX_VALUE, deserializer.deserializeInt());
    }

    @Test
    public void shouldSerializeIntAsShort() throws Exception {
        serializer.serializeInt(Short.MIN_VALUE, Size.SHORT);
        serializer.serializeInt(10042, Size.SHORT);
        serializer.serializeInt(Short.MAX_VALUE, Size.SHORT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Short.MIN_VALUE, deserializer.deserializeInt(Size.SHORT));
        Assert.assertEquals(10042, deserializer.deserializeInt(Size.SHORT));
        Assert.assertEquals(Short.MAX_VALUE, deserializer.deserializeInt(Size.SHORT));
    }

    @Test
    public void shouldSerializeIntAsByte() throws Exception {
        serializer.serializeInt(Byte.MIN_VALUE, Size.BYTE);
        serializer.serializeInt(42, Size.BYTE);
        serializer.serializeInt(Byte.MAX_VALUE, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Byte.MIN_VALUE, deserializer.deserializeInt(Size.BYTE));
        Assert.assertEquals(42, deserializer.deserializeInt(Size.BYTE));
        Assert.assertEquals(Byte.MAX_VALUE, deserializer.deserializeInt(Size.BYTE));
    }

    @Test
    public void shouldSerializeLong() throws Exception {
        serializer.serializeLong(Long.MIN_VALUE);
        serializer.serializeLong(10000000042L);
        serializer.serializeLong(Long.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Long.MIN_VALUE, deserializer.deserializeLong());
        Assert.assertEquals(10000000042L, deserializer.deserializeLong());
        Assert.assertEquals(Long.MAX_VALUE, deserializer.deserializeLong());
    }

    @Test
    public void shouldSerializeLongAsInt() throws Exception {
        serializer.serializeLong(Integer.MIN_VALUE, Size.INT);
        serializer.serializeLong(100042L, Size.INT);
        serializer.serializeLong(Integer.MAX_VALUE, Size.INT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Integer.MIN_VALUE, deserializer.deserializeLong(Size.INT));
        Assert.assertEquals(100042L, deserializer.deserializeLong(Size.INT));
        Assert.assertEquals(Integer.MAX_VALUE, deserializer.deserializeLong(Size.INT));
    }

    @Test
    public void shouldSerializeLongAsShort() throws Exception {
        serializer.serializeLong(Short.MIN_VALUE, Size.SHORT);
        serializer.serializeLong(10042L, Size.SHORT);
        serializer.serializeLong(Short.MAX_VALUE, Size.SHORT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Short.MIN_VALUE, deserializer.deserializeLong(Size.SHORT));
        Assert.assertEquals(10042L, deserializer.deserializeLong(Size.SHORT));
        Assert.assertEquals(Short.MAX_VALUE, deserializer.deserializeLong(Size.SHORT));
    }

    @Test
    public void shouldSerializeLongAsByte() throws Exception {
        serializer.serializeLong(Byte.MIN_VALUE, Size.BYTE);
        serializer.serializeLong(42L, Size.BYTE);
        serializer.serializeLong(Byte.MAX_VALUE, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Byte.MIN_VALUE, deserializer.deserializeLong(Size.BYTE));
        Assert.assertEquals(42L, deserializer.deserializeLong(Size.BYTE));
        Assert.assertEquals(Byte.MAX_VALUE, deserializer.deserializeLong(Size.BYTE));
    }

    @Test
    public void shouldSerializeFloat() throws Exception {
        serializer.serializeFloat(Float.MIN_VALUE);
        serializer.serializeFloat(42.42f);
        serializer.serializeFloat(Float.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Float.MIN_VALUE, deserializer.deserializeFloat(), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(42.42f, deserializer.deserializeFloat(), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(Float.MAX_VALUE, deserializer.deserializeFloat(), FLOAT_ERROR_TOLERANCE);
    }

    // Float is an exception: serializing it as double MIGHT make it more accurate.
    @Test
    public void shouldSerializeFloatAsDouble() throws Exception {
        serializer.serializeFloat(Float.MIN_VALUE, Size.LONG);
        serializer.serializeFloat(42.42f, Size.LONG);
        serializer.serializeFloat(Float.MAX_VALUE, Size.LONG);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Float.MIN_VALUE, deserializer.deserializeFloat(Size.LONG), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(42.42f, deserializer.deserializeFloat(Size.LONG), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(Float.MAX_VALUE, deserializer.deserializeFloat(Size.LONG), FLOAT_ERROR_TOLERANCE);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingFloatAsByte() throws Exception {
        serializer.serializeFloat(42f, Size.BYTE);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingFloatAsShort() throws Exception {
        serializer.serializeFloat(42f, Size.SHORT);
    }

    @Test
    public void shouldSerializeDouble() throws Exception {
        serializer.serializeDouble(Double.MIN_VALUE);
        serializer.serializeDouble(42.42);
        serializer.serializeDouble(Double.MAX_VALUE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Double.MIN_VALUE, deserializer.deserializeDouble(), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(42.42, deserializer.deserializeDouble(), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(Double.MAX_VALUE, deserializer.deserializeDouble(), FLOAT_ERROR_TOLERANCE);
    }

    @Test
    public void shouldSerializeDoubleAsFloat() throws Exception {
        serializer.serializeDouble(Float.MIN_VALUE, Size.INT);
        serializer.serializeDouble(42.42, Size.INT);
        serializer.serializeDouble(Float.MAX_VALUE, Size.INT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(Float.MIN_VALUE, deserializer.deserializeDouble(Size.INT), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(42.42, deserializer.deserializeDouble(Size.INT), FLOAT_ERROR_TOLERANCE);
        Assert.assertEquals(Float.MAX_VALUE, deserializer.deserializeDouble(Size.INT), FLOAT_ERROR_TOLERANCE);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingDoubleAsByte() throws Exception {
        serializer.serializeDouble(42., Size.BYTE);
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingDoubleAsShort() throws Exception {
        serializer.serializeDouble(42., Size.SHORT);
    }

    // Primitive arrays:

    @Test
    public void shouldSerializeBooleanArray() throws Exception {
        final boolean[] byteLengthSizeArray = new boolean[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final boolean[] shortLengthSizeArray = new boolean[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final boolean[] intLengthSizeArray = new boolean[40042];
        fill(intLengthSizeArray);

        serializer.serializeBooleanArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeBooleanArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeBooleanArray(intLengthSizeArray, Size.INT);
        serializer.serializeBooleanArray(intLengthSizeArray);
        serializer.serializeBooleanArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeBooleanArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeBooleanArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeBooleanArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeBooleanArray());
        Assert.assertArrayEquals(null, deserializer.deserializeBooleanArray());
    }

    private static void fill(final boolean[] array) {
        array[0] = true;
        array[array.length / 2] = true;
        array[array.length - 1] = true;
    }

    @Test
    public void shouldSerializeByteArray() throws Exception {
        final byte[] byteLengthSizeArray = new byte[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final byte[] shortLengthSizeArray = new byte[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final byte[] intLengthSizeArray = new byte[40042];
        fill(intLengthSizeArray);

        serializer.serializeByteArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeByteArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeByteArray(intLengthSizeArray, Size.INT);
        serializer.serializeByteArray(intLengthSizeArray);
        serializer.serializeByteArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeByteArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeByteArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeByteArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeByteArray());
        Assert.assertArrayEquals(null, deserializer.deserializeByteArray());
    }

    private static void fill(final byte[] array) {
        array[0] = Byte.MIN_VALUE;
        array[array.length / 2] = 42;
        array[array.length - 1] = Byte.MAX_VALUE;
    }

    @Test
    public void shouldSerializeShortArray() throws Exception {
        final short[] byteLengthSizeArray = new short[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final short[] shortLengthSizeArray = new short[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final short[] intLengthSizeArray = new short[40042];
        fill(intLengthSizeArray);

        serializer.serializeShortArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeShortArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeShortArray(intLengthSizeArray, Size.INT);
        serializer.serializeShortArray(intLengthSizeArray);
        serializer.serializeShortArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeShortArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeShortArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeShortArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeShortArray());
        Assert.assertArrayEquals(null, deserializer.deserializeShortArray());
    }

    private static void fill(final short[] array) {
        array[0] = Short.MIN_VALUE;
        array[array.length / 2] = 42;
        array[array.length - 1] = Short.MAX_VALUE;
    }

    @Test
    public void shouldSerializeShortArrayWithTruncatedValues() throws Exception {
        final short[] array = new short[3];
        array[0] = Byte.MIN_VALUE;
        array[1] = 42;
        array[2] = Byte.MAX_VALUE;

        serializer.serializeShortArray(array, Size.getDefaultArrayLengthSize(), Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(array,
                deserializer.deserializeShortArray(Size.getDefaultArrayLengthSize(), Size.BYTE));
    }

    @Test
    public void shouldSerializeIntArray() throws Exception {
        final int[] byteLengthSizeArray = new int[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final int[] shortLengthSizeArray = new int[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final int[] intLengthSizeArray = new int[40042];
        fill(intLengthSizeArray);

        serializer.serializeIntArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeIntArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeIntArray(intLengthSizeArray, Size.INT);
        serializer.serializeIntArray(intLengthSizeArray);
        serializer.serializeIntArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeIntArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeIntArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeIntArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeIntArray());
        Assert.assertArrayEquals(null, deserializer.deserializeIntArray());
    }

    private static void fill(final int[] array) {
        array[0] = Integer.MIN_VALUE;
        array[array.length / 2] = 42;
        array[array.length - 1] = Integer.MAX_VALUE;
    }

    @Test
    public void shouldSerializeIntArrayWithTruncatedValues() throws Exception {
        final int[] byteArray = new int[3];
        byteArray[0] = Byte.MIN_VALUE;
        byteArray[1] = 42;
        byteArray[2] = Byte.MAX_VALUE;
        final int[] shortArray = new int[3];
        shortArray[0] = Short.MIN_VALUE;
        shortArray[1] = 42;
        shortArray[2] = Short.MAX_VALUE;

        serializer.serializeIntArray(byteArray, Size.getDefaultArrayLengthSize(), Size.BYTE);
        serializer.serializeIntArray(shortArray, Size.getDefaultArrayLengthSize(), Size.SHORT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteArray,
                deserializer.deserializeIntArray(Size.getDefaultArrayLengthSize(), Size.BYTE));
        Assert.assertArrayEquals(shortArray,
                deserializer.deserializeIntArray(Size.getDefaultArrayLengthSize(), Size.SHORT));
    }

    @Test
    public void shouldSerializeLongArray() throws Exception {
        final long[] byteLengthSizeArray = new long[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final long[] shortLengthSizeArray = new long[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final long[] intLengthSizeArray = new long[40042];
        fill(intLengthSizeArray);

        serializer.serializeLongArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeLongArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeLongArray(intLengthSizeArray, Size.INT);
        serializer.serializeLongArray(intLengthSizeArray);
        serializer.serializeLongArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeLongArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeLongArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeLongArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeLongArray());
        Assert.assertArrayEquals(null, deserializer.deserializeLongArray());
    }

    private static void fill(final long[] array) {
        array[0] = Long.MIN_VALUE;
        array[array.length / 2] = 42L;
        array[array.length - 1] = Long.MAX_VALUE;
    }

    @Test
    public void shouldSerializeLongArrayWithTruncatedValues() throws Exception {
        final long[] byteArray = new long[3];
        byteArray[0] = Byte.MIN_VALUE;
        byteArray[1] = 42;
        byteArray[2] = Byte.MAX_VALUE;
        final long[] shortArray = new long[3];
        shortArray[0] = Short.MIN_VALUE;
        shortArray[1] = 42;
        shortArray[2] = Short.MAX_VALUE;
        final long[] intArray = new long[3];
        intArray[0] = Integer.MIN_VALUE;
        intArray[1] = 42;
        intArray[2] = Integer.MAX_VALUE;

        serializer.serializeLongArray(byteArray, Size.getDefaultArrayLengthSize(), Size.BYTE);
        serializer.serializeLongArray(shortArray, Size.getDefaultArrayLengthSize(), Size.SHORT);
        serializer.serializeLongArray(intArray, Size.getDefaultArrayLengthSize(), Size.INT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteArray,
                deserializer.deserializeLongArray(Size.getDefaultArrayLengthSize(), Size.BYTE));
        Assert.assertArrayEquals(shortArray,
                deserializer.deserializeLongArray(Size.getDefaultArrayLengthSize(), Size.SHORT));
        Assert.assertArrayEquals(intArray,
                deserializer.deserializeLongArray(Size.getDefaultArrayLengthSize(), Size.INT));
    }

    @Test
    public void shouldSerializeFloatArray() throws Exception {
        final float[] byteLengthSizeArray = new float[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final float[] shortLengthSizeArray = new float[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final float[] intLengthSizeArray = new float[40042];
        fill(intLengthSizeArray);

        serializer.serializeFloatArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeFloatArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeFloatArray(intLengthSizeArray, Size.INT);
        serializer.serializeFloatArray(intLengthSizeArray);
        serializer.serializeFloatArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeFloatArray(Size.BYTE),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeFloatArray(Size.SHORT),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeFloatArray(Size.INT),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeFloatArray(), FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(null, deserializer.deserializeFloatArray(), 0);
    }

    private static void fill(final float[] array) {
        array[0] = Float.MIN_VALUE;
        array[array.length / 2] = 42.42f;
        array[array.length - 1] = Float.MAX_VALUE;
    }

    @Test
    public void shouldSerializeFloatArrayWithExtendedValues() throws Exception {
        final float[] array = new float[3];
        array[0] = Float.MIN_VALUE;
        array[1] = 42.42f;
        array[2] = Float.MAX_VALUE;

        serializer.serializeFloatArray(array, Size.getDefaultArrayLengthSize(), Size.LONG);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(array, deserializer.deserializeFloatArray(Size.getDefaultArrayLengthSize(), Size.LONG),
                FLOAT_ERROR_TOLERANCE);
    }

    @Test
    public void shouldSerializeDoubleArray() throws Exception {
        final double[] byteLengthSizeArray = new double[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final double[] shortLengthSizeArray = new double[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final double[] intLengthSizeArray = new double[40042];
        fill(intLengthSizeArray);

        serializer.serializeDoubleArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeDoubleArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeDoubleArray(intLengthSizeArray, Size.INT);
        serializer.serializeDoubleArray(intLengthSizeArray);
        serializer.serializeDoubleArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeDoubleArray(Size.BYTE),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeDoubleArray(Size.SHORT),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeDoubleArray(Size.INT),
                FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeDoubleArray(), FLOAT_ERROR_TOLERANCE);
        Assert.assertArrayEquals(null, deserializer.deserializeDoubleArray(), 0);
    }

    private static void fill(final double[] array) {
        array[0] = Double.MIN_VALUE;
        array[array.length / 2] = 42.42;
        array[array.length - 1] = Double.MAX_VALUE;
    }

    @Test
    public void shouldSerializeDoubleArrayWithTruncatedValues() throws Exception {
        final double[] array = new double[3];
        array[0] = Float.MIN_VALUE;
        array[1] = 42.42;
        array[2] = Float.MAX_VALUE;

        serializer.serializeDoubleArray(array, Size.getDefaultArrayLengthSize(), Size.INT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(array, deserializer.deserializeDoubleArray(Size.getDefaultArrayLengthSize(), Size.INT),
                FLOAT_ERROR_TOLERANCE);
    }

    @Test(expected = SerializationException.class)
    public void shouldProperlyValidateArraySize() throws Exception {
        // This is used by all array serialization methods.
        try {
            Size.BYTE.validateArrayLengthToSerialize(Byte.MAX_VALUE + 1);
        } catch (final SerializationException expected) {
            expected.hashCode(); // Ignoring exception.
            Size.SHORT.validateArrayLengthToSerialize(Short.MAX_VALUE + 1);
        }
        // INT can store any valid Java array index, so it's not validated. LONG uses INT to serialize array
        // length, as its too long for Java array indexes anyway.
    }

    // Cached arrays:

    @Test
    public void shouldDeserializeToCachedBooleanArray() throws Exception {
        final boolean[] cached = new boolean[100];
        final boolean[] serialized = new boolean[] { true, false, true };
        serializer.serializeBooleanArray(serialized);
        serializer.serializeBooleanArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeBooleanArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = false;
        size = deserializer.deserializeBooleanArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    @Test
    public void shouldDeserializeToCachedByteArray() throws Exception {
        final byte[] cached = new byte[100];
        final byte[] serialized = new byte[] { 12, 13, 14 };
        serializer.serializeByteArray(serialized);
        serializer.serializeByteArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeByteArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = 0;
        size = deserializer.deserializeByteArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    @Test
    public void shouldDeserializeToCachedShortArray() throws Exception {
        final short[] cached = new short[100];
        final short[] serialized = new short[] { 150, 151, 152 };
        serializer.serializeShortArray(serialized);
        serializer.serializeShortArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeShortArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = 0;
        size = deserializer.deserializeShortArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    @Test
    public void shouldDeserializeToCachedIntArray() throws Exception {
        final int[] cached = new int[100];
        final int[] serialized = new int[] { 150000, 151000, 152000 };
        serializer.serializeIntArray(serialized);
        serializer.serializeIntArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeIntArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = 0;
        size = deserializer.deserializeIntArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    @Test
    public void shouldDeserializeToCachedLongArray() throws Exception {
        final long[] cached = new long[100];
        final long[] serialized = new long[] { 1500000000000000L, 151L, 152L };
        serializer.serializeLongArray(serialized);
        serializer.serializeLongArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeLongArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = 0L;
        size = deserializer.deserializeLongArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    @Test
    public void shouldDeserializeToCachedFloatArray() throws Exception {
        final float[] cached = new float[100];
        final float[] serialized = new float[] { 150.0f, 15.1f, 1.52f };
        serializer.serializeFloatArray(serialized);
        serializer.serializeFloatArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeFloatArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index], FLOAT_ERROR_TOLERANCE);
        }
        cached[0] = cached[1] = cached[2] = 0;
        size = deserializer.deserializeFloatArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index], FLOAT_ERROR_TOLERANCE);
        }
    }

    @Test
    public void shouldDeserializeToCachedDoubleArray() throws Exception {
        final double[] cached = new double[100];
        final double[] serialized = new double[] { 15.0, 1.51, .152 };
        serializer.serializeDoubleArray(serialized);
        serializer.serializeDoubleArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeDoubleArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index], FLOAT_ERROR_TOLERANCE);
        }
        cached[0] = cached[1] = cached[2] = 0;
        size = deserializer.deserializeDoubleArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index], FLOAT_ERROR_TOLERANCE);
        }
    }

    @Test
    public void shouldDeserializeToCachedStringArray() throws Exception {
        final String[] cached = new String[100];
        final String[] serialized = new String[] { "2", null, "4" };
        serializer.serializeStringArray(serialized);
        serializer.serializeStringArray(serialized, Size.BYTE);
        deserializer.setSerializedData(serializer.serialize());
        int size = deserializer.deserializeStringArray(cached);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
        cached[0] = cached[1] = cached[2] = null;
        size = deserializer.deserializeStringArray(cached, Size.BYTE);
        Assert.assertEquals(serialized.length, size);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(serialized[index], cached[index]);
        }
    }

    // Strings:

    @Test
    public void shouldSerializeString() throws Exception {
        final String byteLengthSizeString = "byte";
        final String shortLengthSizeString = "I'm not so short as the name might suggest, as I should contain more than 128 characters, since each character translates to roughly 1 byte.";
        final String intLengthSizeString = new String(new byte[40042]);

        serializer.serializeString(byteLengthSizeString, Size.BYTE);
        serializer.serializeString(shortLengthSizeString, Size.SHORT);
        serializer.serializeString(intLengthSizeString, Size.INT);
        serializer.serializeString(intLengthSizeString);
        serializer.serializeString(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(byteLengthSizeString, deserializer.deserializeString(Size.BYTE));
        Assert.assertEquals(shortLengthSizeString, deserializer.deserializeString(Size.SHORT));
        Assert.assertEquals(intLengthSizeString, deserializer.deserializeString(Size.INT));
        Assert.assertEquals(intLengthSizeString, deserializer.deserializeString());
        Assert.assertEquals(null, deserializer.deserializeString());
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingTooLongStringWithTooSmallIndexSize() throws Exception {
        final String shortLengthSizeString = "I'm not so short as the name might suggest, as I should contain more than 128 characters, since each character translates to roughly 1 byte.";
        serializer.serializeString(shortLengthSizeString, Size.BYTE);
    }

    @Test
    public void shouldSerializeStringArray() throws Exception {
        final String[] byteLengthSizeArray = new String[Byte.MAX_VALUE];
        fill(byteLengthSizeArray);
        final String[] shortLengthSizeArray = new String[Short.MAX_VALUE];
        fill(shortLengthSizeArray);
        final String[] intLengthSizeArray = new String[40042];
        fill(intLengthSizeArray);

        serializer.serializeStringArray(byteLengthSizeArray, Size.BYTE);
        serializer.serializeStringArray(shortLengthSizeArray, Size.SHORT);
        serializer.serializeStringArray(intLengthSizeArray, Size.INT);
        serializer.serializeStringArray(intLengthSizeArray);
        serializer.serializeStringArray(null);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthSizeArray, deserializer.deserializeStringArray(Size.BYTE));
        Assert.assertArrayEquals(shortLengthSizeArray, deserializer.deserializeStringArray(Size.SHORT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeStringArray(Size.INT));
        Assert.assertArrayEquals(intLengthSizeArray, deserializer.deserializeStringArray());
        Assert.assertArrayEquals(null, deserializer.deserializeStringArray());
    }

    private static void fill(final String[] array) {
        array[0] = "Hello world!";
        array[array.length / 2] = "42.";
        array[array.length - 1] = new String(new byte[Byte.MAX_VALUE]);
    }

    @Test
    public void shouldSerializeStringArrayWithTruncatedStringLengths() throws Exception {
        final String[] byteLengthStringSizeArray = new String[Byte.MAX_VALUE];
        fill(byteLengthStringSizeArray);
        final String[] shortLengthStringSizeArray = new String[Byte.MAX_VALUE];
        fill(shortLengthStringSizeArray);
        shortLengthStringSizeArray[42] = new String(new byte[Short.MAX_VALUE]);

        serializer.serializeStringArray(byteLengthStringSizeArray, Size.getDefaultArrayLengthSize(), Size.BYTE);
        serializer.serializeStringArray(shortLengthStringSizeArray, Size.getDefaultArrayLengthSize(), Size.SHORT);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(byteLengthStringSizeArray,
                deserializer.deserializeStringArray(Size.getDefaultArrayLengthSize(), Size.BYTE));
        Assert.assertArrayEquals(shortLengthStringSizeArray,
                deserializer.deserializeStringArray(Size.getDefaultArrayLengthSize(), Size.SHORT));
    }

    // Transferables:

    @Test
    public void shouldSerializeTransferables() throws Exception {
        final TransferableObject transferable = new TransferableObject("Secret", 42f, Integer.MIN_VALUE);
        serializer.serializeTransferable(transferable);
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertEquals(transferable, deserializer.deserializeTransferable(TransferableObject.MOCK_UP_INSTANCE));
    }

    @Test
    public void shouldSerializeTransferableArray() throws Exception {
        final TransferableObject[] array = new TransferableObject[] {
                new TransferableObject("Secret", 42f, Integer.MIN_VALUE),
                new TransferableObject("Not a secret", 0f, Integer.MAX_VALUE) };
        serializer.serializeTransferableArray(array);
        for (final Size arraySize : Size.values()) {
            serializer.serializeTransferableArray(array, arraySize);
        }
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(array, deserializer.deserializeTransferableArray(TransferableObject.MOCK_UP_INSTANCE,
                TransferableObject.ARRAY_PROVIDER));
        for (final Size arraySize : Size.values()) {
            Assert.assertArrayEquals(array, deserializer.deserializeTransferableArray(
                    TransferableObject.MOCK_UP_INSTANCE, TransferableObject.ARRAY_PROVIDER, arraySize));
        }
    }

    @Test(expected = SerializationException.class)
    public void shouldThrowExceptionIfSerializingTransferableArrayWithNullsWithOptimizedMethodWhichDoesNotStoreNullabilityData()
            throws Exception {
        final TransferableObject[] array = new TransferableObject[] {
                new TransferableObject("Secret", 42f, Integer.MIN_VALUE), null,
                new TransferableObject("Not a secret", 0f, Integer.MAX_VALUE) };
        serializer.serializeTransferableArray(array);
    }

    @Test
    public void shouldSerializeTransferableArrayWithPossibleNulls() throws Exception {
        final TransferableObject[] array = new TransferableObject[] {
                new TransferableObject("Secret", 42f, Integer.MIN_VALUE), null,
                new TransferableObject("Not a secret", 0f, Integer.MAX_VALUE) };
        serializer.serializeTransferableArrayWithPossibleNulls(array);
        for (final Size arraySize : Size.values()) {
            serializer.serializeTransferableArrayWithPossibleNulls(array, arraySize);
        }
        deserializer.setSerializedData(serializer.serialize());
        Assert.assertArrayEquals(array, deserializer.deserializeTransferableArrayWithPossibleNulls(
                TransferableObject.MOCK_UP_INSTANCE, TransferableObject.ARRAY_PROVIDER));
        for (final Size arraySize : Size.values()) {
            Assert.assertArrayEquals(array, deserializer.deserializeTransferableArrayWithPossibleNulls(
                    TransferableObject.MOCK_UP_INSTANCE, TransferableObject.ARRAY_PROVIDER, arraySize));
        }
    }

    private static class TransferableObject implements Transferable<TransferableObject> {
        /** Utility mock-up object for deserialization. */
        public static final TransferableObject MOCK_UP_INSTANCE = new TransferableObject(null, 0f, 0);
        /** Array provider for deserializing TransferableObject arrays. */
        public static final ArrayProvider<TransferableObject> ARRAY_PROVIDER = new ArrayProvider<TransferableObject>() {
            @Override
            public TransferableObject[] getArray(final int size) {
                return new TransferableObject[size];
            }
        };

        private final String string;
        private final float floating;
        private final int integer;

        public TransferableObject(final String string, final float floating, final int integer) {
            this.string = string;
            this.floating = floating;
            this.integer = integer;
        }

        @Override
        public void serialize(final Serializer serializer) throws SerializationException {
            serializer.serializeString(string, Size.SHORT);
            serializer.serializeFloat(floating, Size.LONG);
            serializer.serializeInt(integer);
        }

        @Override
        public TransferableObject deserialize(final Deserializer deserializer) throws SerializationException {
            return new TransferableObject(deserializer.deserializeString(Size.SHORT),
                    deserializer.deserializeFloat(Size.LONG), deserializer.deserializeInt());
        }

        // GENERATED:
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Float.floatToIntBits(floating);
            result = prime * result + integer;
            result = prime * result + (string == null ? 0 : string.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof TransferableObject)) {
                return false;
            }
            final TransferableObject other = (TransferableObject) obj;
            if (Float.floatToIntBits(floating) != Float.floatToIntBits(other.floating)) {
                return false;
            }
            if (integer != other.integer) {
                return false;
            }
            if (string == null) {
                if (other.string != null) {
                    return false;
                }
            } else if (!string.equals(other.string)) {
                return false;
            }
            return true;
        }
        // /GENERATED
    }
}
