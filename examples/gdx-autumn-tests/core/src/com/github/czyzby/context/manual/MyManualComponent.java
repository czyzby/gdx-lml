package com.github.czyzby.context.manual;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.processor.event.EventDispatcher;
import com.github.czyzby.autumn.processor.event.MessageDispatcher;
import com.github.czyzby.context.asset.MyAssetComponent;
import com.github.czyzby.context.provider.MyLabelProvider;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** Some components cannot or should not be created by Autumn due to various reasons (complexity, being an external
 * class, application listener needs direct reference etc.). This is an example of a component manually created and
 * added to the Autumn context. Note that while this classes is not found by scanning, it is still processed by Autumn
 * and can contain any supported field and method annotations.
 *
 * @author MJ */
public class MyManualComponent {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyManualComponent.class);

    /** Assets component will be injected. */
    @Inject MyAssetComponent assets;
    /** {@link Label} instance will be provided by {@link MyLabelProvider}. */
    @Inject Label label;
    /** Used for Autumn events API examples. */
    @Inject MessageDispatcher messageDispatcher;
    @Inject EventDispatcher eventDispatcher;

    public MyManualComponent() {
        LOGGER.info("I was created manually. I'm {0}.", this);
    }

    /** @return injected assets component. */
    public MyAssetComponent getAssets() {
        return assets;
    }

    /** @return a label provided by {@link MyLabelProvider}. */
    public Label getLabel() {
        return label;
    }

    /** @return {@link MessageDispatcher} allowing to post messages to notify message listeners. */
    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    /** @return {@link EventDispatcher} allowing to post events to notify event listeners. */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    public String toString() {
        return "(MyManualComponent[hashCode=" + hashCode() + "])";
    }
}
