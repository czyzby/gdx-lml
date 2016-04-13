package com.github.czyzby.views.actions;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.Core;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.util.LmlUtilities;

public class Global implements ActionContainer {
    private final Core core = (Core) Gdx.app.getApplicationListener();

    // Uncomment this method and try using onclick="global.roll" in first.lml.
    // void roll(final Actor actor) {
    // Gdx.app.log(Lml.LOGGER_TAG, actor.toString());
    // }

    @LmlAction("setLocale")
    public void setLocale(final Actor actor) {
        final String localeId = LmlUtilities.getActorId(actor);
        final I18NBundle currentBundle = core.getParser().getData().getDefaultI18nBundle();
        if (currentBundle.getLocale().getLanguage().equalsIgnoreCase(localeId)) {
            // Same language.
            return;
        }
        core.getParser().getData()
                .setDefaultI18nBundle(I18NBundle.createBundle(Gdx.files.internal("i18n/bundle"), new Locale(localeId)));
        core.reloadViews();
    }
}
