package com.github.czyzby.kiwi.util.gdx.collection;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.disposable.DisposableObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableObjectSet;

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

	/** @param set will contain all values that are present in every passed set (excluding this one).
	 * @return first argument set will all intersecting values. */
	public static <Type> ObjectSet<Type> intersectTo(final ObjectSet<Type> set, final ObjectSet<Type>... sets) {
		if (sets == null || sets.length == 0) {
			return set;
		}
		final ObjectSet<Type> allValues = union(sets);
		for (final Type value : allValues) {
			if (isPresentInEvery(value, sets)) {
				set.add(value);
			}
		}
		return set;
	}

	/** @return true if passed value is present in all of passed sets. */
	public static <Type> boolean isPresentInEvery(final Type value, final ObjectSet<Type>... sets) {
		for (final ObjectSet<Type> set : sets) {
			if (!set.contains(value)) {
				return false;
			}
		}
		return true;
	}

	/** @return true if passed value is present in any of passed sets. */
	public static <Type> boolean isPresentInAny(final Type value, final ObjectSet<Type>... sets) {
		for (final ObjectSet<Type> set : sets) {
			if (set.contains(value)) {
				return true;
			}
		}
		return false;
	}

	/** @return a new set with all values that are present in all of passed sets. */
	public static <Type> ObjectSet<Type> intersect(final ObjectSet<Type>... sets) {
		return intersectTo(new ObjectSet<Type>(), sets);
	}

	/** @param set will contain all values present in passed sets.
	 * @return first set argument will all values present in other passed sets. */
	public static <Type> ObjectSet<Type> unionTo(final ObjectSet<Type> set, final ObjectSet<Type>... sets) {
		if (sets == null || sets.length == 0) {
			return set;
		}
		for (final ObjectSet<Type> unionedSet : sets) {
			set.addAll(unionedSet);
		}
		return set;
	}

	/** @return a new set containing all values present in all of passed sets. */
	public static <Type> ObjectSet<Type> union(final ObjectSet<Type>... sets) {
		return unionTo(new ObjectSet<Type>(), sets);
	}
}
