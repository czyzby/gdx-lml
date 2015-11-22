package com.github.czyzby.autumn.android.scanner;

import java.lang.annotation.Annotation;
import java.util.Enumeration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.kiwi.util.common.Exceptions;

import android.content.pm.ApplicationInfo;
import dalvik.system.DexFile;

/** Default {@link ClassScanner} implementation for Android platform.
 *
 * @author MJ */
public class AndroidClassScanner implements ClassScanner {
    @Override
    public Array<Class<?>> findClassesAnnotatedWith(final Class<?> root,
            final Iterable<Class<? extends Annotation>> annotations) {
        final Array<Class<?>> result = new Array<Class<?>>();
        final ApplicationInfo applicationInfo = ((AndroidApplication) Gdx.app).getApplicationInfo();
        final String classPath = applicationInfo.sourceDir;
        final String packageName = root.getPackage().getName();
        DexFile dexFile = null;
        try {
            dexFile = new DexFile(classPath);
            final Enumeration<String> classesNames = dexFile.entries();
            while (classesNames.hasMoreElements()) {
                processClassName(annotations, result, packageName, classesNames.nextElement());
            }
            return result;
        } catch (final Exception exception) {
            throw new GdxRuntimeException("Unable to scan Android application.", exception);
        } finally {
            closeFile(dexFile);
        }
    }

    private static void processClassName(final Iterable<Class<? extends Annotation>> annotations,
            final Array<Class<?>> result, final String packageName, final String className) {
        if (!className.startsWith(packageName)) {
            return;
        }
        try {
            final Class<?> classToProcess = ClassReflection.forName(className);
            for (final Class<? extends Annotation> annotation : annotations) {
                if (ClassReflection.isAnnotationPresent(classToProcess, annotation)) {
                    result.add(classToProcess);
                    return;
                }
            }
        } catch (final ReflectionException exception) {
            exception.printStackTrace(); // Unexpected. Classes should be present.
        }
    }

    private static void closeFile(final DexFile dexFile) {
        try {
            if (dexFile != null) {
                dexFile.close();
            }
        } catch (final Exception exception) {
            Exceptions.ignore(exception);
        }
    }
}