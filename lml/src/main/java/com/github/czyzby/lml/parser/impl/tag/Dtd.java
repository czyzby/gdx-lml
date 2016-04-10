package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.macro.AbstractConditionalLmlMacroTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.util.Lml;

/** Allows to create DTD schema files for LML templates.
 *
 * @author MJ */
public class Dtd {
    protected static final String XML_ELEMENT_REGEX = "[\\w:.-]+";
    private boolean displayLogs = true;

    /** @param parser contains parsing data. Used to create mock-up actors. The skin MUST be fully loaded and contain
     *            all used actors' styles for the generator to work properly.
     * @return DTD schema file containing all possible tags and their attributes. Any problems with the generation will
     *         be logged. This is a relatively heavy operation and should be done only during development.
     * @see #getDtdSchema(LmlParser) */
    public static String getSchema(final LmlParser parser) {
        return new Dtd().getDtdSchema(parser);
    }

    /** @param displayLogs defaults to true. If set to false, parsing messages will not be shown in the console. */
    public void setDisplayLogs(final boolean displayLogs) {
        this.displayLogs = displayLogs;
    }

    /** @param message will be displayed in the console. */
    protected void log(final String message) {
        if (displayLogs) {
            Gdx.app.log(Lml.LOGGER_TAG, message);
        }
    }

    /** @param parser contains parsing data. Used to create mock-up actors. The skin MUST be fully loaded and contain
     *            all used actors' styles for the generator to work properly.
     * @return DTD schema file containing all possible tags and their attributes. Any problems with the generation will
     *         be logged. This is a relatively heavy operation and should be done only during development. */
    public String getDtdSchema(final LmlParser parser) {
        final StringBuilder builder = new StringBuilder();
        appendActorTags(builder, parser);
        appendActorAttributes(parser, builder);
        appendMacroTags(builder, parser);
        return builder.toString();
    }

    protected void appendDtdElement(final StringBuilder builder, final String comment, final String name) {
        appendDtdElement(builder, comment, Strings.EMPTY_STRING, name);
    }

    protected void appendDtdElement(final StringBuilder builder, final String comment, final String prefix,
            final String name) {
        appendDtdElement(builder, comment, prefix, name, "ANY");
    }

    protected void appendDtdElement(final StringBuilder builder, final String comment, final String prefix,
            final String name, final String type) {
        if (!name.matches(XML_ELEMENT_REGEX)) {
            log("Warning: '" + name + "' tag might contain invalid XML characters.");
        }
        builder.append("<!-- ").append(comment).append(" -->\n");
        builder.append("<!ELEMENT ").append(prefix).append(name).append(' ').append(type).append(">\n");
    }

    protected void appendDtdAttributes(final StringBuilder builder, final String tagName,
            final ObjectMap<String, Object> attributes) {
        builder.append("<!ATTLIST ").append(tagName);
        for (final Entry<String, Object> attribute : attributes) {
            if (!attribute.key.matches(XML_ELEMENT_REGEX)) {
                log("Warning: '" + attribute + "' attribute might contain invalid XML characters.");
            }
            builder.append("\n\t<!-- ").append(attribute.value.getClass().getSimpleName()).append(" -->\n");
            builder.append('\t').append(attribute.key).append(" CDATA #IMPLIED");
        }
        builder.append(">\n");
    }

    protected void appendActorTags(final StringBuilder builder, final LmlParser parser) {
        builder.append("<!-- Actor tags: -->\n");
        final ObjectMap<String, LmlTagProvider> actorTags = parser.getSyntax().getTags();
        for (final Entry<String, LmlTagProvider> actorTag : actorTags) {
            appendDtdElement(builder, getTagClassName(actorTag.value), actorTag.key);
        }
    }

    protected String getTagClassName(final LmlTagProvider provider) {
        final String providerClass = provider.getClass().getSimpleName();
        return providerClass.endsWith("Provider")
                ? providerClass.substring(0, providerClass.length() - "Provider".length()) : providerClass;
    }

