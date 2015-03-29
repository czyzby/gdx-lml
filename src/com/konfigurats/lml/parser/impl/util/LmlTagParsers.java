package com.konfigurats.lml.parser.impl.util;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.macro.AssignLmlMacroParser;
import com.konfigurats.lml.parser.impl.macro.ConditionLmlMacroParser;
import com.konfigurats.lml.parser.impl.macro.ForEachLoopLmlMacroParser;
import com.konfigurats.lml.parser.impl.macro.InternalImportLmlMacroParser;
import com.konfigurats.lml.parser.impl.macro.LmlMetaMacroParser;
import com.konfigurats.lml.parser.impl.macro.LoopLmlMacroParser;
import com.konfigurats.lml.parser.impl.macro.NestedForEachLoopLmlMacroParser;
import com.konfigurats.lml.parser.impl.tag.ButtonLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.CheckBoxLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.DialogLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.EmptyLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.HorizontalGroupLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.ImageLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.LabelLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.ListLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.ProgressBarLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.RowLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.ScrollPaneLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.SliderLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.SplitPaneLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.StackLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.TableLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.TextAreaLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.TextButtonLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.TextFieldLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.TreeLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.VerticalGroupLmlTagDataParser;
import com.konfigurats.lml.parser.impl.tag.WindowLmlTagDataParser;

/** Syntax utilities.
 *
 * @author MJ */
public class LmlTagParsers {
	private LmlTagParsers() {
	}

	public static void registerDefaultMacroSyntax(final LmlParser lmlParser) {
		lmlParser.registerMacroParser(new LmlMetaMacroParser(), "macro");
		lmlParser.registerMacroParser(new InternalImportLmlMacroParser(), "import", "include", "require",
				"template");
		lmlParser.registerMacroParser(new ForEachLoopLmlMacroParser(), "forEach", "for", "each");
		lmlParser.registerMacroParser(new NestedForEachLoopLmlMacroParser(), "forEachNested", "nested",
				"nestedForEach", "eachNested");
		lmlParser.registerMacroParser(new LoopLmlMacroParser(), "loop", "while", "times");
		lmlParser.registerMacroParser(new ConditionLmlMacroParser(), "if", "test", "check", "try", "verify",
				"inspect", "validate", "onCondition", "condition", "conditional");
		lmlParser.registerMacroParser(new AssignLmlMacroParser(), "assign", "var", "val");
		// Add other default macros here.
	}

	public static void registerDefaultTagSyntax(final LmlParser lmlParser) {
		lmlParser.registerParser(new EmptyLmlTagDataParser(), "empty", "blank", "null", "placeholder");
		lmlParser.registerParser(new TableLmlTagDataParser<Table>(), "table", "div", "td", "th");
		lmlParser.registerParser(new RowLmlTagDataParser(), "row", "tr", "br");
		lmlParser.registerParser(new LabelLmlTagDataParser(), "label", "text", "li");
		lmlParser.registerParser(new ButtonLmlTagDataParser<Button>(), "button");
		lmlParser.registerParser(new TextButtonLmlTagDataParser<TextButton>(), "textButton", "a");
		lmlParser.registerParser(new CheckBoxLmlTagDataParser(), "checkBox", "check");
		lmlParser.registerParser(new ScrollPaneLmlTagDataParser(), "scrollPane", "scrollable", "scroll");
		lmlParser.registerParser(new SplitPaneLmlTagDataParser(), "splitPane", "splitable", "split");
		lmlParser.registerParser(new StackLmlTagDataParser(), "stack");
		lmlParser.registerParser(new ImageLmlTagDataParser(), "image", "icon", "img");
		lmlParser.registerParser(new HorizontalGroupLmlTagDataParser(), "horizontalGroup", "horizontal");
		lmlParser.registerParser(new VerticalGroupLmlTagDataParser(), "verticalGroup", "vertical");
		lmlParser.registerParser(new TextFieldLmlTagDataParser(), "textField", "input", "textInput");
		lmlParser.registerParser(new TextAreaLmlTagDataParser(), "textArea", "inputArea", "area");
		lmlParser.registerParser(new ProgressBarLmlTagDataParser(), "progressBar", "progress", "loadingBar",
				"loading");
		lmlParser.registerParser(new SliderLmlTagDataParser(), "slider");
		lmlParser.registerParser(new ListLmlTagDataParser(), "list", "ul");
		lmlParser.registerParser(new TreeLmlTagDataParser(), "tree", "root");
		lmlParser.registerParser(new WindowLmlTagDataParser<Window>(), "window");
		lmlParser.registerParser(new DialogLmlTagDataParser(), "dialog", "popup");
		// Add other default tags here.
	}
}
