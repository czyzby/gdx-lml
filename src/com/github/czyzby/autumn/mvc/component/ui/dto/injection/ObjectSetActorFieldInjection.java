package com.github.czyzby.autumn.mvc.component.ui.dto.injection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Used when the ViewActor-annotated field is an {@link com.badlogic.gdx.utils.ObjectSet}.
 *
 * @author MJ */
public class ObjectSetActorFieldInjection implements ActorFieldInjection {
    private final Field field;
    private final String[] actorIds;

    public ObjectSetActorFieldInjection(final Field field, final String[] actorIds) {
        this.field = field;
        this.actorIds = actorIds;
    }

    @Override
    public void injectActors(final Object fieldOwner, final ObjectMap<String, Actor> actorsMappedById) {
        try {
            @SuppressWarnings("unchecked") ObjectSet<Actor> actorArray = Reflection.getFieldValue(field, fieldOwner,
                    ObjectSet.class);
            if (actorArray == null) {
                actorArray = GdxSets.newSet();
            } else {
                actorArray.clear();
            }
            for (final String actorId : actorIds) {
                if (actorsMappedById.containsKey(actorId)) {
                    actorArray.add(actorsMappedById.get(actorId));
                }
            }
            Reflection.setFieldValue(field, fieldOwner, actorArray);
        } catch (final ReflectionException exception) {
            throw new AutumnRuntimeException(
                    "Unable to inject actors into field:" + field + " of controller: " + fieldOwner + ".", exception);
        }
    }
}
