package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.ActorLmlMacroParent;

/** Invokes an action returning an actor. Adds the actor to its parent.
 *
 * @author MJ */
public class ActorLmlMacroParser extends AbstractLmlMacroParser {
    @Override
    public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
            final LmlParent<?> parent) {
        return new ActorLmlMacroParent(lmlMacroData, parent, parser);
    }

    @Override
    protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
        return null;
    }
}
