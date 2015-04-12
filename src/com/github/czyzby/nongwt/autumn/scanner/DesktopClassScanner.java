package com.github.czyzby.nongwt.autumn.scanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.Reflection;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;
import com.github.czyzby.nongwt.autumn.reflection.StandardReflectionProvider;

/** Tries to scan class path resources if running from binaries (IDE) or Jar files.
 *
 * @author MJ */
public class DesktopClassScanner implements ClassScanner {
	private static final char INNER_CLASS_SIGN = '$';
	private static final char DOT_SEPARATOR = '.';
	private static final char FILE_SEPARATOR = '/';

	private static final String CLASS_FILE_EXTENSION = ".class";
	private static final String JAR_FILE_EXTENSION = ".jar";

	static {
		Reflection.setReflectionProvider(new StandardReflectionProvider());
	}

	@Override
	public ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> findClassesAnnotatedWith(
			final Class<?> root, final Iterable<Class<? extends Annotation>> annotations) {
		final String mainPackageName = root.getPackage().getName();
		final String classPathRoot = getClassPathRoot(mainPackageName);
		final ClassLoader classLoader =
				root.getClassLoader() == null ? ClassLoader.getSystemClassLoader() : root.getClassLoader();
		try {
			final Enumeration<URL> resources = classLoader.getResources(classPathRoot);
			final Queue<Pair<File, Integer>> filesWithDepthsToProcess = new LinkedList<Pair<File, Integer>>();
			while (resources.hasMoreElements()) {
				filesWithDepthsToProcess.add(Pair.of(toFile(resources.nextElement()), 0));
			}
			if (filesWithDepthsToProcess.isEmpty()) {
				return extractFromJar(annotations, classPathRoot, classLoader);
			}

			return extractFromBinaries(annotations, mainPackageName, filesWithDepthsToProcess);
		} catch (final Throwable exception) {
			throw new AutumnRuntimeException("Unable to scan classpath.", exception);
		}
	}

	private ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> extractFromBinaries(
			final Iterable<Class<? extends Annotation>> annotations, final String mainPackageName,
			final Queue<Pair<File, Integer>> filesWithDepthsToProcess) throws ReflectionException {
		final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> result =
				LazyObjectMap.newMapOfSets();
		while (!filesWithDepthsToProcess.isEmpty()) {
			final Pair<File, Integer> classPathFileWithDepth = filesWithDepthsToProcess.poll();
			final File classPathFile = classPathFileWithDepth.getFirst();
			final int depth = classPathFileWithDepth.getSecond();
			if (classPathFile.isDirectory()) {
				addAllChildren(filesWithDepthsToProcess, classPathFile, depth);
			} else {
				final Class<?> classToProcess =
						ClassReflection.forName(getBinaryClassName(mainPackageName, classPathFile, depth));
				mapClassByAnnotations(annotations, result, classToProcess);
			}
		}
		return result;
	}

	private File toFile(final URL url) throws URISyntaxException {
		return new File(url.toURI()).getAbsoluteFile();
	}

	private void addAllChildren(final Queue<Pair<File, Integer>> rootFiles, final File classPathFile,
			int depth) {
		depth++;
		for (final File file : classPathFile.listFiles()) {
			if (file.isDirectory() || file.getName().endsWith(CLASS_FILE_EXTENSION)) {
				rootFiles.add(Pair.of(file, depth));
			}
		}
	}

	private String getBinaryClassName(final String mainPackageName, final File classPathFile, final int depth) {
		final String[] classFolders = classPathFile.getPath().split(File.separator);
		final StringBuilder builder = new StringBuilder(mainPackageName);
		for (int folderIndex = classFolders.length - depth; folderIndex < classFolders.length - 1; folderIndex++) {
			builder.append(DOT_SEPARATOR).append(classFolders[folderIndex]);
		}
		final String classFileName = classFolders[classFolders.length - 1];
		builder.append(DOT_SEPARATOR).append(
				classFileName.substring(0, classFileName.length() - CLASS_FILE_EXTENSION.length()));
		return builder.toString();
	}

	private String getClassPathRoot(final String mainPackageName) {
		return mainPackageName.replace(DOT_SEPARATOR, FILE_SEPARATOR);
	}

	private ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> extractFromJar(
			final Iterable<Class<? extends Annotation>> annotations, final String classPathRoot,
			final ClassLoader classLoader) throws URISyntaxException, IOException, ReflectionException {
		final Array<JarFile> filesToProcess = getJarFilesToProcess();
		final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> result =
				LazyObjectMap.newMapOfSets();
		for (final JarFile jarFile : filesToProcess) {
			final Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry entry = entries.nextElement();
				processEntry(annotations, classPathRoot, result, entry);
			}
		}
		return result;
	}

	private Array<JarFile> getJarFilesToProcess() throws URISyntaxException, IOException {
		final Array<JarFile> filesToProcess = GdxArrays.newArray();
		final File jarDirectory = new File(ClassLoader.getSystemClassLoader().getResource(".").toURI());
		for (final File file : jarDirectory.listFiles()) {
			if (file.getName().endsWith(JAR_FILE_EXTENSION)) {
				filesToProcess.add(new JarFile(file));
			}
		}
		return filesToProcess;
	}

	private void processEntry(final Iterable<Class<? extends Annotation>> annotations,
			final String classPathRoot,
			final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> result,
			final JarEntry entry) throws ReflectionException {
		if (!entry.isDirectory()) {
			final String entryName = entry.getName();
			if (!Strings.contains(entryName, INNER_CLASS_SIGN) && entryName.startsWith(classPathRoot)) {
				final String className = jarEntryToClassName(entryName);
				final Class<?> classToProcess = ClassReflection.forName(className);
				mapClassByAnnotations(annotations, result, classToProcess);
			}
		}
	}

	private void mapClassByAnnotations(final Iterable<Class<? extends Annotation>> annotations,
			final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> result,
			final Class<?> classToProcess) {
		for (final Class<? extends Annotation> annotation : annotations) {
			if (ClassReflection.isAnnotationPresent(classToProcess, annotation)) {
				result.get(annotation).add(Reflection.getWrapperForClass(classToProcess));
			}
		}
	}

	private String jarEntryToClassName(final String entryName) {
		return entryName.substring(0, entryName.length() - CLASS_FILE_EXTENSION.length()).replace(
				FILE_SEPARATOR, DOT_SEPARATOR);
	}
}
