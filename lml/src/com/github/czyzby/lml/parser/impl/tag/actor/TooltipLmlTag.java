package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.builder.TooltipLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.scene2d.ui.reflected.TooltipTable;
import com.github.czyzby.lml.util.LmlParsingException;

/** Handles {@link Tooltip} listener represented by a {@link TooltipTable} actor. Handles and appends children like a
 * table - its tag can contain any table attributes, and its children's tag can have cell attributes and will be
 * properly handled. Attached to its parent tag above it. Mapped to "tooltip".
 *
 * @author MJ */
public class TooltipLmlTag extends TableLmlTag {
    private Tooltip<TooltipTable> tooltip;

    public TooltipLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected TooltipLmlActorBuilder getNewInstanceOfBuilder() {
        return new TooltipLmlActorBuilder();
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        final TooltipManager manager = getTooltipManager((TooltipLmlActorBuilder) builder);
        final TooltipTable table = TooltipTable.create(getSkin(builder), manager);
        tooltip = table.getTooltip();
        return table;
    }

    /** @param builder contains tooltip building data.
     * @return an instance of tooltip manager with the ID selected by the builder or default tooltip manager.
     * @throws LmlParsingException if parser is strict and the ID is invalid. */
    protected TooltipManager getTooltipManager(final TooltipLmlActorBuilder builder) {
        TooltipManager manager = getParser().getData().getTooltipManager(builder.getTooltipManager());
        if (manager == null) {
            getParser().throwErrorIfStrict("Could not find tooltip manager for name: " + builder.getTooltipManager());
            manager = TooltipManager.getInstance();
        }
        return manager;
    }

    @Override
    public boolean isAttachable() {
        return true;
    }

    @Override
    public void attachTo(final LmlTag tag) {
        tag.getActor().addListener(tooltip);
    }

    /** @return managed instance of tooltip. */
    public Tooltip<TooltipTable> getTooltip() {
        return tooltip;
    }

    @Override
    protected void doOnTagClose() {
        super.doOnTagClose();
        tooltip.getContainer().pack();
    }
}
