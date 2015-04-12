package com.github.czyzby.autumn.context.provider;

import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Provides components that already exist in the context on {@link #provide()} calls.
 *
 * @author MJ */
public class ContextComponentProvider<Type> implements ObjectProvider<Type> {
	private final ContextContainer context;
	private final Class<Type> componentClass;

	/** @param context has to already contain the component of given class. */
	public ContextComponentProvider(final ContextContainer context, final Class<Type> componentClass) {
		this.context = context;
		this.componentClass = componentClass;
	}

	@Override
	public Type provide() {
		return context.getFromContext(componentClass);
	}
}