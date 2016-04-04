package com.github.czyzby.lml.vis.parser.impl.tag.provider;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.VisTreeLmlTag;

/** Handles VisTree tags
 * 
 * @author Kotcrab */
public class VisTreeLmlTagProvider implements LmlTagProvider {
    @Override
    public LmlTag create(LmlParser parser, LmlTag parentTag, String rawTagData) {
        return new VisTreeLmlTag(parser, parentTag, rawTagData);
    }
}
