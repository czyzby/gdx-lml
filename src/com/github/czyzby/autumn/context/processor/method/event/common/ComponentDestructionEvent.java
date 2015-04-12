package com.github.czyzby.autumn.context.processor.method.event.common;

import com.github.czyzby.autumn.context.ContextComponent;

/** Posted when components are destroyed. Invokes methods annotated with Destroy annotation and disposing of
 * fields annotated with Dispose.
 *
 * @author MJ */
public class ComponentDestructionEvent {
	private final Iterable<ContextComponent> componentsToDestroy;

	public ComponentDestructionEvent(final Iterable<ContextComponent> componentsToDestroy) {
		this.componentsToDestroy = componentsToDestroy;
	}

	public Iterable<ContextComponent> getComponentsToDestroy() {
		return componentsToDestroy;
	}
}
