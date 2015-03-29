package com.github.czyzby.lml.util.tuple.util;

import java.util.Iterator;

import com.github.czyzby.lml.util.tuple.Tuple;

/** Contains utilties for tuples. Note that factory methods are provided by classes that are implementation of
 * specific tuple interfaces.
 *
 * @author MJ */
public class Tuples {
	private Tuples() {
	}

	/** @return a new instance of an iterator that iterates over tuple's values. */
	public static <Type> Iterator<Type> getTupleIterator(final Tuple tuple) {
		return new Iterator<Type>() {
			private int currentIndex;

			@Override
			public boolean hasNext() {
				return currentIndex < tuple.getSize();
			}

			@Override
			@SuppressWarnings("unchecked")
			public Type next() {
				return (Type) tuple.get(currentIndex++);
			}

			@Override
			public void remove() {
				if (tuple.isMutable()) {
					tuple.set(currentIndex, null);
				}
			}
		};
	}
}
