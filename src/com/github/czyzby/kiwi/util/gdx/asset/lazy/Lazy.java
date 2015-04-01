package com.github.czyzby.kiwi.util.gdx.asset.lazy;

import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Wraps around an object, allowing to have a final reference to a lazy-initialized object. Adds a very small
 * overhead, without the usual boilerplate that lazy objects require. Should be used for objects that are
 * expensive to create and rarely (or - at least - not always) needed to ensure that they are created only
 * when necessary. Concurrent use might result in multiple provider method calls.
 *
 * @author MJ */
public class Lazy<Type> {
	private final ObjectProvider<? extends Type> provider;
	private Type object;

	/** @param provider will provide wrapped object on first call. */
	public Lazy(final ObjectProvider<? extends Type> provider) {
		this.provider = provider;
	}

	/** @param provider will provide wrapped object on first call. */
	public static <Type> Lazy<Type> providedBy(final ObjectProvider<? extends Type> provider) {
		return new Lazy<Type>(provider);
	}

	/** @return wrapped object instance. Is never null, as long as the provider is properly created. */
	public Type get() {
		if (object == null) {
			object = getObjectInstance();
		}
		return object;
	}

	/** @return object instance provided by provider. Should be called once, upon object initiation. Cannot
	 *         return null. */
	protected Type getObjectInstance() {
		return provider.provide();
	}

	/** @return direct reference to lazy provider. */
	protected ObjectProvider<? extends Type> getProvider() {
		return provider;
	}

	/** @return direct reference to lazy object. */
	protected Type getObject() {
		return object;
	}

	/** @return true if object is not null. */
	public boolean isInitialized() {
		return object != null;
	}

	@Override
	public boolean equals(final Object object) {
		return object instanceof Lazy<?> && Nullables.areEqual(this.object, ((Lazy<?>) object).object);
	}

	@Override
	public String toString() {
		return "Lazy [" + object + "]";
	}
}
