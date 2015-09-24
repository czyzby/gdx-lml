package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.dto.SkinData;
import com.github.czyzby.autumn.mvc.stereotype.preference.Skin;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Used to process annotated skin data.
 *
 * @author MJ */
@MetaComponent
public class SkinAnnotationProcessor extends ComponentFieldAnnotationProcessor {
    private final Lazy<SkinData> skinData = Lazy.empty();

    @Override
    public Class<? extends Annotation> getProcessedAnnotationClass() {
        return Skin.class;
    }

    @Override
    public void processField(final ContextContainer context, final ContextComponent component, final Field field) {
        validateCurrentSkinData();
        final Skin annotationData = Reflection.getAnnotation(field, Skin.class);
        validateFontsData(annotationData);
        try {
            skinData.set(new SkinData(Reflection.getFieldValue(field, component.getComponent()).toString(),
                    annotationData.fonts(), annotationData.fontNames()));
        } catch (final ReflectionException exception) {
            throw new AutumnRuntimeException("Unable to read skin data.", exception);
        }
    }

    private void validateCurrentSkinData() {
        if (skinData.isInitialized()) {
            throw new AutumnRuntimeException("Only one skin is supported. Multiple fields with @Skin found.");
        }
    }

    private static void validateFontsData(final Skin annotationData) {
        if (annotationData.fonts().length != annotationData.fontNames().length) {
            throw new AutumnRuntimeException("Fonts amount specified with @Skin should match font names amount.");
        }
    }

    /** @return data of the main application's skin.
     * @throws IllegalStateException if skin was not set with
     *             {@link com.github.czyzby.autumn.mvc.stereotype.preference.Skin} annotation. */
    public SkinData getSkinData() throws IllegalStateException {
        try {
            return skinData.get();
        } catch (final IllegalStateException exception) {
            throw new AutumnRuntimeException("Skin setting was not set. Annotate a configuration field with @Skin.",
                    exception);
        }
    }
}
