package com.github.czyzby.lml.scene2d.ui.reflected;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** This is a LML internal utility actor container. It extends {@link Actor} class, but not
 * {@link com.badlogic.gdx.scenes.scene2d.Group Group}. While it contains multiple actors, it does not display them in
 * any way, nor is it cleared when the actors are added to a {@link com.badlogic.gdx.scenes.scene2d.Group Group}.
 *
 * @author MJ */
public class ActorStorage extends Actor {
    private final Array<Actor> actors = GdxArrays.newArray();

    /** @param actor will be stored. */
    public void addActor(final Actor actor) {
        actors.add(actor);
    }

    /** @return direct reference to the array storing managed actors. */
    public Array<Actor> getActors() {
        return actors;
    }
}
