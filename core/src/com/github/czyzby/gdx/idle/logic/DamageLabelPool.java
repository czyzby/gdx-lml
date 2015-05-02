package com.github.czyzby.gdx.idle.logic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.github.czyzby.autumn.annotation.method.Initiate;
import com.github.czyzby.autumn.annotation.stereotype.Component;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;

// A simple, custom pool. LibGDX pool would probably do better, but this was faster for me to set up.
@Component
public class DamageLabelPool {
	private Label damagePrototype;
	private Label healingPrototype;
	private StringBuilder textBuilder = new StringBuilder();
	private LinkedList<Label> queue = new LinkedList<Label>();
	private ObjectMap<Label, Runnable> returningActions = GdxMaps.newObjectMap();

	@Initiate
	private void buildPrototypes(final InterfaceService interfaceService) {
		final LmlParser parser = interfaceService.getParser();
		parser.parse(Gdx.files.internal("templates/prototypes/labels.lml"));
		final ObjectMap<String, Actor> labels = parser.getActorsMappedById();
		damagePrototype = (Label) labels.get("damage");
		healingPrototype = (Label) labels.get("healing");
	}

	public void attachLabel(final int withValue, final Image toImage, final boolean heal) {
		Strings.clearBuilder(textBuilder);
		textBuilder.append(withValue);
		final Label label = getLabel();
		label.setText(textBuilder);
		if (heal) {
			label.setStyle(healingPrototype.getStyle());
		} else {
			label.setStyle(damagePrototype.getStyle());
		}
		// I know this is ugly, but I REALLY didn't want to focus on the logic.
		label.addAction(Actions.sequence(Actions.moveBy(8f, 32f, 1f, Interpolation.bounceOut),
				Actions.run(returningActions.get(label)), Actions.removeActor()));
		label.setPosition(
				toImage.getX() + toImage.getParent().getX()
						+ MathUtils.random(toImage.getWidth() * 0.25f, toImage.getWidth() * 0.75f),
				toImage.getY() + toImage.getParent().getY() + toImage.getHeight() * 0.75f);

		toImage.getStage().addActor(label);
	}

	private Label getLabel() {
		if (queue.isEmpty()) {
			final Label label = new Label(textBuilder, damagePrototype.getStyle());
			returningActions.put(label, new Runnable() {
				@Override
				public void run() {
					queue.add(label);
				}
			});
			return label;
		}
		return queue.poll();
	}
}