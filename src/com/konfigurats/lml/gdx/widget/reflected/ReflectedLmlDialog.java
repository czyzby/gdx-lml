package com.konfigurats.lml.gdx.widget.reflected;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.konfigurats.lml.parser.impl.dto.ActorConsumer;

/** If this dialog receives an ActorConsumer in result method, it will invoke it; if a boolean is return by the
 * method and it's true, dialog hiding will be ignored.
 *
 * Made as GWT utility for LML. Anonymous class was, obviously, not available for GWT's reflection mechanism.
 *
 * @author MJ */
public class ReflectedLmlDialog extends Dialog {
	public ReflectedLmlDialog(final String title, final Skin skin, final String windowStyleName) {
		super(title, skin, windowStyleName);
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