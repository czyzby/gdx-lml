package com.github.czyzby.tests.service;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.uedi.stereotype.Factory;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;

/** Invoked each time a {@link LmlParser} instance is requested.
 *
 * @author MJ */
public class ParserFactory implements Factory {
    private I18NBundle nls; // i18n/nls.properties

    /** @return a new instance of {@link LmlParser}. */
    public LmlParser getParser() {
        VisUI.load(SkinScale.X2);
        return VisLml.parser().i18nBundle(nls).stylesPath("style/styles.lss").build();
    }
}
