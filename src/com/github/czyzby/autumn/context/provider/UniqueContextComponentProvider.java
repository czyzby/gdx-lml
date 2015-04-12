package com.github.czyzby.autumn.context.provider;

import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Rather than extracting existing instances of components from the context, this provider will create a new
 * component instance (initiated with the context) on each {@link #provide()} call.
 *
 * @author MJ */
public class UniqueContextComponentProvider<Type> implements ObjectProvider<Type> {
	private final ContextContainer context;
	private final Class<Type> componentClass;

	/** @param context can to already contain the component of given class; it will not be extracted.
	 * @param componentClass new instance of this class (initiated with context) is provided on provide calls. */
	public UniqueContextComponentProvider(final ContextContainer context, final Class<Type> componentClass) {
		this.context = context;
		this.componentClass = componentClass;
	}

	@Override
	public Type provide() {
		return context.createWithContext(componentClass);
	}
}
