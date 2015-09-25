package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class ActorLmlMacroParent extends AbstractLmlMacroParent {
    public ActorLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent, final LmlParser parser) {
        super(lmlMacroData, parent, parser);
    }

    @Override
    public void closeTag(final LmlParser parser) {
        if (GdxArrays.isEmpty(arguments)) {
            throw new LmlParsingException("Actor macro needs at least one argument: action name.", parser);
        }
        final Actor result = (Actor) parser.findAction(arguments.first(), (Actor) null).consume(null);
        handleChild(result, new LmlTagData("", new ObjectMap<String, String>(), true), parser);
    }
}
