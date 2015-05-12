package com.github.czyzby.autumn.gwt.reflection.generator;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.github.czyzby.autumn.gwt.reflection.ReflectionPool;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/** Generates {@link com.github.czyzby.autumn.gwt.reflection.ReflectionPool} instance, aware of all
 * GWT-reflected classes.
 *
 * @author MJ */
public class ReflectionPoolGenerator extends Generator {
	private static final String GENERATED_CLASS_PREFIX = "Generated";

	@Override
	public String generate(final TreeLogger logger, final GeneratorContext context, final String typeName)
			throws UnableToCompleteException {
		final TypeOracle oracle = context.getTypeOracle();
		assert oracle != null;
		final JClassType type = oracle.findType(typeName);
		if (type == null || type.isInterface() == null) {
			logger.log(Type.ERROR, "Invalid type: " + typeName + ".");
			throw new UnableToCompleteException();
		}

		final String packageName = type.getPackage().getName();
		final String generatedClassName = GENERATED_CLASS_PREFIX + type.getSimpleSourceName();
		final ClassSourceFileComposerFactory composer =
				new ClassSourceFileComposerFactory(packageName, generatedClassName);
		composer.addImplementedInterface(ReflectionPool.class.getCanonicalName());

		final PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);
		if (printWriter == null) {
			return getQualifiedGeneratedClassName(packageName, generatedClassName);
		}

		final SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);
		final Set<JType> reflectedClasses = findReflectedClasses(context);
		appendClassBody(sourceWriter, reflectedClasses);

		sourceWriter.commit(logger);
		return getQualifiedGeneratedClassName(packageName, generatedClassName);
	}

	private String getQualifiedGeneratedClassName(final String packageName, final String generatedClassName) {
		return packageName + "." + generatedClassName;
	}

	private void appendClassBody(final SourceWriter sourceWriter, final Set<JType> reflectedClasses) {
		sourceWriter.println("private static final Class<?>[] POOL = new Class<?>[] { ");
		for (final JType reflectedClass : reflectedClasses) {
			sourceWriter.print(reflectedClass.getQualifiedSourceName() + ".class, ");
		}
		sourceWriter.println(" };");
		sourceWriter.println("@Override public Class<?>[] getReflectedClasses() { return POOL; } ");
	}

	// LibGDX code goes here. Slightly modified, refactored and without unnecessary operations.
	private Set<JType> findReflectedClasses(final GeneratorContext context) throws UnableToCompleteException {
		final Set<JType> types = new HashSet<JType>();
		final TypeOracle typeOracle = context.getTypeOracle();
		final JPackage[] packages = typeOracle.getPackages();

		// gather all types from wanted packages
		for (final JPackage pack : packages) {
			for (final JClassType type : pack.getTypes()) {
				gatherTypes(type.getErasedType(), types, context);
			}
		}

		// gather all types from explicitly requested packages
		try {
			final ConfigurationProperty properties =
					context.getPropertyOracle().getConfigurationProperty("gdx.reflect.include");
			for (final String property : properties.getValues()) {
				final JClassType type = typeOracle.findType(property);
				if (type != null) {
					gatherTypes(type.getErasedType(), types, context);
				}
			}
		} catch (final BadPropertyValueException exception) {
			throw new UnableToCompleteException();
		}

		gatherTypes(typeOracle.findType("java.util.List").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.util.ArrayList").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.util.HashMap").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.util.Map").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.String").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Boolean").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Byte").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Long").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Character").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Short").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Integer").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Float").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.CharSequence").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Double").getErasedType(), types, context);
		gatherTypes(typeOracle.findType("java.lang.Object").getErasedType(), types, context);

		return types;
	}

	private void gatherTypes(final JType type, final Set<JType> types, final GeneratorContext context)
			throws UnableToCompleteException {
		// came here from a type that has no super class / package info / not visible
		if (type == null || type.getQualifiedSourceName().contains("-") || !isVisible(type)) {
			return;
		}

		// filter reflection scope based on configuration in gwt xml module
		boolean keep = false;
		final String name = type.getQualifiedSourceName();
		try {
			ConfigurationProperty properties;
			keep |= !name.contains(".");
			properties = context.getPropertyOracle().getConfigurationProperty("gdx.reflect.include");
			for (final String property : properties.getValues()) {
				keep |= name.contains(property);
			}
			properties = context.getPropertyOracle().getConfigurationProperty("gdx.reflect.exclude");
			for (final String property : properties.getValues()) {
				keep &= !name.equals(property);
			}
		} catch (final BadPropertyValueException exception) {
			throw new UnableToCompleteException();
		}
		if (!keep) {
			return;
		}

		// already visited this type
		if (types.contains(type.getErasedType())) {
			return;
		}
		types.add(type.getErasedType());

		if (type instanceof JPrimitiveType) {
			// nothing to do for a primitive type
			return;
		} else {
			// gather fields
			final JClassType classType = (JClassType) type;
			final JField[] fields = classType.getFields();
			if (fields != null) {
				for (final JField field : fields) {
					gatherTypes(field.getType().getErasedType(), types, context);
				}
			}

			// gather super types & interfaces
			gatherTypes(classType.getSuperclass(), types, context);
			final JClassType[] interfaces = classType.getImplementedInterfaces();
			if (interfaces != null) {
				for (final JClassType classInterface : interfaces) {
					gatherTypes(classInterface.getErasedType(), types, context);
				}
			}

			// gather method parameter & return types
			final JMethod[] methods = classType.getMethods();
			if (methods != null) {
				for (final JMethod method : methods) {
					gatherTypes(method.getReturnType().getErasedType(), types, context);
					if (method.getParameterTypes() != null) {
						for (final JType parameterType : method.getParameterTypes()) {
							gatherTypes(parameterType.getErasedType(), types, context);
						}
					}
				}
			}

			// gather inner classes
			final JClassType[] nestedClasses = classType.getNestedTypes();
			if (nestedClasses != null) {
				for (final JClassType nestedClass : nestedClasses) {
					gatherTypes(nestedClass.getErasedType(), types, context);
				}
			}
		}
	}

	private boolean isVisible(final JType type) {
		if (type == null) {
			return false;
		}

		if (type instanceof JClassType) {
			if (type instanceof JArrayType) {
				JType componentType = ((JArrayType) type).getComponentType();
				while (componentType instanceof JArrayType) {
					componentType = ((JArrayType) componentType).getComponentType();
				}
				if (componentType instanceof JClassType) {
					return ((JClassType) componentType).isPublic();
				}
			} else {
				return ((JClassType) type).isPublic();
			}
		}
		return true;
	}
}