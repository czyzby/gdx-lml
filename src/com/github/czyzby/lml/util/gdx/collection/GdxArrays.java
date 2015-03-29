package com.github.czyzby.lml.util.gdx.collection;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.lml.util.gdx.collection.disposable.DisposableArray;
import com.github.czyzby.lml.util.gdx.collection.immutable.ImmutableArray;

/** Simple Array utilities, somewhat inspired by Guava.
 *
 * @author MJ */
public class GdxArrays {
	private GdxArrays() {
	}

	/** @return a new, empty Array. */
	public static <Type> Array<Type> newArray() {
		return new Array<Type>();
	}

	/** @return a new, empty typed Array. */
	public static <Type> Array<Type> newArray(final Class<Type> forClass) {
		return new Array<Type>(forClass);
	}

	/** @param values will be appended to the array.
	 * @return a new Array with the passed values. */
	public static <Type> Array<Type> newArray(final Type... values) {
		final Array<Type> array = new Array<Type>();
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @param values will be appended to the array.
	 * @return a new Array with the passed values. */
	public static <Type> Array<Type> newArray(final Iterable<? extends Type> values) {
		final Array<Type> array = new Array<Type>();
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @return a new disposable array made using passed array values. */
	public static <Type extends Disposable> DisposableArray<Type> toDisposable(
			final Array<? extends Type> array) {
		return new DisposableArray<Type>(array);
	}

	/** @return a new semi-immutable array made using passed array values. */
	public static <Type> ImmutableArray<Type> toImmutable(final Array<? extends Type> array) {
		return new ImmutableArray<Type>(array);
	}

	/** @return true if array is null or has no elements. */
	public static boolean isEmpty(final Array<?> array) {
		return array == null || array.size == 0;
	}

	/** @return true if array is not null and has at least one element. */
	public static boolean isNotEmpty(final Array<?> array) {
		return array != null && array.size > 0;
	}

	/** @return the biggest size among the passed arrays. */
	public static int getBiggestSize(final Array<?>... arrays) {
		int maxSize = 0;
		for (final Array<?> array : arrays) {
			maxSize = Math.max(maxSize, array.size);
		}
		return maxSize;
	}

	/** @return the biggest size among the passed arrays. */
	public static <Type> int getBiggestSize(final Iterable<Array<? extends Type>> arrays) {
		int maxSize = 0;
		for (final Array<?> array : arrays) {
			maxSize = Math.max(maxSize, array.size);
		}
		return maxSize;
	}

	/** @return true if the given index is last in the passed array. */
	public static boolean isIndexLast(final Array<?> array, final int index) {
		return array.size - 1 == index;
	}

	/** @param array can be null.
	 * @return null (if array is empty or last stored value was null) or last stored value. */
	public static <Type> Type removeLast(final Array<? extends Type> array) {
		if (isEmpty(array)) {
			return null;
		}
		return array.removeIndex(array.size - 1);
	}
}
