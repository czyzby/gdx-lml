package com.github.czyzby.autumn.mvc.component.ui.dto.injection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;

/** Common interface for actor injections.
 *
 * @author MJ */
public interface ActorFieldInjection {
	/** @param fieldOwner instance of the class that contains the field.
	 * @param actorsMappedById parsed actors, some (or one) of which will be injected. */
	void injectActors(Object fieldOwner, ObjectMap<String, Actor> actorsMappedById);
}
