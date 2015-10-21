package com.github.czyzby.lml.vis.parser.impl.tag.provider;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.VisSelectBoxLmlTag;

/**
 * Handles VisSelectBox tags
 * @author Kotcrab
 */
public class VisSelectBoxLmlTagProvider implements LmlTagProvider {
    @Override
    public LmlTag create(LmlParser parser, LmlTag parentTag, String rawTagData) {
        return new VisSelectBoxLmlTag(parser, parentTag, rawTagData);
    }
}
