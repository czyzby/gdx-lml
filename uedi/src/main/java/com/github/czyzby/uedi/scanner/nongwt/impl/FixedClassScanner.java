package com.github.czyzby.uedi.scanner.nongwt.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

/** Does not support automatic scanning. Instead, a fixed pool of scannable classes is provided and scanned when
 * requested. While heavily relying on reflection, this might actually be a faster solution in case of huge contexts (if
 * the components are not in separate root package, which they should be). Use when absolutely necessary.
 *
 * @author MJ */
public class FixedClassScanner extends AbstractClassScanner {
    private final ObjectSet<Class<?>> context = GdxSets.newSet();

    /** @param scannableClasses will be available for scanning. */
    public FixedClassScanner(final Class<?>... scannableClasses) {
        add(scannableClasses);
    }

    /** @param scannableClasses will be available for scanning. */
    public void add(final Class<?>... scannableClasses) {
        context.addAll(scannableClasses);
    }

    /** @param scannableClasses will be available for scanning. */
    public void add(final Array<Class<?>> scannableClasses) {
        context.addAll(scannableClasses);
    }

    @Override
    public Iterable<Class<?>> getClassesImplementing(final Class<?> root, final Class<?>... interfaces) {
        final ObjectSet<Class<?>> implementingClasses = GdxSets.newSet();
        final String rootPackage = getPackageName(root);
        for (final Class<?> scannableClass : context) {
            if (getPackageName(scannableClass).startsWith(rootPackage) && isNotAbstract(scannableClass)
                    && isInstanceOfAny(scannableClass, interfaces)) {
                implementingClasses.add(scannableClass);
            }
        }
        return implementingClasses;
    }
}
