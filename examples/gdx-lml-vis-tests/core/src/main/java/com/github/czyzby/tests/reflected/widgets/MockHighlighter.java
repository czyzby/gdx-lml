package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.highlight.BaseHighlighter;
import com.kotcrab.vis.ui.util.highlight.Highlight;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Mock-up {@link BaseHighlighter} implementation that does not color the text.
 * @author MJ
 */
public class MockHighlighter extends BaseHighlighter {
    @Override
    public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
        // Does nothing.
    }
}
