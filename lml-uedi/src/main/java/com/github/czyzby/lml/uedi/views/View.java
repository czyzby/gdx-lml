package com.github.czyzby.lml.uedi.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.uedi.stereotype.Named;

/** Abstract base for LML views managed by UEDI. The views should also implement
 * {@link com.github.czyzby.uedi.stereotype.Singleton Singleton} interface if you want them to be found by the class
 * scanner.
 *
 * <p>
 * Note that this view's {@link Stage} will be cleared during LML template parsing. Instead, parsed actors will be added
 * using {@link #addActors(Array)} method and appended to a shared application's stage once the view is shown. You can
 * still inject a stage instance to one of the fields to get a reference to the global stage, but be aware that it will
 * be shared among all views.
 *
 * @author MJ */
public abstract class View extends AbstractLmlView implements Named {
    private final Array<Actor> actors = GdxArrays.newArray();

    /** Creates a new empty {@link View}. */
    public View() {
        super(null);
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal(getPath());
    }

    /** @return path to the LML template file. By default, will return "view/" + view ID + ".lml". */
    protected String getPath() {
        return "view/" + getViewId() + ".lml";
    }

    @Override
    public String getName() {
        return getViewId();
    }

    /** Override this method and return {@code true} if this view should be displayed first. Usually the first view
     * would inject the {@link com.badlogic.gdx.assets.AssetManager AssetManager} and update it until all assets are
     * loaded.
     *
     * @return true if the view should be displayed first. */
    public boolean isFirst() {
        return false;
    }

    /** @param actor will be added to the view's root actors. Might be cleared during locale change. */
    public void addActor(final Actor actor) {
        actors.add(actor);
    }

    /** @return direct reference to collection storing all current root actors of the view. */
    public Array<Actor> getActors() {
        return actors;
    }

    /** Removes all root actors managed by the view. */
    public void clearActors() {
        actors.clear();
    }

    /** @param actors will be added to the view's root actors. Might be cleared during locale change. */
    public void addActors(final Array<Actor> actors) {
        this.actors.addAll(actors);
    }

    /** Invoked after previous view is hidden and this view is about to show. Might be called when the view is being
     * reloaded. Clears previous stage actors and adds managed actor to stage. If overridden, call super. */
    @Override
    public void show() {
        final Stage stage = getStage();
        stage.getRoot().clearChildren();
        LmlUtilities.appendActorsToStage(stage, actors);
    }
}
