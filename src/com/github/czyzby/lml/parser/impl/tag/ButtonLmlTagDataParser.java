package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.TableLmlParent;

/** Additionally to Table tags, button supports: <ul> <li>isChecked: if true, button will be set to checked
 * status.</li> <li>isDisables: if true, button will be disabled.</li> </ul>
 *
 * @author MJ */
public class ButtonLmlTagDataParser<ButtonWidget extends Button> extends TableLmlTagDataParser<ButtonWidget> {
	public static final String IS_CHECKED_ATTRIBUTE = "ISCHECKED";
	public static final String IS_DISABLED_ATTRIBUTE = "ISDISABLED";

	@Override
	protected ButtonWidget parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final ButtonWidget button = super.parseChildWithValidTag(lmlTagData, parser);
		button.setChecked(parseBoolean(lmlTagData, IS_CHECKED_ATTRIBUTE, parser, button));
		button.setDisabled(parseBoolean(lmlTagData, IS_DISABLED_ATTRIBUTE, parser, button));
		return button;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ButtonWidget prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		return (ButtonWidget) new Button(parser.getSkin(), getStyleName(lmlTagData, parser));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected LmlParent<ButtonWidget> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return (LmlParent<ButtonWidget>) new TableLmlParent<Button>(lmlTagData, parseChildWithValidTag(
				lmlTagData, parser), parent, parser);
	}

}
