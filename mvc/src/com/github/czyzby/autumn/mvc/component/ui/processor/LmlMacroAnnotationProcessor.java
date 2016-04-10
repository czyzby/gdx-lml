package com.github.czyzby.autumn.mvc.component.ui.processor;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.context.Context;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.LmlMacro;
import com.github.czyzby.autumn.processor.AbstractAnnotationProcessor;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.lml.parser.LmlParser;

/** Used to scan for paths with LML macro files.
 *
 * @author MJ */
public class LmlMacroAnnotationProcessor extends AbstractAnnotationProcessor<LmlMacro> {
    @Inject(lazy = InterfaceService.class) private Lazy<InterfaceService> interfaceService;

    @Override
    public Class<LmlMacro> getSupportedAnnotationType() {
        return LmlMacro.class;
    }

    @Override
    public boolean isSupportingFields() {
        return true;
    }

    @Override
    public void processField(final Field field, final LmlMacro annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        try {
            final Object macroData = Reflection.getFieldValue(field, component);
            final LmlParser parser = interfaceService.get().getParser();
            final FileType fileType = annotation.fileType();
            if (macroData instanceof String) {
                parser.parseTemplate(Gdx.files.getFileHandle((String) macroData, fileType));
            } else if (macroData instanceof String[]) {
                for (final String macroPath : (String[]) macroData) {
                    parser.parseTemplate(Gdx.files.getFileHandle(macroPath, fileType));
                }
            } else {
                throw new GdxRuntimeException("Invalid type of LML macro definition in component: " + component
                        + ". String or String[] expected, received: " + macroData + ".");
            }
        } catch (final ReflectionException exception) {
            throw new GdxRuntimeException(
                    "Unable to extract macro paths from field: " + field + " of component: " + component + ".",
                    exception);
        }
    }
}