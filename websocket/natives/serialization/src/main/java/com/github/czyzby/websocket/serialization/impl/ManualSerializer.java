package com.github.czyzby.websocket.serialization.impl;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;

/** Uses {@link Serializer} and {@link Deserializer} to handle serialization from objects to byte arrays and vice versa.
 * Expects that all packets implement {@link Transferable} interface. Before using this serializer, all expected packets
 * should be registered with {@link #register(Transferable)} method.
 *
 * <p>
 * This serializer is not thread-safe. When used in multi-threaded environment, synchronization or using thread locals
 * is required.
 *
 * @author MJ
 * @see #register(Transferable)
 * @see #setPacketIdSize(Size) */
public class ManualSerializer extends AbstractBinarySerializer {
    protected static final int UNKNOWN = -1;

    private final Serializer serializer;
    private final Deserializer deserializer;
    /** Packet class mapped to class ID. */
    private final ObjectIntMap<Class<?>> typesToIds = new ObjectIntMap<Class<?>>();
    /** Class ID mapped to mock-up packet. */
    private final IntMap<Transferable<?>> idsToPackets = new IntMap<Transferable<?>>();

    private Size packetIdSize = Size.BYTE;
    private int id;
    private boolean ignoreDeserializationErrors;

    public ManualSerializer() {
        this(new Serializer(), new Deserializer());
    }

    /** @param serializer used to serialize packets to byte arrays.
     * @param deserializer used to create packets from byte arrays. */
    public ManualSerializer(final Serializer serializer, final Deserializer deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /** @param packet a mock up packet instance. Will be registered as one of the handled packets. Note that packets
     *            HAVE to be registered in the same order both on the client and on the server, as registration order
     *            determines packet type ID. Unregistered packets cannot be (de)serialized, so this method should be
     *            called for all expected packets before using the web socket. */
    public void register(final Transferable<?> packet) {
        final int classId = id++;
        typesToIds.put(packet.getClass(), classId);
        idsToPackets.put(classId, packet);
    }

    @Override
    public byte[] serialize(final Object object) {
        try {
            serializer.reset();
            final int classId = typesToIds.get(object.getClass(), UNKNOWN);
            if (classId == UNKNOWN) {
                throw new SerializationException("Unable to serialize unregistered type: " + object.getClass());
            }
            serializer.serializeInt(classId, packetIdSize);
            serializer.serializeTransferable((Transferable<?>) object);
            return serializer.serialize();
        } catch (final SerializationException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new SerializationException("Unable to serialize: " + object, exception);
        }
    }

    @Override
    public Object deserialize(final byte[] data) {
        deserializer.setSerializedData(data);
        final int packetId = deserializer.deserializeInt(packetIdSize);
        final Transferable<?> mockUpPacket = idsToPackets.get(packetId);
        if (mockUpPacket == null) {
            if (!ignoreDeserializationErrors) {
                throw new SerializationException("Unexpected packet class ID: " + packetId
                        + ". Has server and client registered the same packets in the same order?");
            }
            return null;
        }
        return deserializer.deserializeTransferable(mockUpPacket);
    }

    /** @return {@link Serializer} instance used to convert {@link Transferable} objects to byte arrays. */
    public Serializer getSerializer() {
        return serializer;
    }

    /** @return {@link Deserializer} instance used to convert byte arrays to {@link Transferable} objects. */
    public Deserializer getDeserializer() {
        return deserializer;
    }

    /** @param packetIdSize determines how many bytes will be used to serialize packet type. Defaults to
     *            {@link Size#BYTE}, which uses a single byte to serialize packet ID - this allows to use up to
     *            {@link Byte#MAX_VALUE} different packets. If you have more packets in your application, adjust this
     *            value. */
    public void setPacketIdSize(final Size packetIdSize) {
        this.packetIdSize = packetIdSize;
    }

    /** @param ignoreDeserializationErrors if true, deserialization of packets with invalid class ID will be quietly
     *            ignored. Useful on server in production mode, when clients can use old application versions or modify
     *            source code on purpose. {@link SerializationException} will still be thrown if data is otherwise
     *            corrupted. */
    public void setIgnoreDeserializationErrors(final boolean ignoreDeserializationErrors) {
        this.ignoreDeserializationErrors = ignoreDeserializationErrors;
    }
}
