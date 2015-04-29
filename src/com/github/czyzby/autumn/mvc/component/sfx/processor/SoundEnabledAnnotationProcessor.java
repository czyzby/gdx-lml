package com.github.czyzby.autumn.mvc.component.sfx.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.SoundEnabled;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Allows to set initial music state and assign music preferences.
 *
 * @author MJ */
@MetaComponent
public class SoundEnabledAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = MusicService.class)
	private Lazy<MusicService> musicService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return SoundEnabled.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		try {
			if (field.getFieldType().equals(boolean.class)) {
				musicService.get().setSoundEnabled((Boolean) field.get(component.getComponent()));
			} else {
				final SoundEnabled soundData = field.getAnnotation(SoundEnabled.class);
				musicService.get().setSoundEnabledFromPreferences(soundData.preferences(),
						field.get(component.getComponent()).toString(), soundData.defaultSetting());
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to extract sound state.", exception);
		}
	}
}