# Custom serialization for LibGDX Web Sockets

Most simple projects will do just fine using JSON-based communication thanks to the default `JsonSerializer`. However, some performance-critical applications might need a faster solution - without the use of reflection and producing much less bytes when serialized. Of course, you can use an existing tested solution like [Kryo](https://github.com/EsotericSoftware/kryo) and implement `Serializer` interface yourself - but then again, if you want to use *web* sockets, you probably need to support *web* platform as well, and existing libraries will rarely work well with GWT. That's why `gdx-websocket` library comes with its custom serialization implementation. While it might be tedious to use at times, it gives you full control over how the data is serialized.

## Including `gdx-websocket-serialization`

See [example project](https://github.com/czyzby/gdx-lml/tree/master/examples/gdx-websocket-serialization-tests).

### Dependencies
`Gradle` dependency (for LibGDX core project):
```
         compile "com.github.czyzby:gdx-websocket-serialization:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

GWT module:
```
         <inherits name='com.github.czyzby.websocket.GdxWebSocketSerialization' />
```

### Comparison with JSON serialization
JSON serialization requires a lot less code, as the classes' fields are scanned with reflection and processed automatically. `gdx-websocket-serialization` forces you to implement `Transferable` interface and (de)serialize your objects manually using `Serializer` and `Deserializer` API. This gives you a lot of control about how your data is serialized: every data type (primitive values, strings, etc.) allows you decide how many bytes you want to use for each value thanks to `Size` enum.

#### `JsonSerializer`:
- Based on tested and stable official LibGDX `Json` API.
- Serialized objects do not need to implement any interfaces or have any annotations to be properly handled.
- Verbose. Serialized data has much more bytes than it could have.
- Relatively slow: uses reflection.
- Outputs strings.
- Uses common format: many frameworks and engines will be able to handle JSON input with ease, even if (or *especially* if) they are not JVM-based.

*When to use*: simple projects; server written in another language; prototyping.

#### `ManualSerializer`:
- Uses `Serializer` and `Deserializer` API.
- Serialized objects need to implement `Transferable` interface and provide (de)serialization methods, forcing the programmer to (de)serialize each field manually.
- Easy to optimize. Serialized data can be as small as possible without truncating the transfered values.
- Serialization is pretty much based on POJOs: no reflection involved, just simple invocation of methods. Much faster than JSON serialization.
- Outputs byte arrays.
- Uses custom format: the server has to use the same API to (de)serialize objects.

*When to use*: performance-critical applications; server running on JVM.

### Example usage
To use this serialization, you need to set the `ManualSerializer` in `WebSocket`:
```
         WebSocket webSocket = ...;
         ManualSerializer serializer = new ManualSerializer();
         webSocket.setSerializer(serializer);
```

Before using the web socket, all expected packets need to be registered in the serializer. The same serialization order has to be preserved in both client and server code, as internally `ManualSerializer` assigns IDs to each class: this is how the serializer is able to determine the type of received object by inspecting the bytes.
```
        serializer.register(new MyPacket()); // Registering a mock-up packet.
```

All that's left to do is implementing the actual packet classes:
```
        public class MyPacket implements Transferable<MyPacket> {
            private String message;
            private float value;

            public MyPacket() {}
            public MyPacket(String message, float value) {
                this.message = message;
                this.value = value;
            }

            @Override
            public void serialize(Serializer serializer) {
                serializer.serializeString(message).serializeFloat(value);
            }

            @Override
            public MyPacket deserialize(Deserializer deserializer) {
                return new MyPacket(deserializer.deserializeString(),
                                    deserializer.deserializeFloat());
            }

            // Getters, setters, toString(), etc.
        }
```
Note that values *have* to be serialized and deserialized in the same order. As you can see, using this serializer adds some boilerplate code to the transfered packets' classes, but in return it allows you to fully control how many bytes are used for each value. For example, you might want to work on `int` values, but if you're 100% sure their value will never exceed `Short#MAX_VALUE`, you can serialize these as shorts:
```
        private int value;
        
        public MyPacket() {}
        public MyPacket(final int value) {
            this.value = value;
        }

        @Override
        public void serialize(final Serializer serializer)  {
            serializer.serializeInt(value, Size.SHORT);
        }

        @Override
        public MyPacket deserialize(final Deserializer deserializer) {
            return new MyPacket(deserializer.deserializeInt(Size.SHORT));
        }
```
We might be saving just 2 lousy bytes there, but when it comes to transferring hundreds of ints in arrays, these kinds of settings start to matter.

You could ask "why not just use `serializeShort` method?" - and you're correct, you can. But by specifying the initial data type, A) the number conversion is handled for you, and B) the least possible amount of bytes is used for the value. For example, if you try serializing int as long: `serializeInt(value, Size.LONG)`, only 4 bytes will be used, as this is the actual int length in bytes. `serializeLong(value)` would use 8 bytes, even if the initial `value` type is int, as JVM silently converts int to a long. This is more of a bug prevention feature, but can be helpful. The choice is yours, use whatever you consider more readable.

Make sure to check out the [example project](https://github.com/czyzby/gdx-lml/tree/master/examples/gdx-websocket-serialization-tests).
