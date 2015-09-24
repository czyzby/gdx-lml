package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.StageViewport;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Used to scan for viewport provider.
 *
 * @author MJ */
@MetaComponent
public class StageViewportAnnotationProcessor extends ComponentFieldAnnotationProcessor {
    @Inject(lazy = InterfaceService.class) private Lazy<InterfaceService> interfaceService;

    @Override
    public Class<? extends Annotation> getProcessedAnnotationClass() {
        return StageViewport.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processField(final ContextContainer context, final ContextComponent component, final Field field) {
        try {
            final Object bundleField = Reflection.getFieldValue(field, component.getComponent());
            if (bundleField instanceof ObjectProvider<?>) {
                interfaceService.get().setViewportProvider((ObjectProvider<Viewport>) bundleField);
                return;
            }
            throw new AutumnRuntimeException("Invalid viewport provider: has to implement ObjectProvider<Viewport>.");
        } catch (final ReflectionException exception) {
            throw new AutumnRuntimeException("Unable to extract viewport provider.", exception);
        }
    }
}