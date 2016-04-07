package com.github.czyzby.shared;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

/** Utility class. Allows to easily register packets in the same order on both client and server.
 *
 * @author MJ */
public class MyPackets {
    private MyPackets() {
    }

    public static void register(final ManualSerializer serializer) {
        // Note that the packets use simple, primitive data, but nothing stops you from using more complex types like
        // strings, arrays or even other transferables. Both Serializer and Deserializer APIs are well documented: make
        // sure to check them out.
        serializer.register(new Ping());
        serializer.register(new Pong());
    }
}
