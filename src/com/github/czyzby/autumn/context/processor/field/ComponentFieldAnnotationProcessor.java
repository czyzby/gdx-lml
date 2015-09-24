package com.github.czyzby.autumn.context.processor.field;

import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.AbstractComponentAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationType;

/** The base class for processors that handle field annotations.
 *
 * @author MJ */
public abstract class ComponentFieldAnnotationProcessor extends AbstractComponentAnnotationProcessor {
    @Override
    public ComponentAnnotationType getAnnotationType() {
        return ComponentAnnotationType.FIELD;
    }

    /** Field annotation processing method. For internal Autumn use.
     *
     * @param context contains the component.
     * @param component contains the annotated field.
     * @param field contains processed annotation type. */
    public abstract <Type> void processField(ContextContainer context, ContextComponent component, Field field);
}
