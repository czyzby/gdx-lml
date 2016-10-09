package com.github.czyzby.uedi.scanner.nongwt.impl;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.github.czyzby.uedi.reflection.impl.Modifier;
import com.github.czyzby.uedi.scanner.ClassScanner;

/** Provides utilities for {@link ClassScanner} implementations.
 *
 * @author MJ */
public abstract class AbstractClassScanner implements ClassScanner {
    /** GWT-friendly way of extracting package name.
     *
     * @param root scanning root.
     * @return name of the package of the root class. */
    protected String getPackageName(final Class<?> root) {
        return root.getName().substring(0, root.getName().length() - root.getSimpleName().length() - 1);
    }

    /** @param testedClass will be validated
     * @return true if the class is not abstract or anonymous and not an interface. */
    protected boolean isNotAbstract(final Class<?> testedClass) {
        return (Modifier.ABSTRACT & testedClass.getModifiers()) == 0 && !testedClass.isInterface();
    }

    /** @param testedClass will be validated.
     * @param interfaces set of interfaces to be checked against.
     * @return true if the class implements any of the passed interfaces. */
    protected boolean isInstanceOfAny(final Class<?> testedClass, final Class<?>[] interfaces) {
        for (final Class<?> possibleMatch : interfaces) {
            if (ClassReflection.isAssignableFrom(possibleMatch, testedClass)) {
                return true;
            }
        }
        return false;
    }
}
