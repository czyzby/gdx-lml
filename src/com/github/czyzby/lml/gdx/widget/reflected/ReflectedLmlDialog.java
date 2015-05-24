package com.github.czyzby.lml.gdx.widget.reflected;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;

/** If this dialog receives an ActorConsumer in result method, it will invoke it; if a boolean (primitive or
 * wrapped) is returned by the method and it's true, dialog hiding will be cancelled.
 *
 * Made as GWT utility for LML. Anonymous class was, obviously, not available for GWT's reflection mechanism.
 *
 * @author MJ */
public class ReflectedLmlDialog extends Dialog {
	public ReflectedLmlDialog(final String title, final Skin skin, final String windowStyleName) {
		super(title, skin, windowStyleName);
		row();
	}

	@Override
	protected void result(final Object object) {
		if (object instanceof ActorConsumer<?, ?>) {
			@SuppressWarnings("unchecked")
			final Object result = ((ActorConsumer<?, Object>) object).consume(this);
			if (result instanceof Boolean && ((Boolean) result).booleanValue()) {
				cancel();
			}
		}
	}
}