package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.reflection.impl.MethodMember;
import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Named;

/** Wraps around a method, converting it into a provider.
 *
 * @author MJ */
public class ReflectionProvider implements DelegateProvider<Object>, Named {
    private final Context context;
    private final Method method;
    private final Class<?> type;
    private final Object owner;
    private final Class<?>[] parameterTypes;
    private final Object[] parameters;
    private final String name;
    private final boolean isDefault;
    private final Member methodMember;

    /** @param context parent context.
     * @param owner instance of the class with the method.
     * @param method will be wrapped and converted into a provider. */
    public ReflectionProvider(final Context context, final Object owner, final Method method) {
        this.context = context;
        this.owner = owner;
        this.method = method;
        type = method.getReturnType();
        parameterTypes = method.getParameterTypes();
        parameters = parameterTypes.length == 0 ? Providers.EMPTY_ARRAY : new Object[parameterTypes.length];
        name = Providers.getName(method);
        isDefault = owner instanceof Default;
        methodMember = new MethodMember(method);
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public Object getWrappedObject() {
        return owner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends Object> getType() {
        return type;
    }

    @Override
    public Object provide(final Object target, final Member member) {
        try {
            if (parameters.length == 0) {
                return method.invoke(owner, parameters);
            }
            final Class<?> targetType = target == null ? null : target.getClass();
            for (int index = 0, length = parameters.length; index < length; index++) {
                final Class<?> parameterType = parameterTypes[index];
                parameters[index] = parameterType == Object.class || parameterType == targetType ? target
                        : context.get(parameterType, owner, methodMember);
            }
            return method.invoke(owner, parameters);
        } catch (final RuntimeException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new RuntimeException("Unable to invoke method: '" + method.getName() + "' of component: " + owner,
                    exception);
        }
    }
}
