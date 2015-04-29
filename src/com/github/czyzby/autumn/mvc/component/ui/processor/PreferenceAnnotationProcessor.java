package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.Preference;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Used to scan for annotated preferences' data.
 *
 * @author MJ */
@MetaComponent
public class PreferenceAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Preference.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		try {
			final Preference preferenceData = field.getAnnotation(Preference.class);
			final String preferencesKey = preferenceData.value();
			final String preferencesPath = field.get(component.getComponent()).toString();
			interfaceService.get().addPreferencesToParser(preferencesKey, preferencesPath);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to read preference path from field: " + field
					+ " of component: " + component.getComponent() + ".", exception);
		}
	}
}
