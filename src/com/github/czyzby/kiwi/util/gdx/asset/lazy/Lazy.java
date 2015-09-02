package com.github.czyzby.kiwi.util.gdx.asset.lazy;

import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Wraps around an object, allowing to have a final reference to a lazy-initialized object. Adds a very small
 * overhead, without the usual boilerplate that lazy objects require. Should be used for objects that are
 * expensive to create and rarely (or - at least - not always) needed to ensure that they are created only
 * when necessary. Concurrent use might result in multiple provider method calls.
 *
 * @author MJ */
public class Lazy<Type> {
	private ObjectProvider<? extends Type> provider;
	private Type object;

	/** Constructs an empty lazy object with no provider. Stored variable has to be set manually. */
	public Lazy() {
		this(null);
	}

	/** @param provider will provide wrapped object on first call. */
	public Lazy(final ObjectProvider<? extends Type> provider) {
		this.provider = provider;
	}

	/** @return a new instance of empty provider which can be managed manually. */
	public static <Type> Lazy<Type> empty() {
		return new Lazy<Type>();
	}

	/** @param provider will provide wrapped object on first call. */
	public static <Type> Lazy<Type> providedBy(final ObjectProvider<? extends Type> provider) {
		return new Lazy<Type>(provider);
	}

	/** @param provider will provide wrapped object on first call. Thread-safe. */
	public static <Type> ConcurrentLazy<Type> concurrentProvidedBy(
			final ObjectProvider<? extends Type> provider) {
		return new ConcurrentLazy<Type>(provider);
	}

	/** @param provider will provide wrapped disposable object on first call. */
	public static <Type extends Disposable> DisposableLazy<Type> disposableProvidedBy(
			final ObjectProvider<? extends Type> provider) {
		return new DisposableLazy<Type>(provider);
	}

	/** @param provider will provide wrapped disposable object on first call. Thread-safe. */
	public static <Type extends Disposable> ConcurrentDisposableLazy<Type> concurrentDisposableProvidedBy(
			final ObjectProvider<? extends Type> provider) {
		return new ConcurrentDisposableLazy<Type>(provider);
	}

	/** @return wrapped object instance. Is never null, as long as the provider is properly created. */
	public final Type get() {
		if (object == null) {
			object = getObjectInstance();
			provider = null;
		}
		return object;
	}

	/** Allows to manually set the object, not relying on a provider. Use with care - throws
	 * IllegalStateException if variable is already not null. Although discouraged, using of this method can
	 * be preferred if extra overhead of the provider is not an option.
	 *
	 * @param object will be set as wrapped object.
	 * @throws IllegalStateException if wrapped object is already present. */
	public void set(final Type object) throws IllegalStateException {
		if (this.object != null) {
			throw new IllegalStateException("Cannot set lazy variable - already initiated.");
		}
		this.object = object;
	}

	/** @return object instance provided by provider. Should be called once, upon object initiation. Cannot
	 *         return null. */
	protected Type getObjectInstance() {
		validateProvider();
		return provider.provide();
	}

	protected void validateProvider() {
		if (provider == null) {
			throw new IllegalStateException(
					"Variable was not set and there is no provider - unable to retrieve lazy variable.");
		}
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

	/** @return 0 if object was not initiated. Wrapped object's hash code if its present. */
	@Override
	public int hashCode() {
		if (object == null) {
			return 0;
		}
		return object.hashCode();
	}

	@Override
	public String toString() {
		return "Lazy [" + object + "]";
	}
}
