package com.github.czyzby.autumn.mvc.component.i18n.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nBundle;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Used to scan for annotated i18n bundles data.
 *
 * @author MJ */
@MetaComponent
public class I18nBundleAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	private final ObjectMap<String, FileHandle> bundleFiles = GdxMaps.newObjectMap();

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return I18nBundle.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final Field field) {
		try {
			final I18nBundle bundleData = Reflection.getAnnotation(field, I18nBundle.class);
			final Object bundleField = Reflection.getFieldValue(field, component.getComponent());
			bundleFiles.put(bundleData.value(), extractBundleFile(bundleData, bundleField));
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to extract i18n bundle path.", exception);
		}
	}

	private FileHandle extractBundleFile(final I18nBundle bundleData, final Object bundleField) {
		if (bundleField instanceof FileHandle) {
			return (FileHandle) bundleField;
		} else {
			return Gdx.files.getFileHandle(bundleField.toString(), bundleData.fileType());
		}
	}

	/** @return all registered i18n bundle file handles that were already scanned. */
	public ObjectMap<String, FileHandle> getBundleFiles() {
		return bundleFiles;
	}
}