package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.SimpleActorLmlParent;

/** Appends an empty actor (as a child) or an simple actor group (as a parent).
 *
 * @author MJ */
public class EmptyLmlTagDataParser extends AbstractLmlTagDataParser<Actor> {
    private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

    private final ObjectMap<String, LmlTagAttributeParser> attributeParsers = new ObjectMap<String, LmlTagAttributeParser>(
            ATTRIBUTE_PARSERS);

    static {
        ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
    }

    public static void registerParser(final LmlTagAttributeParser parser) {
        for (final String alias : parser.getAttributeNames()) {
            ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
        }
    }

    public static void unregisterParser(final String withAlias) {
        ATTRIBUTE_PARSERS.remove(withAlias);
    }

    @Override
    protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
        super.parseAttributes(lmlTagData, parser, actor);
        if (GdxMaps.isNotEmpty(attributeParsers)) {
            for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
                if (attributeParsers.containsKey(attribute.key)) {
                    attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
                }
            }
        }
    }

    @Override
    public void registerAttributeParser(final LmlTagAttributeParser parser) {
        for (final String alias : parser.getAttributeNames()) {
            attributeParsers.put(alias.toUpperCase(), parser);
        }
    }

    @Override
    public void unregisterAttributeParser(final String attributeName) {
        attributeParsers.remove(attributeName);
    }

    @Override
    protected Actor parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
        return new Actor();
    }

    @Override
    protected LmlParent<Actor> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
            final LmlParent<?> parent) {
        return new SimpleActorLmlParent(lmlTagData, parent, parser);
    }
}
