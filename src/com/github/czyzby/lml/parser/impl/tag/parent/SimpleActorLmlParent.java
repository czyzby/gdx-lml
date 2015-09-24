package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

/** Stores a single Group actor. Usually a result of trying to append children to a empty-on-purpose tag, like "blank".
 *
 * @author MJ */
public class SimpleActorLmlParent extends AbstractLmlParent<Actor> {
    public SimpleActorLmlParent(final LmlTagData tagData, final LmlParent<?> parent, final LmlParser parser) {
        super(tagData, new Group(), parent, parser);
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        if (child != null) {
            ((Group) actor).addActor(child);
        }
    }

    @Override
    public void doOnTagClose(final LmlParser parser) {
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        ((Group) actor).addActor(getLabelFromRawDataBetweenTags(data, parser));
    }
}
