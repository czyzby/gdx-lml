package com.github.czyzby.kiwi.util.gdx.collection;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.SortedIntList;
import com.github.czyzby.kiwi.util.gdx.collection.disposable.DisposableArray;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;

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

	/** @return a new, empty SnapshotArray. */
	public static <Type> SnapshotArray<Type> newSnapshotArray() {
		return new SnapshotArray<Type>();
	}

	/** @return a new, empty typed SnapshotArray. */
	public static <Type> SnapshotArray<Type> newSnapshotArray(final Class<Type> forClass) {
		return new SnapshotArray<Type>(forClass);
	}

	/** @return a new, empty DelayedRemovalArray. */
	public static <Type> DelayedRemovalArray<Type> newDelayedRemovalArray() {
		return new DelayedRemovalArray<Type>();
	}

	/** @return a new, empty typed DelayedRemovalArray. */
	public static <Type> DelayedRemovalArray<Type> newDelayedRemovalArray(final Class<Type> forClass) {
		return new DelayedRemovalArray<Type>(forClass);
	}

	/** @param values will be appended to the array.
	 * @return a new Array with the passed values. */
	public static <Type> Array<Type> newArray(final Type... values) {
		return new Array<Type>(values);
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

	/** @param values will be appended to the array.
	 * @return a new typed Array with the passed values. */
	public static <Type> Array<Type> newArray(final Class<Type> forClass,
			final Iterable<? extends Type> values) {
		final Array<Type> array = new Array<Type>(forClass);
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @param values will be appended to the array.
	 * @return a new SnapshotArray with the passed values. */
	public static <Type> SnapshotArray<Type> newSnapshotArray(final Type... values) {
		return new SnapshotArray<Type>(values);
	}

	/** @param values will be appended to the array.
	 * @return a new SnapshotArray with the passed values. */
	public static <Type> SnapshotArray<Type> newSnapshotArray(final Iterable<? extends Type> values) {
		final SnapshotArray<Type> array = new SnapshotArray<Type>();
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @param values will be appended to the array.
	 * @return a new typed SnapshotArray with the passed values. */
	public static <Type> SnapshotArray<Type> newSnapshotArray(final Class<Type> forClass,
			final Iterable<? extends Type> values) {
		final SnapshotArray<Type> array = new SnapshotArray<Type>(forClass);
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @param values will be appended to the array.
	 * @return a new DelayedRemovalArray with the passed values. */
	public static <Type> DelayedRemovalArray<Type> newDelayedRemovalArray(final Type... values) {
		return new DelayedRemovalArray<Type>(values);
	}

	/** @param values will be appended to the array.
	 * @return a new DelayedRemovalArray with the passed values. */
	public static <Type> DelayedRemovalArray<Type> newDelayedRemovalArray(
			final Iterable<? extends Type> values) {
		final DelayedRemovalArray<Type> array = new DelayedRemovalArray<Type>();
		for (final Type value : values) {
			array.add(value);
		}
		return array;
	}

	/** @param values will be appended to the array.
	 * @return a new typed DelayedRemovalArray with the passed values. */
	public static <Type> DelayedRemovalArray<Type> newDelayedRemovalArray(final Class<Type> forClass,
			final Iterable<? extends Type> values) {
		final DelayedRemovalArray<Type> array = new DelayedRemovalArray<Type>(forClass);
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

	/** @return a new snapshot array created with the passed array values. */
	public static <Type> SnapshotArray<Type> toSnapshot(final Array<? extends Type> array) {
		return new SnapshotArray<Type>(array);
	}

	/** @return a new delayed removal array created with the passed array values. */
	public static <Type> DelayedRemovalArray<Type> toDelayedRemoval(final Array<? extends Type> array) {
		return new DelayedRemovalArray<Type>(array);
	}

	/** @return a new pooled linked list the the passes iterable's values. */
	public static <Type> PooledLinkedList<Type> toPooledLinkedList(final int maxPoolSize,
			final Iterable<? extends Type> iterable) {
		final PooledLinkedList<Type> list = new PooledLinkedList<Type>(maxPoolSize);
		for (final Type value : iterable) {
			list.add(value);
		}
		return list;
	}

	/** @return a new sorted int list with iterable's values inserted with ascending indexes, starting with 0. */
	public static <Type> SortedIntList<Type> toSortedIntList(final Iterable<? extends Type> iterable) {
		final SortedIntList<Type> list = new SortedIntList<Type>();
		int index = 0;
		for (final Type value : iterable) {
			list.insert(index++, value);
		}
		return list;
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

	/** @return a new array with values from all passed arrays. Duplicates are added multiple times. */
	public static <Type> Array<Type> union(final Array<Type>... arrays) {
		return unionTo(new Array<Type>(), arrays);
	}

	/** @return a new typed array with values from all passed arrays. Duplicates are added multiple times. */
	public static <Type> Array<Type> union(final Class<Type> ofClass, final Array<Type>... arrays) {
		return unionTo(new Array<Type>(ofClass), arrays);
	}

	/** @return first argument array with values added from all passed arrays. Duplicates are added multiple
	 *         times. */
	public static <Type> Array<Type> unionTo(final Array<Type> array, final Array<Type>... arrays) {
		if (arrays == null || arrays.length == 0) {
			return array;
		}
		for (final Array<Type> unionedArray : arrays) {
			array.addAll(unionedArray);
		}
		return array;
	}
}
