package com.github.czyzby.lml.uedi.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.uedi.LmlApplication;
import com.github.czyzby.lml.uedi.preferences.PreferenceProvider;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.impl.DefaultContext;
import com.github.czyzby.uedi.scanner.ClassScanner;
import com.github.czyzby.uedi.stereotype.Initiated;
import com.github.czyzby.uedi.stereotype.impl.StringProvider;

/** Extends LibGDX {@link com.github.czyzby.uedi.Context context} implementation with LML-specific utilities.
 *
 * @author MJ */
public class LmlContext extends DefaultContext {
    private final LmlApplication applicationListener;

    /** @param applicationListener constructed the context.
     * @param classScanner should be specific to the current platform. */
    public LmlContext(final LmlApplication applicationListener, final ClassScanner classScanner) {
        super(classScanner);
        this.applicationListener = applicationListener;
    }

    @Override
    protected boolean isInjectable(final Field field, final Object component) throws Exception {
        return super.isInjectable(field, component) && !field.isAnnotationPresent(LmlActor.class);
    }

    @Override
    protected StringProvider getPropertyProvider() {
        return new PreferenceProvider(ApplicationPreferences.getPreferences());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void processScannedComponent(final Object component, final Array<Initiated> componentsToInitiate) {
        super.processScannedComponent(component, componentsToInitiate);
        if (component instanceof View) {
            final View view = (View) component;
            applicationListener.addClassAlias(view.getViewId(),
                    (Class<? extends AbstractLmlView>) component.getClass());
            if (view.isFirst()) {
                applicationListener.forceCurrentView(view);
            }
        }
    }
}
