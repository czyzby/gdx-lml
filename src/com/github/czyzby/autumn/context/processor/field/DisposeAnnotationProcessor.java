package com.github.czyzby.autumn.context.processor.field;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.field.Dispose;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.method.EventProcessor;
import com.github.czyzby.autumn.context.processor.method.event.ComponentEventListener;
import com.github.czyzby.autumn.context.processor.method.event.common.ComponentDestructionEvent;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Processes Dispose annotation. Handles variable disposing upon component destruction.
 *
 * @author MJ */
public class DisposeAnnotationProcessor extends ComponentFieldAnnotationProcessor implements
		ComponentEventListener {
	private final ObjectMap<ContextComponent, Array<Field>> fieldsToDispose = LazyObjectMap.newMapOfArrays();

	public DisposeAnnotationProcessor(final EventProcessor eventProcessor) {
		eventProcessor.registerListener(this);
	}

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Dispose.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final Field field) {
		fieldsToDispose.get(component).add(field);
	}

	@Override
	public Class<?> getEventType() {
		return ComponentDestructionEvent.class;
	}

	@Override
	public boolean processEvent(final Object event) {
		final ComponentDestructionEvent destructionEvent = (ComponentDestructionEvent) event;
		for (final ContextComponent componentToDestroy : destructionEvent.getComponentsToDestroy()) {
			for (final Field field : fieldsToDispose.get(componentToDestroy)) {
				disposeOf(field, componentToDestroy.getComponent());
			}
			fieldsToDispose.remove(componentToDestroy);
		}
		return KEEP_AFTER_INVOCATION;
	}

	private void disposeOf(final Field field, final Object component) {
		try {
			final Object fieldValue = Reflection.getFieldValue(field, component);
			if (fieldValue instanceof Disposable) {
				((Disposable) fieldValue).dispose();
			} else if (fieldValue instanceof Lazy<?>) {
				final Lazy<?> lazyValue = (Lazy<?>) fieldValue;
				if (lazyValue.isInitialized() && lazyValue.get() instanceof Disposable) {
					((Disposable) lazyValue.get()).dispose();
				}
			} else if (fieldValue instanceof Iterable<?>) {
				for (final Object probableDisposable : (Iterable<?>) fieldValue) {
					if (probableDisposable instanceof Disposable) {
						((Disposable) probableDisposable).dispose();
					}
				}
			}
		} catch (final Throwable exception) {
			// Unable to dispose. Ignored - might have been already disposed and there is more important stuff
			// to do, like disposing the rest of fields.
			Gdx.app.log("WARN", "Unable to dispose field: " + field + " of component: " + component + ".",
					exception);
		}
	}

	@Override
	public boolean isForcingMainThread() {
		return false;
	}
}
