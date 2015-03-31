package com.github.czyzby.lml.util.gdx.collection.immutable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** An ordered or unordered array of objects. Semi-immutable. Extends LibGDX Array class, deprecating and
 * throwing UnsupportedOperationExceptions on all operations that mutate the array. It is not truly immutable,
 * because the original API is not properly encapsulated; although it does overshadow Array public variables
 * to make them unaccessible without casting. Use for constants and arrays that are not supposed to be
 * changed. Also, avoiding Array public fields is advised.
 *
 * @author Nathan Sweet
 * @author MJ */
public class ImmutableArray<Type> extends Array<Type> {
	// Hiding public Array variables, making it harder to manually modify array values.
	private final int size;
	private final boolean ordered;
	@SuppressWarnings("unused")
	private final Type[] items;

	private ImmutableArrayIterable<Type> arrayIterable;

	/** Creates a new ordered, immutable array containing the elements in the specified array. The new array
	 * will have the same type of backing array. */
	public ImmutableArray(final Type[] array) {
		super(array);
	}

	/** Creates a new immutable array containing the elements in the specified array. The new array will have
	 * the same type of backing array and will be ordered if the specified array is ordered. */
	public ImmutableArray(final Array<? extends Type> array) {
		super(array);
	}

	/** Creates a new array containing the elements in the specified array. The new array will have the same
	 * type of backing array. */
	public ImmutableArray(final boolean ordered, final Type[] array, final int start, final int count) {
		super(ordered, array, start, count);
	}

	{
		this.size = super.size;
		this.ordered = super.ordered;
		this.items = super.items;
	}

	/** @return a new ImmutableArray containing the passed objects. */
	public static <Type> ImmutableArray<Type> of(final Type... values) {
		return new ImmutableArray<Type>(values);
	}

	/** @return a new ImmutableArray containing the passed objects. */
	public static <Type> ImmutableArray<Type> with(final Type... values) {
		return of(values);
	}

	/** @return a new ImmutableArray created using the passed array. */
	public static <Type> ImmutableArray<Type> copyOf(final Array<? extends Type> array) {
		return new ImmutableArray<Type>(array);
	}

	/** @return a new ImmutableArray containing the sorted passed objects. */
	public static <Type extends Comparable<?>> ImmutableArray<Type> ofSorted(final Type... values) {
		return copyOfSorted(new Array<Type>(values));
	}

	/** @return a new ImmutableArray created using the sorted passed array. */
	public static <Type extends Comparable<?>> ImmutableArray<Type> copyOfSorted(
			final Array<? extends Type> array) {
		array.sort();
		return new ImmutableArray<Type>(array);
	}

	@Override
	@Deprecated
	public void add(final Type value) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void addAll(final Array<? extends Type> array) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void addAll(final Type... array) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void addAll(final Array<? extends Type> array, final int start, final int count) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void addAll(final Type[] array, final int start, final int count) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void set(final int index, final Type value) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void insert(final int index, final Type value) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void swap(final int first, final int second) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public boolean removeValue(final Type value, final boolean identity) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public Type removeIndex(final int index) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void removeRange(final int start, final int end) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public boolean removeAll(final Array<? extends Type> array, final boolean identity) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public Type pop() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void clear() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public Type[] shrink() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public Type[] ensureCapacity(final int additionalCapacity) {
		return super.ensureCapacity(additionalCapacity);
	}

	@Override
	@Deprecated
	public void sort() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void sort(final Comparator<? super Type> comparator) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public Type selectRanked(final Comparator<Type> comparator, final int kthLowest) {
		// Might partially sort the array.
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public int selectRankedIndex(final Comparator<Type> comparator, final int kthLowest) {
		// Might partially sort the array.
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void reverse() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void shuffle() {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	@Override
	@Deprecated
	public void truncate(final int arg0) {
		throw new UnsupportedOperationException("Cannot modify ImmutableArray.");
	}

	/** @return original amount of elements in the array. */
	public int size() {
		return size;
	}

	/** @return true if the array was ordered when created. */
	public boolean isOrdered() {
		return ordered;
	}

	@Override
	public Iterator<Type> iterator() {
		if (arrayIterable == null) {
			arrayIterable = new ImmutableArrayIterable<Type>(this);
		}
		return arrayIterable.iterator();
	}

	// Based on Array.ArrayIterator.
	public static class ImmutableArrayIterator<Type> implements Iterator<Type>, Iterable<Type> {
		private final Array<Type> array;
		private int index;
		private boolean valid = true;

		public ImmutableArrayIterator(final Array<Type> array) {
			this.array = array;
		}

		@Override
		public boolean hasNext() {
			if (!valid) {
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return index < array.size;
		}

		@Override
		public Type next() {
			if (index >= array.size) {
				throw new NoSuchElementException(String.valueOf(index));
			}
			if (!valid) {
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return array.items[index++];
		}

		public void reset() {
			index = 0;
		}

		@Override
		public Iterator<Type> iterator() {
			return this;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Array is immutable.");
		}
	}

	// Based on Array.ArrayIterable.
	public static class ImmutableArrayIterable<Type> implements Iterable<Type> {
		private final Array<Type> array;
		private ImmutableArrayIterator<Type> iterator1, iterator2;

		public ImmutableArrayIterable(final Array<Type> array) {
			this.array = array;
		}

		@Override
		public Iterator<Type> iterator() {
			if (iterator1 == null) {
				iterator1 = new ImmutableArrayIterator<Type>(array);
				iterator2 = new ImmutableArrayIterator<Type>(array);
			}
			if (!iterator1.valid) {
				iterator1.reset();
				iterator1.valid = true;
				iterator2.valid = false;
				return iterator1;
			}
			iterator2.reset();
			iterator2.valid = true;
			iterator1.valid = false;
			return iterator2;
		}
	}
}