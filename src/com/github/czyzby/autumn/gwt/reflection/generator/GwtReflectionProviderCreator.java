package com.github.czyzby.autumn.gwt.reflection.generator;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.gwt.reflection.ExtendedReflectionProvider;
import com.github.czyzby.autumn.gwt.reflection.wrapper.GwtReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class GwtReflectionProviderCreator {
	private static final boolean DEBUG_ENABLED = false;

	private static final String AUTUMN_REFLECTION_INCLUDE_PROPERTY = "gdx.autumn.include";
	private static final String AUTUMN_REFLECTION_EXCLUDE_PROPERTY = "gdx.autumn.exclude";
	private static final String PACKAGE_INFO_NAME = "package-info";
	private static final String GENERATED_CLASS_NAME_PREFIX = "Generated";
	private static final String IMPORT_ALL_SUFFIX = ".*";

	@SuppressWarnings("unchecked")
	private static final Set<Class<? extends Annotation>> IGNORED_ANNOTATIONS = Sets.newHashSet(
			Retention.class, Deprecated.class);

	private static final String CLASS_INITIATION_METHOD_NAME = "i";
	private static final String CONSTRUCTOR_METHOD_NAME = "c";
	private static final String FIELD_METHOD_NAME = "f";
	private static final String FIELD_GETTER_METHOD_NAME = "fg";
	private static final String FIELD_SETTER_METHOD_NAME = "fs";
	private static final String METHOD_METHOD_NAME = "m";
	private static final String METHOD_INVOCATION_METHOD_NAME = "mi";

	private static final JType[] EMPTY_PARAMETERS_ARRAY = new JType[] {};

	private final TreeLogger logger;
	private final GeneratorContext context;
	private final JClassType requestedType;
	private final List<String> includedTypes, excludedTypes;
	private final StringBuilder debugLogBuffer;
	private SourceWriter sourceWriter;

	// Control variables:
	private int currentClassId;
	private int currentMethodId;
	private int currentFieldId;

	public GwtReflectionProviderCreator(final TreeLogger logger, final GeneratorContext context,
			final JClassType requestedType) throws BadPropertyValueException {
		this.logger = logger;
		this.context = context;
		this.requestedType = requestedType;
		final PropertyOracle properyOracle = context.getPropertyOracle();
		includedTypes = getTypesToInclude(properyOracle);
		excludedTypes = getTypesToExclude(properyOracle);
		debugLogBuffer = initiateDebugLogBuffer();
	}

	private List<String> getTypesToInclude(final PropertyOracle properyOracle)
			throws BadPropertyValueException {
		return properyOracle.getConfigurationProperty(AUTUMN_REFLECTION_INCLUDE_PROPERTY).getValues();
	}

	private List<String> getTypesToExclude(final PropertyOracle properyOracle) {
		try {
			return properyOracle.getConfigurationProperty(AUTUMN_REFLECTION_EXCLUDE_PROPERTY).getValues();
		} catch (final Throwable exception) {
			// Unknown property(?) - no excluded classes. Somewhat expected.
			return new ArrayList<String>();
		}
	}

	private StringBuilder initiateDebugLogBuffer() {
		if (DEBUG_ENABLED) {
			return new StringBuilder();
		}
		return null;
	}

	public String generateClass() {
		final String packageName = getGeneratedPackageName();
		final String generatedClassName = getGeneratedClassName();
		final ClassSourceFileComposerFactory composer =
				prepareClassSourceFileComposer(packageName, generatedClassName);
		final PrintWriter printWriter = context.tryCreate(logger, packageName, generatedClassName);
		if (printWriter == null) {
			return getCanonicalGeneratedClassName();
		}
		sourceWriter = composer.createSourceWriter(context, printWriter);

		final List<JClassType> classesToReflect = findClassesToReflect();
		appendClassOpening();
		appendClassInitiationMethods(classesToReflect);
		for (final JClassType classToReflect : classesToReflect) {
			appendReflectedClass(classToReflect);
		}

		sourceWriter.commit(logger);
		return getCanonicalGeneratedClassName();
	}

	private List<JClassType> findClassesToReflect() {
		final List<JClassType> classesToReflect = new ArrayList<JClassType>();
		final TypeOracle typeOracle = context.getTypeOracle();
		for (final JPackage jPackage : typeOracle.getPackages()) {
			for (final JClassType jClassType : jPackage.getTypes()) {
				if (isClassRequestedForReflection(jClassType)) {
					classesToReflect.add(jClassType);
				}
			}
		}
		return classesToReflect;
	}

	private boolean isClassRequestedForReflection(final JClassType jClassType) {
		if (jClassType == null || jClassType.isInterface() != null || jClassType.isAnnotation() != null
				|| jClassType.isPrimitive() != null
				|| jClassType.getQualifiedSourceName().contains(PACKAGE_INFO_NAME)) {
			return false;
		}
		final String typeName = jClassType.getQualifiedSourceName();
		for (final String excludedType : excludedTypes) {
			if (typeName.contains(excludedType)) {
				return false;
			}
		}
		for (final String includedType : includedTypes) {
			if (typeName.contains(includedType)) {
				return true;
			}
		}
		return false;
	}

	private String getGeneratedPackageName() {
		return requestedType.getPackage().getName();
	}

	private String getGeneratedClassName() {
		return GENERATED_CLASS_NAME_PREFIX + requestedType.getSimpleSourceName();
	}

	private String getCanonicalGeneratedClassName() {
		return getGeneratedPackageName() + "." + getGeneratedClassName();
	}

	private ClassSourceFileComposerFactory prepareClassSourceFileComposer(final String packageName,
			final String generatedClassName) {
		final ClassSourceFileComposerFactory composer =
				new ClassSourceFileComposerFactory(packageName, generatedClassName);
		composer.addImplementedInterface(ExtendedReflectionProvider.class.getCanonicalName());
		composer.addImport(constructImportForPackage(ReflectedClass.class));
		composer.addImport(constructImportForPackage(GwtReflectedClass.class));
		composer.addImport(ReflectionException.class.getCanonicalName());
		composer.addImport(constructImportForPackage(Map.class));
		return composer;
	}

	private String constructImportForPackage(final Class<?> withClass) {
		return withClass.getPackage().getName() + IMPORT_ALL_SUFFIX;
	}

	// Appending methods.

	private GwtReflectionProviderCreator append(final Object element) {
		sourceWriter.print(element.toString());
		if (DEBUG_ENABLED) {
			debugLogBuffer.append(element);
		}
		return this;
	}

	private void appendLine() {
		sourceWriter.println();
		if (DEBUG_ENABLED) {
			clearDebugLogBuffer();
		}
	}

	private void appendLine(final String line) {
		sourceWriter.println(line);
		if (DEBUG_ENABLED) {
			clearDebugLogBuffer();
			System.err.println(line);
			logger.log(Type.INFO, line);
		}
	}

	private void clearDebugLogBuffer() {
		if (debugLogBuffer.length() > 0) {
			logger.log(Type.INFO, debugLogBuffer.toString());
			System.err.println(debugLogBuffer.toString());
			debugLogBuffer.setLength(0);
		}
	}

	private void appendClassOpening() {
		// Class map initiation:
		appendLine("private final Map<Class<?>, ReflectedClass> classesPool = new HashMap<Class<?>, ReflectedClass>();");
		// Constructor:
		appendLine("public " + getGeneratedClassName() + "() { initiateClassesPool(); }");
		// Providing method implementation:
		appendLine("@Override public ReflectedClass provide(final Class<?> reflectedClass) { ReflectedClass reflectedClassWrapper = classesPool.get(reflectedClass); if (reflectedClassWrapper == null) { throw new java.lang.RuntimeException(\"Class unavailable for reflection: \" + reflectedClass); } return reflectedClassWrapper; }");
		// Class pool method implementation:
		appendLine("@Override public Iterable<ReflectedClass> getReflectedClassesPool() { return classesPool.values(); }");
	}

	private void appendClassInitiationMethods(final List<JClassType> classesToReflect) {
		append("private void initiateClassesPool() { ");
		for (int classId = 0; classId < classesToReflect.size(); classId++) {
			append(CLASS_INITIATION_METHOD_NAME).append(classId).append("(); ");
		}
		append(" }").appendLine();
	}

	private void appendReflectedClass(final JClassType classToReflect) {
		final int classId = currentClassId++;
		final List<JMethod> qualifiedMethods = findQualifiedMethods(classToReflect);
		final List<JField> qualifiedFields = findQualifiedFields(classToReflect);

		appendClassInitiationMethodOpening(classId);
		appendClassToReflectObject(classToReflect);
		appendImplementedInterfaces(classToReflect);
		appendSuperclass(classToReflect);
		appendMethodsArray(qualifiedMethods);
		appendFieldsArray(qualifiedFields);
		appendAnnotations(classToReflect.getDeclaredAnnotations());
		appendClassInitiationMethodClosing(classId);

		// External methods:
		appendConstructorMethod(classToReflect, classId);
		for (final JMethod method : qualifiedMethods) {
			appendMethod(classToReflect, method);
		}
		for (final JField field : qualifiedFields) {
			appendField(classToReflect, field);
		}
	}

	private List<JMethod> findQualifiedMethods(final JClassType classToReflect) {
		final List<JMethod> qualifiedMethods = new ArrayList<JMethod>();
		for (final JMethod method : classToReflect.getMethods()) {
			if (method.isStatic()) {
				continue;
			}
			final Annotation[] annotations = method.getDeclaredAnnotations();
			for (final Annotation annotation : annotations) {
				if (!IGNORED_ANNOTATIONS.contains(annotation.annotationType())) {
					qualifiedMethods.add(method);
					break;
				}
			}
		}
		return qualifiedMethods;
	}

	private List<JField> findQualifiedFields(final JClassType classToReflect) {
		final List<JField> qualifiedFields = new ArrayList<JField>();
		for (final JField field : classToReflect.getFields()) {
			if (field.isStatic()) {
				continue;
			}
			final Annotation[] annotations = field.getDeclaredAnnotations();
			for (final Annotation annotation : annotations) {
				if (!IGNORED_ANNOTATIONS.contains(annotation.annotationType())) {
					qualifiedFields.add(field);
					break;
				}
			}
		}
		return qualifiedFields;
	}

	private void appendClassInitiationMethodOpening(final int classId) {
		append("private void ").append(CLASS_INITIATION_METHOD_NAME).append(classId).append("() {")
				.appendLine();
	}

	private void appendClassToReflectObject(final JClassType classToReflect) {
		append("Class<?> classToReflect = ").append(classToReflect.getQualifiedSourceName())
				.append(".class;").appendLine();
	}

	private void appendImplementedInterfaces(final JClassType classToReflect) {
		append("Class<?>[] interfaces = new Class[] { ");
		for (final JClassType implementedInterface : classToReflect.getImplementedInterfaces()) {
			append(implementedInterface.getQualifiedSourceName()).append(".class, ");
		}
		append("};").appendLine();
	}

	private void appendSuperclass(final JClassType classToReflect) {
		final String superclassName =
				classToReflect.getSuperclass() == null ? "null" : classToReflect.getSuperclass()
						.getQualifiedSourceName() + ".class";
		append("Class<?> superclass = ").append(superclassName).append(";").appendLine();
	}

	private void appendAnnotations(final Annotation[] annotations) {
		append("java.lang.annotation.Annotation[] annotationArray = ")
				.append(constructAnnotationsArray(annotations)).append(";").appendLine();
		appendLine("Map<Class<? extends java.lang.annotation.Annotation>, java.lang.annotation.Annotation> annotations = new HashMap<Class<? extends java.lang.annotation.Annotation>, java.lang.annotation.Annotation>();");
		appendLine("for (java.lang.annotation.Annotation annotation : annotationArray) { annotations.put(annotation.annotationType(), annotation); }");
	}

	private String constructAnnotationsArray(final Annotation[] annotations) {
		final List<Annotation> validAnnotations = getValidAnnotations(annotations);
		if (validAnnotations.isEmpty()) {
			return "new java.lang.annotation.Annotation[] {}";
		}
		final StringBuilder annotationBuilder = new StringBuilder();
		annotationBuilder.append("new java.lang.annotation.Annotation[] {");
		for (final Annotation annotation : validAnnotations) {
			final Class<?> annotationType = annotation.annotationType();
			annotationBuilder.append(" new ").append(annotationType.getCanonicalName()).append("() {");
			// Overriding all methods:
			final Method[] methods = annotationType.getDeclaredMethods();
			for (final Method method : methods) {
				final Class<?> returnType = method.getReturnType();
				annotationBuilder.append(" @Override public ").append(returnType.getCanonicalName());
				annotationBuilder.append(" ").append(method.getName()).append("() { return");
				if (returnType.isArray()) {
					annotationBuilder.append(" new ").append(returnType.getCanonicalName()).append(" {");
				}
				// Getting the actual method result:
				final Object invokeResult = getAnnotationMethodResult(annotation, method);
				addAnnotationMethodResult(annotationBuilder, returnType, invokeResult);
				annotationBuilder.append("; }");
			}
			// Overriding annotationType():
			annotationBuilder
					.append(" @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return ");
			annotationBuilder.append(annotationType.getCanonicalName()).append(".class; } }, ");
		}
		annotationBuilder.append("}");
		return annotationBuilder.toString();
	}

	private List<Annotation> getValidAnnotations(final Annotation[] annotations) {
		final List<Annotation> validAnnotations = new ArrayList<Annotation>();
		if (annotations == null || annotations.length == 0) {
			return validAnnotations;
		}
		for (final Annotation annotation : annotations) {
			if (!IGNORED_ANNOTATIONS.contains(annotation.annotationType())) {
				final Retention retention = annotation.annotationType().getAnnotation(Retention.class);
				if (retention != null && retention.value() == RetentionPolicy.RUNTIME) {
					validAnnotations.add(annotation);
				}
			}
		}
		return validAnnotations;
	}

	private Object getAnnotationMethodResult(final Annotation annotation, final Method method) {
		try {
			return method.invoke(annotation);
		} catch (final Throwable exception) {
			logger.log(Type.ERROR, "Error invoking annotation method.", exception);
			throw new RuntimeException(exception);
		}
	}

	private void addAnnotationMethodResult(final StringBuilder annotationBuilder, final Class<?> returnType,
			final Object invokeResult) {
		if (invokeResult == null) {
			annotationBuilder.append(" null");
		} else if (returnType.equals(String[].class)) { // String[]
			for (final String string : (String[]) invokeResult) {
				annotationBuilder.append(" \"").append(string).append("\",");
			}
		} else if (returnType.equals(String.class)) { // String
			annotationBuilder.append(" \"").append((String) invokeResult).append("\"");
		} else if (returnType.equals(Class[].class)) { // Class[]
			for (final Class<?> classInArray : (Class[]) invokeResult) {
				annotationBuilder.append(" ").append(classInArray.getCanonicalName()).append(".class,");
			}
		} else if (returnType.equals(Class.class)) { // Class
			annotationBuilder.append(" ").append(((Class<?>) invokeResult).getCanonicalName())
					.append(".class");
		} else if (returnType.isArray() && returnType.getComponentType().isEnum()) { // Enum[]
			final String enumTypeName = returnType.getComponentType().getCanonicalName();
			final int length = Array.getLength(invokeResult);
			for (int index = 0; index < length; index++) {
				final Object enumConstant = Array.get(invokeResult, index);
				annotationBuilder.append(" ").append(enumTypeName).append(".")
						.append(enumConstant.toString()).append(",");
			}
		} else if (returnType.isEnum()) { // Enum
			annotationBuilder.append(" ").append(returnType.getCanonicalName()).append(".")
					.append(invokeResult.toString());
		} else if (returnType.isArray() && returnType.getComponentType().isPrimitive()) { // primitive[]
			final Class<?> primitiveType = returnType.getComponentType();
			final int length = Array.getLength(invokeResult);
			for (int index = 0; index < length; index++) {
				final Object primitive = Array.get(invokeResult, index);
				annotationBuilder.append(" ").append(primitive.toString());
				if (primitiveType.equals(float.class)) {
					annotationBuilder.append("f");
				}
				annotationBuilder.append(",");
			}
		} else if (returnType.isPrimitive()) { // primitive
			annotationBuilder.append(" ").append(invokeResult.toString());
			if (returnType.equals(float.class)) {
				annotationBuilder.append("f");
			}
		} else {
			logger.log(Type.ERROR, "Annotation method return type not supported (or not yet implemented): "
					+ returnType);
			throw new RuntimeException("Unsupported annotation return type: " + returnType);
		}
		if (returnType.isArray()) {
			annotationBuilder.append(" }");
		}
	}

	private void appendMethodsArray(final List<JMethod> qualifiedMethods) {
		append("ReflectedMethod[] methods = new ReflectedMethod[] { ");
		for (int methodIndex = currentMethodId; methodIndex < currentMethodId + qualifiedMethods.size(); methodIndex++) {
			append(METHOD_METHOD_NAME).append(methodIndex).append("(), ");
		}
		append("};").appendLine();
	}

	private void appendFieldsArray(final List<JField> qualifiedFields) {
		append("ReflectedField[] fields = new ReflectedField[] { ");
		for (int fieldIndex = currentFieldId; fieldIndex < currentFieldId + qualifiedFields.size(); fieldIndex++) {
			append(FIELD_METHOD_NAME).append(fieldIndex).append("(), ");
		}
		append("};").appendLine();
	}

	private void appendClassInitiationMethodClosing(final int classId) {
		append("ReflectedMethod constructor = ").append(CONSTRUCTOR_METHOD_NAME).append(classId)
				.append("();").appendLine();
		appendLine("GwtReflectedClass reflectedClass = new GwtReflectedClass(classToReflect, interfaces, superclass, methods, fields, annotations, constructor);");
		appendLine("reflectedClass.initiate();");
		appendLine("classesPool.put(reflectedClass.getReflectedClass(), reflectedClass); }");
	}

	private void appendConstructorMethod(final JClassType classToReflect, final int classId) {
		append("private ReflectedMethod ").append(CONSTRUCTOR_METHOD_NAME).append(classId).append("() {")
				.appendLine();
		append("return new GwtReflectedMethod(").append(classIdToConstructorId(classId))
				.append(", new Class[] {}, null) {").appendLine();
		appendLine("@Override public Object invoke(Object owner, Object... parameters) throws ReflectionException {");
		try {
			final JConstructor constructor = classToReflect.getConstructor(EMPTY_PARAMETERS_ARRAY);
			if (constructor == null || !constructor.isPublic()) {
				append("throw new ReflectionException(\"No public no-arg constructor specified.\");");
			} else if (classToReflect.isAbstract() || classToReflect.isInterface() != null) {
				append("throw new ReflectionException(\"Class is abstract or an interface.\");");
			} else {
				append("return new ").append(classToReflect.getQualifiedSourceName()).append("();");
			}
		} catch (final NotFoundException exception) {
			append("throw new ReflectionException(\"No public no-arg constructor specified.\");");
		}
		append("} }; }").appendLine();
	}

	private int classIdToConstructorId(final int classId) {
		return -classId - 1;
	}

	private void appendMethod(final JClassType classToReflect, final JMethod method) {
		final int methodId = currentMethodId++;
		append("private ReflectedMethod ").append(METHOD_METHOD_NAME).append(methodId).append("() {")
				.appendLine();
		appendAnnotations(method.getDeclaredAnnotations());
		// Anonymous object creation:
		append("return new GwtReflectedMethod(").append(methodId).append(", new Class[] { ");
		final JType[] parameterTypes = method.getParameterTypes();
		for (final JType parameterType : parameterTypes) {
			append(parameterType.getErasedType().getQualifiedSourceName()).append(".class, ");
		}
		append("}, annotations) {").appendLine();
		// Implementation of invoking method:
		append("@Override public Object invoke(Object owner, Object... parameters) throws ReflectionException { ");
		append("if (parameters.length != ")
				.append(parameterTypes.length)
				.append(") { throw new ReflectionException(\"Unexpected amount of parameters: \" + parameters.length); }")
				.appendLine();
		// Native method invocation:
		append("return ").append(METHOD_INVOCATION_METHOD_NAME).append(methodId).append("((")
				.append(classToReflect.getQualifiedSourceName()).append(") owner");
		if (parameterTypes.length > 0) {
			append(", ");
		}
		for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++) {
			append("(").append(getCastingTypeName(parameterTypes[parameterIndex])).append(") parameters[")
					.append(parameterIndex).append("]");
			if (parameterIndex != parameterTypes.length - 1) {
				append(", ");
			}
		}
		append("); } }; }").appendLine();

		appendNativeMethod(classToReflect, method, methodId, parameterTypes);
	}

	private String getCastingTypeName(final JType type) {
		final JType erasedType = type.getErasedType();
		final JPrimitiveType primitive = erasedType.isPrimitive();
		if (primitive != null) {
			return primitive.getQualifiedBoxedSourceName();
		}
		return erasedType.getQualifiedSourceName();
	}

	private void appendNativeMethod(final JClassType classToReflect, final JMethod method,
			final int methodId, final JType[] parameterTypes) {
		append("private native ")
				.append(method.getReturnType().getErasedType().getQualifiedSourceName()
						.equalsIgnoreCase("void") ? "Object" : method.getReturnType().getErasedType())
				.append(" ").append(METHOD_INVOCATION_METHOD_NAME).append(methodId).append("(")
				.append(classToReflect.getQualifiedSourceName()).append(" owner");
		if (parameterTypes.length > 0) {
			append(", ");
		}
		for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++) {
			append(parameterTypes[parameterIndex].getErasedType().getQualifiedSourceName()).append(" p")
					.append(parameterIndex);
			if (parameterIndex != parameterTypes.length - 1) {
				append(", ");
			}
		}
		append(") /*-{").appendLine();
		append("return owner.").append(method.getJsniSignature()).append("(");
		for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++) {
			append("p").append(parameterIndex);
			if (parameterIndex != parameterTypes.length - 1) {
				append(", ");
			}
		}
		append("); }-*/;").appendLine();
	}

	private void appendField(final JClassType classToReflect, final JField field) {
		final int fieldId = currentFieldId++;
		append("private ReflectedField ").append(FIELD_METHOD_NAME).append(fieldId).append("() {")
				.appendLine();
		appendAnnotations(field.getDeclaredAnnotations());
		// Anonymous field class:
		append("return new GwtReflectedField(").append(fieldId).append(", ")
				.append(field.getType().getErasedType().getQualifiedSourceName())
				.append(".class, annotations) {").appendLine();
		// Setter method implementation:
		append("@Override public void set(Object owner, Object fieldValue) throws ReflectionException { ");
		append(FIELD_SETTER_METHOD_NAME).append(fieldId).append("((")
				.append(classToReflect.getQualifiedSourceName()).append(") owner, (")
				.append(getCastingTypeName(field.getType())).append(") fieldValue); }").appendLine();
		// Getter method implementation:
		append("@Override public Object get(Object owner) throws ReflectionException { ");
		append("return ").append(FIELD_GETTER_METHOD_NAME).append(fieldId).append("((")
				.append(classToReflect.getQualifiedSourceName()).append(") owner); }");
		append(" }; }").appendLine();

		appendNativeFieldGetter(classToReflect, field, fieldId);
		appendNativeFieldSetter(classToReflect, field, fieldId);
	}

	private void appendNativeFieldGetter(final JClassType classToReflect, final JField field,
			final int fieldId) {
		append("private native ").append(field.getType().getErasedType().getQualifiedSourceName())
				.append(" ").append(FIELD_GETTER_METHOD_NAME).append(fieldId).append("(")
				.append(classToReflect.getQualifiedSourceName()).append(" owner) /*-{").appendLine();
		append("return owner.@").append(classToReflect.getQualifiedSourceName()).append("::")
				.append(field.getName()).append("; }-*/;").appendLine();
	}

	private void appendNativeFieldSetter(final JClassType classToReflect, final JField field,
			final int fieldId) {
		append("private native void ").append(FIELD_SETTER_METHOD_NAME).append(fieldId).append("(")
				.append(classToReflect.getQualifiedSourceName()).append(" owner, ")
				.append(field.getType().getErasedType().getQualifiedSourceName()).append(" fieldValue) /*-{")
				.appendLine();
		append("owner.@").append(classToReflect.getQualifiedSourceName()).append("::")
				.append(field.getName()).append(" = fieldValue; }-*/;").appendLine();
	}
}