    @SuppressWarnings("unchecked")
    protected void appendActorAttributes(final LmlParser parser, final StringBuilder builder) {
        builder.append("<!-- Actor tags' attributes: -->\n");
        final ObjectMap<String, LmlTagProvider> actorTags = parser.getSyntax().getTags();
        for (final Entry<String, LmlTagProvider> actorTag : actorTags) {
            final ObjectMap<String, Object> attributes = GdxMaps.newObjectMap();
            try {
                final LmlTag tag = actorTag.value.create(parser, null, new StringBuilder(actorTag.key));
                LmlActorBuilder actorBuilder;
                final boolean usesAbstractBase = tag instanceof AbstractActorLmlTag;
                if (usesAbstractBase) {
                    actorBuilder = ((AbstractActorLmlTag) tag).getNewInstanceOfBuilder();
                } else {
                    actorBuilder = new LmlActorBuilder();
                }
                // Appending building attributes:
                attributes.putAll(
                        (ObjectMap<String, Object>) (Object) parser.getSyntax().getAttributesForBuilder(actorBuilder));
                // Appending regular attributes:
                attributes.putAll(
                        (ObjectMap<String, Object>) (Object) parser.getSyntax().getAttributesForActor(tag.getActor()));
                // Appending attributes of component actors:
                if (usesAbstractBase && ((AbstractActorLmlTag) tag).hasComponentActors()) {
                    for (final Actor component : ((AbstractActorLmlTag) tag).getComponentActors(tag.getActor())) {
                        attributes.putAll((ObjectMap<String, Object>) (Object) parser.getSyntax()
                                .getAttributesForActor(component));
                    }
                }
            } catch (final Exception exception) {
                Exceptions.ignore(exception);
                log("Warning: unable to create an instance of actor mapped to '" + actorTag.key
                        + "' tag name with provider: " + actorTag.value
                        + ". Attributes list will not be complete. Is the provider properly implemented? Is a default style provided for the selected actor?");
                attributes.putAll(
                        (ObjectMap<String, Object>) (Object) parser.getSyntax().getAttributesForActor(new Actor()));
                attributes.putAll((ObjectMap<String, Object>) (Object) parser.getSyntax()
                        .getAttributesForBuilder(new LmlActorBuilder()));
            }
            appendDtdAttributes(builder, actorTag.key, attributes);
        }
    }

    protected void appendMacroTags(final StringBuilder builder, final LmlParser parser) {
        builder.append("<!-- Macro tags: -->\n");
        final String macroMarker = String.valueOf(parser.getSyntax().getMacroMarker());
        if (!macroMarker.matches(XML_ELEMENT_REGEX)) {
            log("Error: current macro marker (" + macroMarker
                    + ") is an invalid XML character. Override getMacroMarker in your current LmlSyntax implementation and provide a correct character to create valid DTD file.");
        }
        final ObjectMap<String, LmlTagProvider> macroTags = parser.getSyntax().getMacroTags();
        for (final Entry<String, LmlTagProvider> macroTag : macroTags) {
            appendDtdElement(builder, getTagClassName(macroTag.value), macroMarker, macroTag.key);
            // If the tag is conditional, it should provide an extra name:else tag:
            try {
                final LmlTag tag = macroTag.value.create(parser, null, new StringBuilder(macroTag.key));
                if (tag instanceof AbstractConditionalLmlMacroTag) {
                    appendDtdElement(builder, "'Else' helper tag of: " + macroTag.key, macroMarker,
                            macroTag.key + AbstractConditionalLmlMacroTag.ELSE_SUFFIX, "EMPTY");
                }
            } catch (final Exception expected) {
                // Tag might need a parent or additional attributes and cannot be checked. It's OK.
                Exceptions.ignore(expected);
            }
        }
    }
}
