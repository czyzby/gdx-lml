package com.github.czyzby.context.event;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnEvent;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.processor.event.EventDispatcher;
import com.github.czyzby.autumn.processor.event.MessageDispatcher;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** Event-driven architecture is relatively easy way of achieving communication between different application components
 * without dependencies hell. After all, a simple listener that reacts to a selected string message is much easier to
 * transfer to another project than a complex class that requires 5 others.
 *
 * <p>
 * Autumn makes it easier to communicate with events thanks to its unified event API. By using simple (yet powerful)
 * {@link OnEvent} and {@link OnMessage} annotations you can turn any methods into an event listener. You can post
 * events using {@link EventDispatcher} and {@link MessageDispatcher} components. This class demonstrates the standard
 * use of Autumn events API.
 *
 * <p>
 * The main difference between messages and events is that the latter can carry additional parameters. Messages should
 * be used when some basic events occur and no additional data is needed to handle them other than the simple fact that,
 * well, they happened. For example, "assetsLoaded" or "connectedWithServer" could be good messages. On contrary, any
 * Java object can become an event, so event API is much more powerful than messages - it should be used when simple
 * messages are simply not enough. A good example of an event could be ServerPacket.
 *
 * <p>
 * Note that method-based event listeners are using reflection. If you notice that they are causing any delays, try
 * using POJO event listeners by annotating classes rather than methods. Don't be scared though, they are not much
 * harder to use than method-based listeners. See {@link MyEventHandler} and {@link MyMessageHandler} for some examples.
 *
 * @author MJ */
@Component
public class MyHandler {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyHandler.class);

    /** Will be invoked any time "myMessage" is posted using {@link MessageDispatcher}. Note that annotated listener
     * methods can contain any injectable parameters, using the same mechanism as {@link Inject} or {@link Initiate}
     * annotations.
     *
     * @param eventDispatcher will be automatically injected. */
    @OnMessage("myMessage")
    public void onMessage(final EventDispatcher eventDispatcher) {
        LOGGER.info("I detected 'myMessage'. Posting MyEvent.");
        final MyEvent event = new MyEvent("MyHandler#onMessage");
        eventDispatcher.postEvent(event);
    }

    /** Will be invoked any time {@link MyEvent} instance is posted using {@link EventDispatcher}. Note that annotated
     * listener methods can contain any injectable parameters, using the same mechanism as {@link Inject} or
     * {@link Initiate} annotations, as well as {@link MyEvent} parameter.
     * <p>
     * By returning a boolean result, you can decide whether the event listener should be kept or removed. This allows
     * you to easily removed no longer needed listeners. (Note that same applies to OnMessage methods.)
     *
     * @param event will be injected.
     * @return see {@link OnEvent#KEEP} and {@link OnEvent#REMOVE}. */
    @OnEvent(MyEvent.class)
    public boolean onEvent(final MyEvent event) {
        LOGGER.info("I detected a MyEvent: {0}. Removing this listener.", event.getMessage());
        return OnEvent.REMOVE;
    }
}
