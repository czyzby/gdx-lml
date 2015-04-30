package com.github.czyzby.autumn.mvc.component.i18n.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.i18n.LocaleService;
import com.github.czyzby.autumn.mvc.component.i18n.dto.LocaleChangingAction;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.AvailableLocales;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.lml.parser.LmlParser;

/** Used to scan for annotated preferences' data.
 *
 * @author MJ */
@MetaComponent
public class AvailableLocalesAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;
	@Inject
	private LocaleService localeService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return AvailableLocales.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		try {
			final Object locales = field.get(component.getComponent());
			if (locales instanceof String[]) {
				final String[] availableLocales = (String[]) locales;
				final LmlParser parser = interfaceService.get().getParser();
				final AvailableLocales localesData = field.getAnnotation(AvailableLocales.class);

				parser.addArgument(localesData.viewArgumentName(), availableLocales);
				for (final String locale : availableLocales) {
					parser.addAction(localesData.localeChangeMethodPrefix() + locale,
							new LocaleChangingAction(localeService, LocaleService.toLocale(locale)));
				}
			}
			throw new AutumnRuntimeException("Invalid field annotated with @AvailableLocales in component "
					+ component.getComponent() + ". Expected String[], received: " + locales + ".");
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to read available locales from field: " + field
					+ " of component: " + component.getComponent() + ".", exception);
		}
	}
}