package com.github.czyzby.lml.parser.impl.util;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.macro.AbsoluteImportLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.ActorLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.AssignLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.ClasspathImportLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.ConditionLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.EvaluateLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.ExternalImportLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.ForEachLoopLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.InternalImportLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.LmlMetaMacroParser;
import com.github.czyzby.lml.parser.impl.macro.LocalImportLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.LoopLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.NestedForEachLoopLmlMacroParser;
import com.github.czyzby.lml.parser.impl.macro.NullCheckLmlMacroParser;
import com.github.czyzby.lml.parser.impl.tag.ButtonLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.CheckBoxLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.DialogLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.EmptyLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.HorizontalGroupLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.ImageLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.LabelLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.ListLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.ProgressBarLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.RowLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.ScrollPaneLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.SliderLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.SplitPaneLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.StackLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TableLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TextAreaLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TextButtonLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TextFieldLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TooltipLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.TreeLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.VerticalGroupLmlTagDataParser;
import com.github.czyzby.lml.parser.impl.tag.WindowLmlTagDataParser;

/** Syntax utilities.
 *
 * @author MJ */
public class LmlTagParsers {
    private LmlTagParsers() {
    }

    public static void registerDefaultMacroSyntax(final LmlParser lmlParser) {
        lmlParser.registerMacroParser(new LmlMetaMacroParser(), "macro");
        lmlParser.registerMacroParser(new InternalImportLmlMacroParser(), "import", "include", "require", "template",
                "internalImport", "internalInclude", "internalRequire", "internalTemplate");
        lmlParser.registerMacroParser(new ExternalImportLmlMacroParser(), "externalImport", "externalInclude",
                "externalRequire", "externalTemplate");
        lmlParser.registerMacroParser(new LocalImportLmlMacroParser(), "localImport", "localInclude", "localRequire",
                "localTemplate");
        lmlParser.registerMacroParser(new AbsoluteImportLmlMacroParser(), "absoluteImport", "absoluteInclude",
                "absoluteRequire", "absoluteTemplate");
        lmlParser.registerMacroParser(new ClasspathImportLmlMacroParser(), "classpathImport", "classpathInclude",
                "classpathRequire", "classpathTemplate");
        lmlParser.registerMacroParser(new ForEachLoopLmlMacroParser(), "forEach", "for", "each");
        lmlParser.registerMacroParser(new NestedForEachLoopLmlMacroParser(), "forEachNested", "nested", "nestedForEach",
                "eachNested");
        lmlParser.registerMacroParser(new LoopLmlMacroParser(), "loop", "while", "times");
        lmlParser.registerMacroParser(new ConditionLmlMacroParser(), "if", "test", "check", "try", "verify", "inspect",
                "validate", "onCondition", "condition", "conditional");
        lmlParser.registerMacroParser(new NullCheckLmlMacroParser(), "ifNotNull", "ifPresent", "notNull", "exists",
                "ifExists", "nullCheck");
        lmlParser.registerMacroParser(new AssignLmlMacroParser(), "assign", "var", "val");
        lmlParser.registerMacroParser(new EvaluateLmlMacroParser(), "eval", "evaluate", "invoke");
        lmlParser.registerMacroParser(new ActorLmlMacroParser(), "actor", "widget");
    }

    public static void registerDefaultTagSyntax(final LmlParser lmlParser) {
        lmlParser.registerParser(new EmptyLmlTagDataParser(), "empty", "blank", "null", "placeholder");
        lmlParser.registerParser(new TableLmlTagDataParser(), "table", "div", "td", "th");
        lmlParser.registerParser(new RowLmlTagDataParser(), "row", "tr", "br");
        lmlParser.registerParser(new LabelLmlTagDataParser(), "label", "text", "li");
        lmlParser.registerParser(new ButtonLmlTagDataParser(), "button");
        lmlParser.registerParser(new TextButtonLmlTagDataParser(), "textButton", "a");
        lmlParser.registerParser(new CheckBoxLmlTagDataParser(), "checkBox", "check");
        lmlParser.registerParser(new ScrollPaneLmlTagDataParser(), "scrollPane", "scrollable", "scroll");
        lmlParser.registerParser(new SplitPaneLmlTagDataParser(), "splitPane", "splitable", "split");
        lmlParser.registerParser(new StackLmlTagDataParser(), "stack");
        lmlParser.registerParser(new ImageLmlTagDataParser(), "image", "icon", "img");
        lmlParser.registerParser(new HorizontalGroupLmlTagDataParser(), "horizontalGroup", "horizontal");
        lmlParser.registerParser(new VerticalGroupLmlTagDataParser(), "verticalGroup", "vertical");
        lmlParser.registerParser(new TextFieldLmlTagDataParser(), "textField", "input", "textInput");
        lmlParser.registerParser(new TextAreaLmlTagDataParser(), "textArea", "inputArea", "area");
        lmlParser.registerParser(new ProgressBarLmlTagDataParser(), "progressBar", "progress", "loadingBar", "loading");
        lmlParser.registerParser(new SliderLmlTagDataParser(), "slider");
        lmlParser.registerParser(new ListLmlTagDataParser(), "list", "ul");
        lmlParser.registerParser(new TreeLmlTagDataParser(), "tree", "root");
        lmlParser.registerParser(new WindowLmlTagDataParser(), "window");
        lmlParser.registerParser(new DialogLmlTagDataParser(), "dialog", "popup");
        lmlParser.registerParser(new TooltipLmlTagDataParser(), "tooltip", "info", "onHover");
    }
}