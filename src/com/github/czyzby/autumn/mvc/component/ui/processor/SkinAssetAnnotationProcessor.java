package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.mvc.stereotype.SkinAsset;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

/** Injects assets from application's {@link com.badlogic.gdx.scenes.scene2d.ui.Skin} (when it is fully loaded) into
 * fields annotated with {@link com.github.czyzby.autumn.mvc.stereotype.SkinAsset}.
 *
 * @author MJ */
@MetaComponent
public class SkinAssetAnnotationProcessor extends ComponentFieldAnnotationProcessor {
    private final ObjectMap<String, Array<Pair<Field, Object>>> fieldsToInject = LazyObjectMap.newMapOfArrays();

    @Override
    public Class<? extends Annotation> getProcessedAnnotationClass() {
        return SkinAsset.class;
    }

    @Override
    public void processField(final ContextContainer context, final ContextComponent component, final Field field) {
        fieldsToInject.get(Reflection.getAnnotation(field, SkinAsset.class).value())
                .add(Pair.of(field, component.getComponent()));
    }

    /** @return array of fields paired with their owners, mapped by the given asset name. */
    public ObjectMap<String, Array<Pair<Field, Object>>> getFieldsToInject() {
        return fieldsToInject;
    }

    /** Clears all scanned injection data. */
    public void clearFieldsToInject() {
        fieldsToInject.clear();
    }
}