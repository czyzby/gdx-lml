package com.github.czyzby.kiwi.util.gdx.asset.lazy.provider;

import com.badlogic.gdx.utils.ObjectMap;

/** Utility implementation of ObjectProvider that produces object maps. Does not rely on reflection. Note that
 * the object is stateless and immutable, so one instance per application can be used.
 *
 * @author MJ */
public class MapObjectProvider<Key, Value> implements ObjectProvider<ObjectMap<Key, Value>> {
	@Override
	public ObjectMap<Key, Value> provide() {
		return new ObjectMap<Key, Value>();
	}

	/** Produces object sets. */
	public static <Key, Value> MapObjectProvider<Key, Value> getProvider() {
		return new MapObjectProvider<Key, Value>();
	}

}