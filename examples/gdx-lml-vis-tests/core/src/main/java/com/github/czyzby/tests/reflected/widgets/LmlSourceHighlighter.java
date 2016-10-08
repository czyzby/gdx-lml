package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.highlight.BaseHighlighter;
import com.kotcrab.vis.ui.util.highlight.Highlight;
import com.kotcrab.vis.ui.util.highlight.HighlightRule;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import regexodus.Matcher;
import regexodus.Pattern;

/**
 * Highlights LML's XML-like syntax when used by a {@link HighlightTextArea}.
 * @author MJ
 */
public class LmlSourceHighlighter extends BaseHighlighter {
    public LmlSourceHighlighter() {
        addPattern(new Color(0.75f, 0.75f, 0.75f, 1f), "<[!][\\s\\S]*?[-!]>");
        addPattern(new Color(0.6f, 1f, 1f, 1f), "</?:\\w*[>]?");
        addPattern(new Color(0.6f, 0.7f, 1f, 1f), "</?[^!:]\\w*[>]?");
        addPattern(new Color(0.8f, 0.6f, 1f, 1f), "\\{.*\\}");
        addPattern(new Color(0.8f, 0.6f, 1f, 1f), "[#@$]");
        word(new Color(0.8f, 0.4f, 1f, 1f), "'");
        word(new Color(0.8f, 0.4f, 1f, 1f), "\"");
        word(new Color(0.6f, 0.7f, 1f, 1f), ">");
        word(new Color(0.6f, 0.7f, 1f, 1f), "/>");
    }

    /**
     * @param color will be used to color values detected by the pattern.
     * @param pattern will be compiled to a {@link Pattern}. Has to be a valid regular expression.
     */
    public void addPattern(Color color, String pattern) {
        addRule(new RegexRule(color, pattern));
    }

    /**
     * GWT-compatible regular expression highlight rule based on vis-ui-contrib.
     * @author Kotcrab
     */
    public static class RegexRule implements HighlightRule {
        private Color color;
        private Pattern pattern;

        public RegexRule(Color color, String regex) {
            this.color = color;
            pattern = Pattern.compile(regex);
        }

        @Override
        public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
            Matcher matcher = pattern.matcher(textArea.getText());
            while (matcher.find()) {
                highlights.add(new Highlight(color, matcher.start(), matcher.end()));
            }
        }
    }
}
