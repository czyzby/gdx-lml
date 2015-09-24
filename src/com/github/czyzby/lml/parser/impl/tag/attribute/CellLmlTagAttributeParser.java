package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum CellLmlTagAttributeParser implements LmlTagAttributeParser {
    ALIGN("align") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.align(LmlAttributes.parseAlignment(getCellActor(cell), parser, attributeValue));
        }
    },
    COLSPAN("colspan") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.colspan(LmlAttributes.parseInt(getCellActor(cell), parser, attributeValue));
        }
    },
    EXPAND("expand") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final boolean expand = LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue);
            cell.expand(expand, expand);
        }
    },
    EXPAND_X("expandX") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            if (LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue)) {
                cell.expandX();
            }
        }
    },
    EXPAND_Y("expandY") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            if (LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue)) {
                cell.expandY();
            }
        }
    },
    FILL("fill") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.fill(LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue));
        }
    },
    FILL_X("fillX") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            if (LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue)) {
                cell.fillX();
            }
        }
    },
    FILL_Y("fillY") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            if (LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue)) {
                cell.fillY();
            }
        }
    },
    PAD("pad") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.pad(LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PAD_TOP("padTop") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.padTop(LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PAD_BOTTOM("padBottom") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.padBottom(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PAD_LEFT("padLeft") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.padLeft(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PAD_RIGHT("padRight") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.padRight(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    SPACE("space") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.space(LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    SPACE_TOP("spaceTop") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.spaceTop(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    SPACE_BOTTOM("spaceBottom") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.spaceBottom(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    SPACE_LEFT("spaceLeft") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.spaceLeft(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    SPACE_RIGHT("spaceRight") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.spaceRight(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    UNIFORM("uniform") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final boolean uniform = LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue);
            cell.uniform(uniform, uniform);
        }
    },
    UNIFORM_X("uniformX") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            boolean uniformY;
            try {
                uniformY = cell.getUniformY();
            } catch (final Throwable exception) {
                // Ugly, ugly API. Uniform Y is kept in a Boolean and can be null.
                uniformY = false;
            }
            cell.uniform(LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue), uniformY);
        }
    },
    UNIFORM_Y("uniformY") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            boolean uniformX;
            try {
                uniformX = cell.getUniformX();
            } catch (final Throwable exception) {
                // Ugly, ugly API. Uniform X is kept in a Boolean and can be null.
                uniformX = false;
            }
            cell.uniform(uniformX, LmlAttributes.parseBoolean(getCellActor(cell), parser, attributeValue));
        }
    },
    HEIGHT("height") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.height(LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    MIN_HEIGHT("minHeight") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.minHeight(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PREF_HEIGHT("prefHeight") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.prefHeight(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    MAX_HEIGHT("maxHeight") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.prefHeight(
                    LmlAttributes.parseVerticalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    WIDTH("width") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.width(LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    MIN_WIDTH("minWidth") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.minWidth(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    PREF_WIDTH("prefWidth") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.prefWidth(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    },
    MAX_WIDTH("maxWidth") {
        @Override
        protected void apply(final Cell<?> cell, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            cell.maxWidth(
                    LmlAttributes.parseHorizontalValue(getCellActor(cell), cell.getTable(), parser, attributeValue));
        }
    };
    private final String[] aliases;

    private CellLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((Cell<?>) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(Cell<?> cell, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    protected Actor getCellActor(final Cell<?> cell) {
        return cell.getActor() == null ? cell.getTable() : cell.getActor();
    }

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
