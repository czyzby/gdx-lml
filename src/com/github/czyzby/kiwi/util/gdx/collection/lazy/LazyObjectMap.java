package com.github.czyzby.kiwi.util.gdx.collection.lazy;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** An unordered map. This implementation is a cuckoo hash map using 3 hashes, random walking, and a small
 * stash for problematic keys. Null keys are not allowed. Null values are allowed. No allocation is done
 * except when growing the table size. <br> <br> This map performs very fast get, containsKey, and remove
 * (typically O(1), worst case O(log(n))). Put may be a bit slower, depending on hash collisions. Load factors
 * greater than 0.91 greatly increase the chances the map will have to rehash to the next higher POT size.
 * <br> <br> Thanks to the passed ObjectProvider, this map can initiate object on every get(key) call if the
 * key is no present in the map. This implementation is especially useful for maps of other collections (like
 * arrays), where you usually keep a lot of similarly created object instances without the need of varying
 * constructor parameters.
 *
 * @author Nathan Sweet
 * @author MJ */
public class LazyObjectMap<Key, Value> extends ObjectMap<Key, Value> {
	private ObjectProvider<? extends Value> provider;

	/** Creates a new map with an initial capacity of 32 and a load factor of 0.8. This map will hold 25 items
	 * before growing the backing table.
	 *
	 * @param provider creates new object on get(key) calls if the key is not present in the map. */
	public LazyObjectMap(final ObjectProvider<? extends Value> provider) {
		super();
		this.provider = provider;
	}

	/** Creates a new map with a load factor of 0.8. This map will hold initialCapacity * 0.8 items before
	 * growing the backing table.
	 *
	 * @param provider creates new object on get(key) calls if the key is not present in the map. */
	public LazyObjectMap(final ObjectProvider<? extends Value> provider, final int initialCapacity) {
		super(initialCapacity);
		this.provider = provider;
	}

	/** Creates a new map with the specified initial capacity and load factor. This map will hold
	 * initialCapacity * loadFactor items before growing the backing table.
	 *
	 * @param provider creates new object on get(key) calls if the key is not present in the map. */
	public LazyObjectMap(final ObjectProvider<? extends Value> provider, final int initialCapacity,
			final float loadFactor) {
		super(initialCapacity, loadFactor);
		this.provider = provider;
	}

	/** Creates a new map identical to the specified map.
	 *
	 * @param provider creates new object on get(key) calls if the key is not present in the map. */
	public LazyObjectMap(final ObjectProvider<? extends Value> provider,
			final ObjectMap<? extends Key, ? extends Value> map) {
		super(map);
		this.provider = provider;
	}

	/** Creates a new map identical to the specified map. */
	public LazyObjectMap(final LazyObjectMap<? extends Key, ? extends Value> map) {
		super(map);
		this.provider = map.provider;
	}

	/** @param provider creates new object on get(key) calls if the key is not present in the map.
	 * @return new lazy map instance. */
	public static <Key, Value> LazyObjectMap<Key, Value> newMap(final ObjectProvider<? extends Value> provider) {
		return new LazyObjectMap<Key, Value>(provider);
	}

	/** @param keysAndValues pairs of keys and values.
	 * @param provider creates new object on get(key) calls if the key is not present in the map.
	 * @return a new LazyObjectMap created with the passed keys and values.
	 * @throws IllegalArgumentException if keys and values total amount is not even.
	 * @throws ClassCastException if received unexpected object type. */
	@SuppressWarnings("unchecked")
	public static <Key, Value> LazyObjectMap<Key, Value> of(final ObjectProvider<? extends Value> provider,
			final Object... keysAndValues) {
		if (keysAndValues.length % 2 != 0) {
			throw new IllegalArgumentException("Keys and values have to be passed in pairs.");
		}
		final LazyObjectMap<Key, Value> map = new LazyObjectMap<Key, Value>(provider);
		for (int index = 0; index < keysAndValues.length; index += 2) {
			map.put((Key) keysAndValues[index], (Value) keysAndValues[index + 1]);
		}
		return map;
	}

	/** @param provider creates new object on get(key) calls if the key is not present in the map.
	 * @return a new LazyObjectMap created with the keys and values stored in passed map. */
	public static <Key, Value> LazyObjectMap<Key, Value> copyOf(
			final ObjectMap<? extends Key, ? extends Value> objectMap,
			final ObjectProvider<? extends Value> provider) {
		return new LazyObjectMap<Key, Value>(provider, objectMap);
	}

	/** @return a new LazyObjectMap created with the keys and values stored in passed map. */
	public static <Key, Value> LazyObjectMap<Key, Value> copyOf(
			final LazyObjectMap<? extends Key, ? extends Value> lazyMap) {
		return new LazyObjectMap<Key, Value>(lazyMap);
	}

	@Override
	public Value get(final Key key) {
		Value value = super.get(key);
		if (value == null) {
			value = provider.provide();
			put(key, value);
		}
		return value;
	}

	/** LazyObjectMap initiates values on access. No need for default values, as nulls are impossible as long
	 * as the provided is correctly implemented and nulls are not put into the map. */
	@Override
	@Deprecated
	public Value get(final Key key, final Value defaultValue) {
		return super.get(key, defaultValue);
	}

	/** @return provider that produces new instances of objects on get(key) calls with unknown keys. */
	public ObjectProvider<? extends Value> getProvider() {
		return provider;
	}

	/** @param provider produces new instances of objects on get(key) calls with unknown keys. */
	public void setProvider(final ObjectProvider<? extends Value> provider) {
		this.provider = provider;
	}
}
