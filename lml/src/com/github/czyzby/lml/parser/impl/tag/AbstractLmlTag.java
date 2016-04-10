package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.lml.util.collection.IgnoreCaseStringMap;

/** Common base for all tag handlers.
 *
 * @author MJ
 * @see AbstractMacroLmlTag */
public abstract class AbstractLmlTag implements LmlTag {
    private static final CharSequence ESCAPED_SPACE = "\\ ";
    private static final CharSequence SPACE = " ";
    private static final CharSequence UNUSUAL_ENOUGH = "%$&";

    private final LmlParser parser;
    private final Array<String> attributes;
    private final ObjectMap<String, String> namedAttributes;
    private final String tagName;
    private final LmlTag parentTag;
    private final boolean parent, macro;

    public AbstractLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        this.parser = parser;
        this.parentTag = parentTag;
        final String[] entities = extractTagEntities(rawTagData);
        final String tagName = entities[0];
        macro = Strings.startsWith(tagName, parser.getSyntax().getMacroMarker());
        this.tagName = LmlUtilities.stripMarker(tagName, parser.getSyntax().getMacroMarker());
        final String lastAttribute = entities[entities.length - 1];
        if (Strings.endsWith(lastAttribute, parser.getSyntax().getClosedTagMarker())) {
            // The tag ends with a closing marker, which means it is a child.
            parent = false;
            entities[entities.length - 1] = LmlUtilities.stripEnding(lastAttribute);
        } else {
            parent = true;
        }
        if (hasAttributes(entities)) {
            attributes = GdxArrays.newArray(String.class);
            if (supportsNamedAttributes() || supportsOptionalNamedAttributes()) {
                namedAttributes = new IgnoreCaseStringMap<String>();
            } else {
                namedAttributes = null;
            }
            fillAttributes(parser, entities);
        } else {
            attributes = null;
            namedAttributes = null;
        }
    }

    /** @return true if this tag type supports named attributes and they should be mapped. */
    protected abstract boolean supportsNamedAttributes();

    /** @return true if this tag can have both named and unnamed attributes. Named attributes will be filled only if ALL
     *         attributes are valid and named, but exception will not be thrown if some of the attributes are invalid.
     *         This is default behavior for some obscure macros. */
    protected boolean supportsOptionalNamedAttributes() {
        return false;
    }

    private static boolean hasAttributes(final String[] entities) {
        return // The first entity is name, so at least 2 entities are required:
        entities.length > 1
                // The last entity might be empty if it was tag closing marker:
                || Strings.isEmpty(entities[entities.length - 1]) && entities.length > 2;
    }

    private static String[] extractTagEntities(final String rawTagData) {
        // No pattern-matcher on GWT.
        final String[] entities = rawTagData.replace(ESCAPED_SPACE, UNUSUAL_ENOUGH)
                .split(Strings.WHITESPACE_SPLITTER_REGEX);
        for (int index = 0, length = entities.length; index < length; index++) {
            entities[index] = entities[index].replace(UNUSUAL_ENOUGH, SPACE);
        }
        return entities;
    }

    private void fillAttributes(final LmlParser parser, final String[] entities) {
        // Starting from 1, since 0 index is the tag name.
        for (int index = 1, length = entities.length; index < length; index++) {
            final String rawAttribute = entities[index];
            if (Strings.isBlank(rawAttribute)) {
                continue;
            }
            final String entity = LmlUtilities.stripQuotation(rawAttribute);
            attributes.add(entity);
            if ((supportsNamedAttributes() || supportsOptionalNamedAttributes()) && Strings.isNotEmpty(entity)) {
                final int separatorIndex = entity.indexOf(parser.getSyntax().getAttributeSeparator());
                if (Strings.isCharacterAbsent(separatorIndex)) {
                    if (supportsNamedAttributes()) {
                        parser.throwErrorIfStrict("Invalid attribute format: \"" + entity
                                + "\". Attribute might be missing assignment character ('"
                                + parser.getSyntax().getAttributeSeparator() + "') or be otherwise unparseable.");
                    }
                    // Supports optionally: parsing another attribute.
                    continue;
                }
                final String attributeName = entity.substring(0, separatorIndex);
                final String attributeValue = LmlUtilities
                        .stripQuotation(entity.substring(separatorIndex + 1, entity.length()));
                namedAttributes.put(attributeName, attributeValue);
            }
        }
    }

    @Override
    public boolean isParent() {
        return parent;
    }

    @Override
    public boolean isChild() {
        return !parent;
    }

    @Override
    public boolean isMacro() {
        return macro;
    }

    @Override
    public LmlTag getParent() {
        return parentTag;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public Array<String> getAttributes() {
        return attributes;
    }

    @Override
    public ObjectMap<String, String> getNamedAttributes() {
        return namedAttributes;
    }

    @Override
    public boolean hasAttribute(final String name) {
        return namedAttributes != null && namedAttributes.containsKey(name);
    }

    @Override
    public String getAttribute(final String name) {
        return namedAttributes == null ? null : namedAttributes.get(name);
    }

    /** @return parser used to create this tag. */
    protected LmlParser getParser() {
        return parser;
    }

    /** @param builder contains ID of the skin.
     * @return utility shortcut method that returns skin from parser's LML data object. */
    protected Skin getSkin(final LmlActorBuilder builder) {
        return getSkin(builder.getSkinName());
    }

    /** @param name ID of the skin.
     * @return utility shortcut method that returns skin from parser's LML data object. */
    protected Skin getSkin(final String name) {
        final Skin skin = findSkin(name);
        if (skin == null) {
            parser.throwError("Unknown skin ID. Skin with name: " + name + " is unavailable.");
        }
        return skin;
    }

    private Skin findSkin(final String name) {
        if (name == null) {
            return parser.getData().getDefaultSkin();
        }
        return parser.getData().getSkin(name);
    }

    @Override
    public boolean isAttachable() {
        // By default, both macros and simple widgets are not attachable.
        return false;
    }

    @Override
    public void attachTo(final LmlTag tag) {
        // By default, both macros and simple widgets are not attachable.
        throw new IllegalStateException("This tag is not attachable: " + tagName);
    }
}
