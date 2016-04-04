package com.github.czyzby.websocket.impl;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.google.gwt.typedarrays.client.ArrayBufferNative;
import com.google.gwt.typedarrays.client.Int8ArrayNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Int8Array;

/** Wraps around GWT native web sockets. Loosely based on com.sksamuel.gwt.websockets.Websocket class.
 *
 * @author MJ */
public class WebSocket {
    private final GwtWebSocket socket;

    /** @param socket cannot ben ull. */
    public WebSocket(final GwtWebSocket socket) {
        this.socket = socket;
    }

    /** @return true if web sockets are supported by the browser. */
    public static native boolean areWebSocketsSupported() /*-{
                                                          return ("WebSocket" in window);
                                                          }-*/;

    /** Creates the web socket object, adding listeners.
     *
     * @param url cannot be null.
     * @throws WebSocketException if unable to open the socket. */
    public void open(final String url) throws WebSocketException {
        if (url == null) {
            throw new WebSocketException("URL cannot be null.");
        }
        try {
            createWebSocket(url);
        } catch (final Throwable exception) {
            throw new WebSocketException("Unable to connect.", exception);
        }
    }

    private native void createWebSocket(String url)/*-{
                                                   var self = this;
                                                   if(self.ws) {
                                                   	self.ws.close(1001);
                                                   }
                                                   self.ws = new WebSocket(url);
                                                   self.ws.onopen = function() { self.@com.github.czyzby.websocket.impl.WebSocket::onOpen()(); };
                                                   self.ws.binaryType = 'arraybuffer';
                                                   self.ws.onclose = function(event) { self.@com.github.czyzby.websocket.impl.WebSocket::onClose(ILjava/lang/String;)(event.code, event.reason); };
                                                   self.ws.onerror = function(error) { self.@com.github.czyzby.websocket.impl.WebSocket::onError(Ljava/lang/String;Ljava/lang/String;)(error.type,error.toString()); };
                                                   self.ws.onmessage = function(msg) {
                                                   if (typeof(msg.data) == 'string' ) {
                                                   self.@com.github.czyzby.websocket.impl.WebSocket::onMessage(Ljava/lang/String;)(msg.data);
                                                   }else{
                                                   self.@com.github.czyzby.websocket.impl.WebSocket::onMessage(Lcom/google/gwt/typedarrays/shared/ArrayBuffer;)(msg.data);
                                                   }
                                                   }
                                                   }-*/;

    protected void onOpen() {
        socket.postOpenEvent();
    }

    protected void onClose(final int closeCode, final String reason) {
        socket.postCloseEvent(WebSocketCloseCode.getByCodeOrElse(closeCode, WebSocketCloseCode.ABNORMAL), reason);
    }

    protected void onError(final String type, final String message) {
        socket.postErrorEvent(
                new WebSocketException("An error occurred. Error type: " + type + ", error event message: " + message));
    }

    protected void onMessage(final ArrayBuffer arrayBuffer) {
        if (arrayBuffer != null && arrayBuffer.byteLength() > 0) {
            final byte[] message = toByteArray(arrayBuffer);
            if (message.length > 0) {
                socket.postMessageEvent(message);
            }
        }
    }

    private static byte[] toByteArray(final ArrayBuffer arrayBuffer) {
        final Int8Array array = Int8ArrayNative.create(arrayBuffer);
        final int length = array.byteLength();
        final byte[] byteArray = new byte[length];
        for (int index = 0; index < length; index++) {
            byteArray[index] = array.get(index);
        }
        return byteArray;
    }

    protected void onMessage(final String message) {
        if (message != null && message.length() > 0) {
            socket.postMessageEvent(message);
        }
    }

    /** Closes the web socket with normal state and no specified reason. */
    public void close() {
        close(WebSocketCloseCode.NORMAL, null);
    }

    /** @param code closing code that server will receive. Cannot be null.
     * @param reason closing reason. Optional. */
    public void close(final WebSocketCloseCode code, final String reason) {
        close(code.getCode(), reason);
    }

    private native void close(int code, String reason)/*-{
                                                      if(this.ws){
                                                      this.ws.close(code,reason);
                                                      }
                                                      }-*/;

    /** @return true if web socket is currently open and secure. */
    public boolean isSecure() {
        final String url = getUrl();
        return url != null && url.substring(0, 3).equalsIgnoreCase("wss");
    }

    /** @return current URL that the web socket stored or null. */
    public native String getUrl()/*-{
                                 if(this.ws) {
                                 return this.ws.url;
                                 }
                                 return null;
                                 }-*/;

    /** @return current web socket state. If not open yet, returns CLOSED (3). */
    public WebSocketState getState() {
        try {
            return WebSocketState.getById(getStateId());
        } catch (final Throwable exception) {
            // Might be thrown if invalid state, for some reason.
            socket.postErrorEvent(exception);
            return WebSocketState.CLOSED;
        }
    }

    private native int getStateId()/*-{
                                   if(this.ws) {
                                   return this.ws.readyState;
                                   }
                                   return 3;
                                   }-*/;

    /** @param message will be sent to the server as a byte buffer if web socket exists. Nulls are ignored. */
    public void send(final byte[] message) {
        if (message != null) {
            final ArrayBuffer arrayBuffer = ArrayBufferNative.create(message.length);
            final Int8Array array = Int8ArrayNative.create(arrayBuffer);
            array.set(message);
            try {
                sendBytes(arrayBuffer);
            } catch (final Throwable exception) {
                throw new WebSocketException(exception);
            }
        }
    }

    private native void sendBytes(ArrayBuffer message)/*-{
                                                      	if(this.ws) {
                                                      	this.ws.send(message);
                                                      	}
                                                      	}-*/;

    /** @param message will be sent to the server if web socket exists. Nulls are ignored. */
    public void send(final String message) {
        if (message != null) {
            try {
                sendText(message);
            } catch (final Throwable exception) {
                throw new WebSocketException(exception);
            }
        }
    }

    private native void sendText(String message)/*-{
                                                if(this.ws) {
                                                this.ws.send(message);
                                                }
                                                }-*/;

    /** @return true if web socket is in connecting state. */
    public boolean isConnecting() {
        return getState() == WebSocketState.CONNECTING;
    }

    /** @return true if web socket is in open state. */
    public boolean isOpen() {
        return getState() == WebSocketState.OPEN;
    }

    /** @return true if web socket is in closing state. */
    public boolean isClosing() {
        return getState() == WebSocketState.CLOSING;
    }

    /** @return true if web socket is in closed state. */
    public boolean isClosed() {
        return getState() == WebSocketState.CLOSED;
    }
}
