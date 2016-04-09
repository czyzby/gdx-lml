package com.github.czyzby.context.provider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.github.czyzby.autumn.annotation.Provider;
import com.github.czyzby.autumn.provider.DependencyProvider;
import com.github.czyzby.context.asset.MyAssetComponent;
import com.github.czyzby.kiwi.util.common.Strings;

/** When you annotate a class with {@link Provider} interface and it does NOT implement {@link DependencyProvider}
 * interface, all its methods will be registered as providers, each assigned to the class type that it returns. In this
 * case, {@link MyLabelProvider} provides {@link Label} instances with {@link #createLabel(MyAssetComponent)} method.
 *
 * <p>
 * As you can guess, such providers are using reflection. While this might be acceptable in case of simple providers,
 * commonly used ones should consider using solution without reflection. See {@link MyArrayProvider}.
 *
 * @author MJ */
@Provider
public class MyLabelProvider {
    /** This is the provider's method that will be invoked any time {@link Label} instance is requested by some
     * component.
     *
     * @param assets will be injected.
     * @return a new instance of {@link Label}. */
    public Label createLabel(final MyAssetComponent assets) {
        final LabelStyle style = new LabelStyle(assets.getFont(), Color.RED);
        final Label label = new Label(Strings.EMPTY_STRING, style);
        label.setWrap(true);
        label.setWidth(Gdx.graphics.getWidth());
        label.setY(Gdx.graphics.getHeight() / 2);
        return label;
    }
}
