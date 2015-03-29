package com.github.czyzby.lml.util.gdx.collection;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.lml.util.gdx.collection.disposable.DisposableObjectSet;
import com.github.czyzby.lml.util.gdx.collection.immutable.ImmutableObjectSet;

/** Simple ObjectSet utilities, somewhat inspired by Guava.
 *
 * @author MJ */
public class GdxSets {
	private GdxSets() {
	}

	/** @return a new, empty ObjectSet. */
	public static <Type> ObjectSet<Type> newSet() {
		return new ObjectSet<Type>();
	}

	/** @return a new ObjectSet with the passed values. */
	public static <Type> ObjectSet<Type> newSet(final Type... values) {
		final ObjectSet<Type> set = new ObjectSet<Type>();
		for (final Type value : values) {
			set.add(value);
		}
		return set;
	}

	/** @return a new ObjectSet with the passed values. */
	public static <Type> ObjectSet<Type> newSet(final Iterable<? extends Type> values) {
		final ObjectSet<Type> set = new ObjectSet<Type>();
		for (final Type value : values) {
			set.add(value);
		}
		return set;
	}

	/** @return a new disposable set with the passed values. */
	public static <Type extends Disposable> DisposableObjectSet<Type> toDisposable(
			final ObjectSet<? extends Type> set) {
		return new DisposableObjectSet<Type>(set);
	}

	/** @return a new semi-immutable set with the passed values. */
	public static <Type> ImmutableObjectSet<Type> toImmutable(final ObjectSet<? extends Type> set) {
		return new ImmutableObjectSet<Type>(set);
	}

	/** @return true if array is null or has no elements. */
	public static boolean isEmpty(final ObjectSet<?> set) {
		return set == null || set.size == 0;
	}

	/** @return true if array is not null and has at least one element. */
	public static boolean isNotEmpty(final ObjectSet<?> set) {
		return set != null && set.size > 0;
	}

	/** @return the biggest size among the passed sets. */
	public static int getBiggestSize(final ObjectSet<?>... sets) {
		int maxSize = 0;
		for (final ObjectSet<?> set : sets) {
			maxSize = Math.max(maxSize, set.size);
		}
		return maxSize;
	}

	/** @return the biggest size among the passed arrays. */
	public static <Type> int getBiggestSize(final Iterable<ObjectSet<? extends Type>> sets) {
		int maxSize = 0;
		for (final ObjectSet<?> set : sets) {
			maxSize = Math.max(maxSize, set.size);
		}
		return maxSize;
	}
}
