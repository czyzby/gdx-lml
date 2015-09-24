package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class ProgressBarLmlParent extends NonParentalLmlParent<ProgressBar> {
    public ProgressBarLmlParent(final LmlTagData tagData, final ProgressBar actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }
}
