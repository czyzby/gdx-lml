package com.github.czyzby.context.event;

import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.processor.event.MessageListener;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** When {@link OnMessage}} is used on a method, it will be invoked each time the selected string message is posted.
 * While useful, such listeners are based on reflection, which might not be the best solution for commonly posted
 * messages that could cause performance bottlenecks.
 *
 * <p>
 * Fortunately, this annotation can be used directly on a class to avoid reflection use - once the listener is created,
 * it is used as a simple POJO class and its {@link MessageListener#processMessage()} method is invoked each time the
 * message is posted. Classes annotated with {@link OnMessage} are regular components: they can inject other values, can
 * be injected into other components, can use initiate/destroy methods, etc.
 *
 * <p>
 * The processMessage method returns a boolean - if it matches {@link OnMessage#REMOVE}, message listener will be
 * removed and will no longer be notified about the selected message. This is a very useful mechanism that allows you to
 * remove listeners when they are no longer needed.
 *
 * @author MJ */
@OnMessage("myMessage")
public class MyMessageHandler implements MessageListener {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyMessageHandler.class);

    @Override
    public boolean processMessage() {
        LOGGER.info("I detected 'myMessage'.");
        return OnMessage.KEEP; // If you change this to OnMessage.REMOVE, listener would be removed after first message.
    }
}
