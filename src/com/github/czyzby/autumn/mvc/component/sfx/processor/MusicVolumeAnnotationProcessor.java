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
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicVolume;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Allows to set initial music volume and assign music preferences.
 *
 * @author MJ */
@MetaComponent
public class MusicVolumeAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = MusicService.class)
	private Lazy<MusicService> musicService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return MusicVolume.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		try {
			if (field.getFieldType().equals(float.class)) {
				musicService.get().setMusicVolume((Float) field.get(component.getComponent()));
			} else {
				final MusicVolume volumeData = field.getAnnotation(MusicVolume.class);
				musicService.get().setMusicVolumeFromPreferences(volumeData.preferences(),
						field.get(component.getComponent()).toString(), volumeData.defaultVolume());
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to extract music volume.", exception);
		}
	}
}