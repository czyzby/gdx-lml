package com.github.czyzby.context.processor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.Processor;
import com.github.czyzby.autumn.context.Context;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.autumn.processor.AbstractAnnotationProcessor;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** This is an advanced Autumn example. {@link MyCustomProcessor} represents an annotation processor which handles
 * {@link MyCustomAnnotation} occurrences. This API allows to register new, custom annotations and highly extend Autumn
 * functionalities.
 *
 * @author MJ */
@Processor
public class MyCustomProcessor extends AbstractAnnotationProcessor<MyCustomAnnotation> {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyCustomProcessor.class);

    /** This array stores instances of all found classes annotated with MyCustomAnnotation. */
    private final Array<Object> annotatedComponents = GdxArrays.newArray();

    @Override
    public Class<MyCustomAnnotation> getSupportedAnnotationType() {
        return MyCustomAnnotation.class;
    }

    @Override
    public void doBeforeScanning(final ContextInitializer initializer) {
        LOGGER.info("Initiating MyCustomProcessor...");
        // Telling the ContextInitializer to look for classes annotated with MyCustomAnnotation:
        initializer.scanFor(MyCustomAnnotation.class);
    }

    // Our annotation supports fields, methods and classes. If you want to support less targets, just override less
    // methods - they default to false.
    @Override
    public boolean isSupportingFields() {
        return true;
    }

    @Override
    public boolean isSupportingMethods() {
        return true;
    }

    @Override
    public boolean isSupportingTypes() {
        return true;
    }

    // Actual annotation processing methods:

    @Override
    public void processField(final Field field, final MyCustomAnnotation annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        LOGGER.info("{0} has field '{1}' annotated with @MyCustomAnnotation. Injecting '{2}'.", component,
                field.getName(), annotation.value());
        // Setting field to value from annotation:
        try {
            field.setAccessible(true);
            field.set(component, annotation.value());
        } catch (final ReflectionException exception) {
            LOGGER.error(exception, "Unable to set field value.");
        }
    }

    @Override
    public void processMethod(final Method method, final MyCustomAnnotation annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        // Invoking annotated method with ID from annotation as parameter:
        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(component, annotation.id());
        } catch (final ReflectionException exception) {
            LOGGER.error(exception, "Unable to invoke method.");
        }
        LOGGER.info("{0} had method '{1}' annotated with @MyCustomAnnotation. Method invocation result: {2}.",
                component, method.getName(), result);
    }

    @Override
    public void processType(final Class<?> type, final MyCustomAnnotation annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        LOGGER.info("Found {0} annotated with @MyCustomAnnotation.", component);
        // Storing scanned class instance:
        annotatedComponents.add(component);
    }

    @Override
    public void doAfterScanning(final ContextInitializer initializer, final Context context,
            final ContextDestroyer destroyer) {
        // By storing instances of classes with a particular annotation, you can perform some operations en masse in
        // this method. This is why we maintain a list of components: annotatedComponents. See processType method!

        // Printing instances of all annotated classes:
        LOGGER.info("Scanning is finished. Instances of classes annotated with @MyCustomAnnotation: {0}.",
                annotatedComponents);

        // Adding an additional action invoked upon context disposing:
        destroyer.addAction(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Disposing...");
                // This is just a proof of concept - you can add additional disposing actions invoked during context
                // destruction. Performing disposing with a single method might be more efficient that registering a
                // separate action for each component during annotation scanning. This is how some of the official
                // processors work: see DisposeAnnotationProcessor, for example.
            }
        });
    }
}
