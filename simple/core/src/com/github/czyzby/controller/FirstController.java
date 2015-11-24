package com.github.czyzby.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.StandardViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlActor;
import com.kotcrab.vis.ui.widget.VisProgressBar;

/** This is the first application's view, shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(value = "templates/first.lml", first = true)
public class FirstController extends StandardViewRenderer {
    /** It's instance will be injected. {@link AssetService} is one of the default Autumn MVC components and - as you
     * might guess - it allows you to manage assets. By default, this service is used to load {@link Asset}-annotated
     * fields and views' themes you specify in {@link View} annotation. */
    @Inject AssetService assetService;
    /** This is an actor that we created in first.lml file. By adding "id=loadingBar" attribute, we assigned the actor
     * to a unique ID and we can use to it inject it after the parsing. Not every actor has to have ID or even be
     * injected; inject only these actors that you actually need a reference to. In this case, we want to update bar's
     * progress as we update the assets service. Thanks to an action we included in first.lml, the bar will
     * automatically switch screens when it reaches its max value. */
    @LmlActor("loadingBar") VisProgressBar loadingBar;

    @Override
    public void render(final Stage stage, final float delta) {
        assetService.update();
        loadingBar.setValue(assetService.getLoadingProgress());
        super.render(stage, delta);
    }
}
