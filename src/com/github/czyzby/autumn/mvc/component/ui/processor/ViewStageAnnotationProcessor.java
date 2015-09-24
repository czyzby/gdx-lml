package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.AbstractAnnotatedController;
import com.github.czyzby.autumn.mvc.stereotype.ViewStage;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Used to process fields that should have {@link com.badlogic.gdx.scenes.scene2d.Stage} injected.
 *
 * @author MJ */
@MetaComponent
public class ViewStageAnnotationProcessor extends ComponentFieldAnnotationProcessor {
    @Inject(lazy = InterfaceService.class) private Lazy<InterfaceService> interfaceService;

    @Override
    public Class<? extends Annotation> getProcessedAnnotationClass() {
        return ViewStage.class;
    }

    @Override
    public void processField(final ContextContainer context, final ContextComponent component, final Field field) {
        if (!Stage.class.equals(field.getType())) {
            throw new AutumnRuntimeException("Only Scene2D stages can be annotated with @ViewStage. Found type:"
                    + field.getType() + " in field: " + field + " of component: " + component.getComponent() + ".");
        }
        final Class<?> controllerClass = field.getDeclaringClass();
        if (!registerField(field, interfaceService.get().getController(controllerClass))) {
            // If view controller not found, trying out dialog controllers:
            if (!registerField(field, interfaceService.get().getDialogController(controllerClass))) {
                throw new AutumnRuntimeException("Unable to assign stage in field: " + field + " of component: "
                        + component.getComponent() + ".");
            }
        }
    }

    private static boolean registerField(final Field field, final Object controller) {
        if (controller instanceof AbstractAnnotatedController) {
            ((AbstractAnnotatedController) controller).registerStageField(field);
            return true;
        }
        return false;
    }
}