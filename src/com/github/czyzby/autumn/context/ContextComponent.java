package com.github.czyzby.autumn.context;

/** Container object. Wraps around an object in context, containing its annotated data.
 *
 * @author MJ */
public class ContextComponent {
	private final Class<?> componentClass;
	private final Object component;

	private final boolean lazy;
	private final boolean keptInContext;

	private final int hashCode;

	private boolean initiated;
	private boolean mapped;

	public ContextComponent(final Class<?> componentClass, final Object component) {
		this(componentClass, component, false, true, false);
	}

	public ContextComponent(final Class<?> componentClass, final Object component, final boolean lazy,
			final boolean keptInContext) {
		this(componentClass, component, lazy, keptInContext, false);
	}

	public ContextComponent(final Class<?> componentClass, final Object component, final boolean lazy,
			final boolean keptInContext, final boolean initiated) {
		this.componentClass = componentClass;
		this.component = component;
		this.lazy = lazy;
		this.keptInContext = keptInContext;
		this.initiated = initiated;
		hashCode = computeHashCode();
	}

	private int computeHashCode() {
		return component.hashCode() ^ componentClass.hashCode();
	}

	/** @param withType expected class of the component.
	 * @return actual component instance wrapped by this object. */
	@SuppressWarnings("unchecked")
	public <ComponentType> ComponentType getComponent(final Class<ComponentType> withType) {
		return (ComponentType) component;
	}

	/** @return actual component instance wrapped by this object. */
	public Object getComponent() {
		return component;
	}

	/** @return class of the actual component. */
	public Class<?> getComponentClass() {
		return componentClass;
	}

	/** @return true if component's injected values and initial methods were invoked. */
	public boolean isInitiated() {
		return initiated;
	}

	/** @param initiated set to true after full initiation in context. */
	public void setInitiated(final boolean initiated) {
		this.initiated = initiated;
	}

	/** @return true if the component should be initiated on first access rather than at once. Annotated. */
	public boolean isLazy() {
		return lazy;
	}

	/** @return if false, destroying methods will be called just after initiation and component will never be
	 *         added to the context. Annotated. */
	public boolean isKeptInContext() {
		return keptInContext;
	}

	/** @return true if component is already mapped and available for extraction. */
	public boolean isMapped() {
		return mapped;
	}

	/** @param mapped true if component is already mapped and available for extraction. */
	public void setMapped(final boolean mapped) {
		this.mapped = mapped;
	}

	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof ContextComponent
				&& component.equals(((ContextComponent) object).component);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return componentClass + " : " + component;
	}
}
