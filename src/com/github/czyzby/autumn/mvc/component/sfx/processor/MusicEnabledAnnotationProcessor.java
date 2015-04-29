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
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicEnabled;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Allows to set initial music state and assign music preferences.
 *
 * @author MJ */
@MetaComponent
public class MusicEnabledAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = MusicService.class)
	private Lazy<MusicService> musicService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return MusicEnabled.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		try {
			if (field.getFieldType().equals(boolean.class)) {
				musicService.get().setMusicEnabled((Boolean) field.get(component.getComponent()));
			} else {
				final MusicEnabled musicData = field.getAnnotation(MusicEnabled.class);
				musicService.get().setMusicEnabledFromPreferences(musicData.preferences(),
						field.get(component.getComponent()).toString(), musicData.defaultSetting());
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to extract music state.", exception);
		}
	}
}