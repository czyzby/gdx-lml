package com.github.czyzby.lml.vis.parser.impl.nongwt;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.AnyFileChooserLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.DirectoryChooserLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.DirectoryLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FavoriteFolderButtonVisibleLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileChooserListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileChooserLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileDeleterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileFilterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.FileTypeFilterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.GroupMultiSelectKeyLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.IconProviderLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.ModeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.MultiSelectKeyLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.PreferencesNameLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.SelectionModeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file.WatchFilesLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.validator.ErrorIfRelativeEmptyLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.validator.FileExistsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.validator.RelativeToFileLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.nongwt.tag.provider.FileChooserLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.nongwt.tag.provider.validator.DirectoryValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.nongwt.tag.provider.validator.EmptyDirectoryContentValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.nongwt.tag.provider.validator.FileValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.nongwt.tag.provider.validator.NotEmptyDirectoryContentValidatorLmlTagProvider;

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
        registerFileChooser(syntax);
        registerFileValidators(syntax);
        return parser;
    }

    /** @param syntax will contain Vis attributes and tags connected with
     *            {@link com.kotcrab.vis.ui.widget.file.FileChooser} widget. */
    public static void registerFileChooser(final LmlSyntax syntax) {
        // Simplified FileChooser attributes, allowing to quickly add a simple file chooser to any widget:
        syntax.addAttributeProcessor(new AnyFileChooserLmlAttribute(), "fileAndDirectoryChooser", "anyFileChooser");
        syntax.addAttributeProcessor(new DirectoryChooserLmlAttribute(), "directoryChooser");
        syntax.addAttributeProcessor(new FileChooserLmlAttribute(), "fileChooser");

        // FileChooser tag:
        syntax.addTagProvider(new FileChooserLmlTagProvider(), "fileChooser");
        // FileChooser attributes:
        syntax.addAttributeProcessor(new DirectoryLmlAttribute(), "directory");
        syntax.addAttributeProcessor(new FavoriteFolderButtonVisibleLmlAttribute(), "favoriteFolderButtonVisible");
        syntax.addAttributeProcessor(new FileChooserListenerLmlAttribute(), "listener", "fileChooserListener");
        syntax.addAttributeProcessor(new FileDeleterLmlAttribute(), "fileDeleter");
        syntax.addAttributeProcessor(new FileFilterLmlAttribute(), "fileFilter");
        syntax.addAttributeProcessor(new FileTypeFilterLmlAttribute(), "fileTypeFilter");
        syntax.addAttributeProcessor(new GroupMultiSelectKeyLmlAttribute(), "groupMultiSelectKey");
        syntax.addAttributeProcessor(new IconProviderLmlAttribute(), "iconProvider");
        syntax.addAttributeProcessor(new ModeLmlAttribute(), "mode");
        syntax.addAttributeProcessor(new MultiSelectKeyLmlAttribute(), "multiSelectKey");
        syntax.addAttributeProcessor(new PreferencesNameLmlAttribute(), "prefsName");
        syntax.addAttributeProcessor(new SelectionModeLmlAttribute(), "selectionMode", "select");
        syntax.addAttributeProcessor(new WatchFilesLmlAttribute(), "watchingFilesEnabled", "watchFiles", "watch");
        // Note: FileChooser#addFavorite(FileHandle) is not supported through LML attributes; this is a design choice,
        // as it would be extremely awkward to use when registering multiple files. It would require a lot of attributes
        // (for each file type), aliases/arrays parsing (for multiple files) or extra awkward syntax, which might be
        // problematic or restricting in attributes. If you want to register attributes, use onCreate or onClose
        // attributes, which allow you to get a reference to the FileChooser during parsing, and register favorites
        // manually.
    }

    /** @param syntax will contain Vis tags and attributes supporting extra form validators from
     *            {@link com.kotcrab.vis.ui.util.form.FormValidator} class. */
    public static void registerFileValidators(final LmlSyntax syntax) {
        // Validator tags:
        syntax.addTagProvider(new DirectoryValidatorLmlTagProvider(), "directoryValidator", "isDirectory");
        syntax.addTagProvider(new EmptyDirectoryContentValidatorLmlTagProvider(), "emptyDirectoryValidator",
                "isDirectoryEmpty");
        syntax.addTagProvider(new FileValidatorLmlTagProvider(), "fileValidator", "isFile");
        syntax.addTagProvider(new NotEmptyDirectoryContentValidatorLmlTagProvider(), "notEmptyDirectoryValidator",
                "isDirectoryNotEmpty");
        // FileExistsValidator attributes:
        syntax.addAttributeProcessor(new ErrorIfRelativeEmptyLmlAttribute(), "errorIfRelativeEmpty");
        syntax.addAttributeProcessor(new FileExistsLmlAttribute(), "exists");
        syntax.addAttributeProcessor(new RelativeToFileLmlAttribute(), "relativeTo");
    }
}
