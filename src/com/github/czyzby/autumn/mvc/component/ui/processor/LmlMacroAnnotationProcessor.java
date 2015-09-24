package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.LmlMacro;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.lml.parser.LmlParser;

/** Used to scan for paths with LML macro files.
 *
 * @author MJ */
@MetaComponent
public class LmlMacroAnnotationProcessor extends ComponentFieldAnnotationProcessor {
    @Inject(lazy = InterfaceService.class) private Lazy<InterfaceService> interfaceService;

    @Override
    public Class<? extends Annotation> getProcessedAnnotationClass() {
        return LmlMacro.class;
    }

    @Override
    public void processField(final ContextContainer context, final ContextComponent component, final Field field) {
        try {
            final Object macroData = Reflection.getFieldValue(field, component.getComponent());
            final LmlParser parser = interfaceService.get().getParser();
            final FileType fileType = Reflection.getAnnotation(field, LmlMacro.class).fileType();
            if (macroData instanceof String) {
                parser.parse(Gdx.files.getFileHandle((String) macroData, fileType));
            } else if (macroData instanceof String[]) {
                for (final String macroPath : (String[]) macroData) {
                    parser.parse(Gdx.files.getFileHandle(macroPath, fileType));
                }
            } else {
                throw new AutumnRuntimeException("Invalid type of LML macro definition in component: "
                        + component.getComponent() + ". String or String[] expected, received: " + macroData + ".");
            }
        } catch (final ReflectionException exception) {
            throw new AutumnRuntimeException("Unable to extract macro paths from field: " + field + " of component: "
                    + component.getComponent() + ".", exception);
        }
    }
}