package com.github.czyzby.autumn.context.processor;

import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.meta.ComponentMetaAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.method.ComponentMethodAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.type.ComponentTypeAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;

/** Contains all types of annotations currently processed by Autumn.
 *
 * @author MJ */
public enum ComponentAnnotationType {
    /** Annotations on components' classes; contrary to TYPE, these components process annotations. */
    META {
        @Override
        public void registerProcessor(final ContextContainer context, final ComponentAnnotationProcessor processor) {
            if (processor instanceof ComponentMetaAnnotationProcessor) {
                context.registerMetaAnnotationProcessor((ComponentMetaAnnotationProcessor) processor);
            } else {
                throw new AutumnRuntimeException("Unable to register meta processor: " + processor
                        + ". Meta processors have to extend ComponentMetaAnnotationProcessor class.");
            }
        }
    },
    /** Annotations on components' classes. Regular context components. */
    TYPE {
        @Override
        public void registerProcessor(final ContextContainer context, final ComponentAnnotationProcessor processor) {
            if (processor instanceof ComponentTypeAnnotationProcessor) {
                context.registerTypeAnnotationProcessor((ComponentTypeAnnotationProcessor) processor);
            } else {
                throw new AutumnRuntimeException("Unable to register type processor: " + processor
                        + ". Type processors have to extend ComponentTypeAnnotationProcessor class.");
            }
        }
    },
    /** Annotations on components' variables. */
    FIELD {
        @Override
        public void registerProcessor(final ContextContainer context, final ComponentAnnotationProcessor processor) {
            if (processor instanceof ComponentFieldAnnotationProcessor) {
                context.registerFieldAnnotationProcessor((ComponentFieldAnnotationProcessor) processor);
            } else {
                throw new AutumnRuntimeException("Unable to register field processor: " + processor
                        + ". Field processors have to extend ComponentFieldAnnotationProcessor class.");
            }
        }
    },
    /** Annotations on components' methods. */
    METHOD {
        @Override
        public void registerProcessor(final ContextContainer context, final ComponentAnnotationProcessor processor) {
            if (processor instanceof ComponentMethodAnnotationProcessor) {
                context.registerMethodAnnotationProcessor((ComponentMethodAnnotationProcessor) processor);
            } else {
                throw new AutumnRuntimeException("Unable to register method processor: " + processor
                        + ". Method processors have to extend ComponentMethodAnnotationProcessor class.");
            }
        }
    };

    /** @param context application's context.
     * @param processor will be registered in the correct processor category. */
    public abstract void registerProcessor(ContextContainer context, ComponentAnnotationProcessor processor);
}
