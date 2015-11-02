package com.github.czyzby.lml.vis.parser.impl.nongwt;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.DirectoryChooserLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileChooserLmlAttribute;

/** Allows to easily register extra VisUI features that are not available on GWT.
 *
 * @author MJ */
public class ExtendedVisLml {
    private ExtendedVisLml() {
    }

    /** @param parser will contain extra non-GWT VisUI features.
     * @return passed parser (for chaining). */
    public static LmlParser extend(final LmlParser parser) {
        final LmlSyntax syntax = parser.getSyntax();
        registerFileChooserAttributes(syntax);
        // TODO validators
        return parser;
    }

    /** @param syntax will contain Vis attributes connected with {@link com.kotcrab.vis.ui.widget.file.FileChooser}
     *            widget. */
    public static void registerFileChooserAttributes(final LmlSyntax syntax) {
        syntax.addAttributeProcessor(new DirectoryChooserLmlAttribute(), "directoryChooser");
        syntax.addAttributeProcessor(new FileChooserLmlAttribute(), "fileChooser");
    }
}
