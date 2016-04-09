package com.github.czyzby.context.event;

import com.github.czyzby.autumn.annotation.OnEvent;
import com.github.czyzby.autumn.processor.event.EventListener;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** When {@link OnEvent}} is used on a method, it will be invoked each time the event is posted. While useful, such
 * listeners are based on reflection, which might not be the best solution for commonly posted events that could cause
 * performance bottlenecks.
 *
 * <p>
 * Fortunately, this annotation can be used directly on a class to avoid reflection use - once the listener is created,
 * it is used as a simple POJO class and its {@link EventListener#processEvent(Object)} method is invoked each time the
 * event is posted. Classes annotated with {@link OnEvent} are regular components: they can inject other values, can be
 * injected into other components, can use initiate/destroy methods, etc.
 *
 * <p>
 * The processEvent method returns a boolean - if it matches {@link OnEvent#REMOVE}, event listener will be removed and
 * will no longer be notified about the posted events of selected types. This is a very useful mechanism that allows you
 * to remove event listeners when they are no longer needed.
 *
 * @author MJ */
@OnEvent(MyEvent.class)
public class MyEventHandler implements EventListener<MyEvent> {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyEventHandler.class);

    @Override
    public boolean processEvent(final MyEvent event) {
        LOGGER.info("I detected a MyEvent: {0}.", event.getMessage());
        return OnEvent.KEEP; // If you change this to OnEvent.REMOVE, listener would be removed after first event.
    }
}
