package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.AbstractLmlAttributeParser;
import com.github.czyzby.lml.util.LmlSyntax;

/** Provides core method implementations for LmlParents.
 *
 * @author MJ */
public abstract class AbstractLmlParent<Widget extends Actor> extends AbstractLmlAttributeParser implements
		LmlParent<Widget>, LmlSyntax {
	private final String tagName;
	private final String id;
	protected final Widget actor;
	protected final LmlParent<?> parent;
	protected final boolean isTreeNode;
	protected final Tree.Node node;

	public AbstractLmlParent(final LmlTagData tagData, final Widget actor, final LmlParent<?> parent,
			final LmlParser parser) {
		this.tagName = tagData.getTagName();
		id = tagData.getId();
		this.actor = actor;
		this.parent = parent;
		isTreeNode = getTreeNodeProperty(tagData, actor, parser, parent);
		node = prepareNode(actor, isTreeNode);
	}

	private boolean getTreeNodeProperty(final LmlTagData tagData, final Widget actor, final LmlParser parser,
			LmlParent<?> parent) {
		final boolean treeNode = parseBoolean(tagData, TREE_NODE_ATTRIBUTE, parser, actor);
		if (treeNode) {
			while (!(parent instanceof TreeLmlParent)) {
				parent = parent.getParent();
				if (parent == null) {
					throwErrorIfStrict(parser, "Received treeNode attribute outside of Tree widget in tag: "
							+ tagData.getTagName() + ".");
					return false;
				}
			}
		}
		return treeNode;
	}

	private static Tree.Node prepareNode(final Actor actor, final boolean isTreeNode) {
		if (isTreeNode) {
			return new Tree.Node(actor);
		}
		return null;
	}

	@Override
	public String getTagName() {
		return tagName;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public LmlParent<?> getParent() {
		return parent;
	}

	@Override
	public Widget getActor() {
		return actor;
	}

	@Override
	public Tree.Node getNode() {
		return node;
	}

	@Override
	public void closeTag(final LmlParser parser) {
		if (isTreeNode) {
			if (parent instanceof TreeLmlParent) {
				((TreeLmlParent) parent).getActor().add(node);
			} else {
				parent.getNode().add(node);
			}
		}
		doOnTagClose(parser);
	}

	protected abstract void doOnTagClose(LmlParser parser);

	@Override
	public void handleChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (isTreeNode) {
			if (child != null
					&& (childTagData != null && !childTagData.containsAttribute(TREE_NODE_ATTRIBUTE) || childTagData == null)) {
				node.add(new Tree.Node(child));
			}
		} else {
			handleValidChild(child, childTagData, parser);
		}
	}

	protected abstract void handleValidChild(Actor child, LmlTagData childTagData, LmlParser parser);

	@Override
	public void handleDataBetweenTags(final String data, final LmlParser parser) {
		if (isDataNotEmpty(data)) {
			if (isTreeNode) {
				node.add(new Tree.Node(getLabelFromRawDataBetweenTags(data, parser)));
			} else {
				handleValidDataBetweenTags(data, parser);
			}
		}
	}

	@Override
	public boolean acceptCharacter(final char character) {
		return true;
	}

	/** @param data is validated and not null. Can be appended to the widget. */
	protected abstract void handleValidDataBetweenTags(String data, LmlParser parser);

	protected boolean isDataNotEmpty(final String data) {
		return data != null && data.length() > 0;
	}

	/** @param data will be converted to a label. */
	protected Actor getLabelFromRawDataBetweenTags(final String data, final LmlParser parser) {
		return new Label(parser.parseStringData(data, actor), parser.getSkin());
	}

	protected int parseAlignment(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser) {
		return super.parseAlignment(lmlTagData, attributeName, parser, actor);
	}

	protected boolean parseBoolean(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser) {
		return super.parseBoolean(lmlTagData, attributeName, parser, actor);
	}

	protected float parseFloat(final LmlTagData lmlTagData, final String attributeName, final LmlParser parser) {
		return super.parseFloat(lmlTagData, attributeName, parser, actor);
	}

	protected int parseInt(final LmlTagData lmlTagData, final String attributeName, final LmlParser parser) {
		return super.parseInt(lmlTagData, attributeName, parser, actor);
	}

	protected String parseString(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser) {
		return super.parseString(lmlTagData, attributeName, parser, actor);
	}
}
