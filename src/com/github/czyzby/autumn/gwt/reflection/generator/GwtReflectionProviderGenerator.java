package com.github.czyzby.autumn.gwt.reflection.generator;

import static com.google.gwt.core.ext.TreeLogger.ERROR;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class GwtReflectionProviderGenerator extends Generator {
	@Override
	public String generate(final TreeLogger logger, final GeneratorContext context, final String typeName)
			throws UnableToCompleteException {
		final TypeOracle oracle = context.getTypeOracle();
		final JClassType type = oracle.findType(typeName);
		if (type == null || type.isInterface() == null) {
			logger.log(ERROR, "Unable to generate. Type not found or not an interface: " + type + ".");
		}

		try {
			return new GwtReflectionProviderCreator(logger, context, type).generateClass();
		} catch (final Throwable exception) {
			logger.log(ERROR, "Unable to generate source. Exception thrown: " + exception.getMessage(),
					exception);
			throw new UnableToCompleteException();
		}
	}
}