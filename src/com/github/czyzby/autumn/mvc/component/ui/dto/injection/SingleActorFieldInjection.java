package com.github.czyzby.autumn.mvc.component.ui.dto.injection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Injects a single actor. Should be used when ViewActor annotation contains only one actor ID.
 *
 * @author MJ */
public class SingleActorFieldInjection implements ActorFieldInjection {
	private final Field field;
	private final String actorId;

	public SingleActorFieldInjection(final Field field, final String actorId) {
		this.field = field;
		this.actorId = actorId;
	}

	@Override
	public void injectActors(final Object fieldOwner, final ObjectMap<String, Actor> actorsMappedById) {
		final Actor actor = actorsMappedById.get(actorId);
		if (actor != null) {
			try {
				Reflection.setFieldValue(field, fieldOwner, actor);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject actor: " + actor + " mapped by ID: "
						+ actorId + " into controller: " + fieldOwner + ".", exception);
			}
		}
	}
}
