package com.github.czyzby.views.container;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.views.Echo;
import com.kotcrab.vis.ui.widget.VisTextField;

/** To avoid flooding classes with actors and too much display-related logic, you can use LML injecting mechanism which
 * allows you to easily construct and fill objects with actor references. This is an example of such object - instead of
 * keeping all actor fields in {@link Echo} and managing disable statuses there, this functionality is delegated here,
 * to {@link WebSocketButtons} class.
 *
 * @author MJ */
public class WebSocketButtons {
    @LmlActor("input") VisTextField input;
    @LmlActor("open") Button connectingButton;
    @LmlActor("send") Button sendingButton;
    @LmlActor("close") Button disconnectingButton;

    /** Enables sending widgets. Disables connecting widgets. */
    public void onConnect() {
        input.setDisabled(false);
        sendingButton.setDisabled(false);
        connectingButton.setDisabled(true);
        disconnectingButton.setDisabled(false);
    }

    /** Disables sending widgets. Enables connecting widgets. */
    public void onDisconnect() {
        input.setDisabled(true);
        sendingButton.setDisabled(true);
        connectingButton.setDisabled(false);
        disconnectingButton.setDisabled(true);
    }
}
