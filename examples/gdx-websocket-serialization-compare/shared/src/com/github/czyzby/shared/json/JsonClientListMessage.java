package com.github.czyzby.shared.json;

import com.badlogic.gdx.utils.Array;

/** JSON packet with list of {@link JsonClientMessage}.
 *
 * @author MJ */
public class JsonClientListMessage {
    public Array<JsonClientMessage> messages;
}
