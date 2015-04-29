package com.github.czyzby.autumn.mvc.component.ui.dto.provider;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

/** Wraps around an {@link com.github.czyzby.lml.parser.impl.dto.ActionContainer}.
 *
 * @author MJ */
public class ActionContainerViewActionProvider extends AbstractViewActionProvider {
	private final String id;
	private final ActionContainer actionContainer;

	public ActionContainerViewActionProvider(final String id, final ActionContainer actionContainer,
			final String[] viewIds) {
		super(viewIds);
		this.id = id;
		this.actionContainer = actionContainer;
	}

	@Override
	protected void register(final LmlParser parser) {
		parser.addActionContainer(id, actionContainer);
	}

	@Override
	protected void unregister(final LmlParser parser) {
		parser.removeActionContainer(id);
	}
}