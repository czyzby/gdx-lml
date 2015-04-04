package com.github.czyzby.tests.reflected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.czyzby.tests.Main;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.util.Lml;

public class UiActions implements ActionContainer {
	private final Main main;

	public UiActions(final Main main) {
		this.main = main;
	}

	// LML-referenced actions:
	// template.lml
	public void parseLml(final Actor actor) {
		try {
			main.getPlayground().clear();
			for (final Actor parsedActor : main.getParser().parse(main.getTemplateInput().getText())) {
				main.getPlayground().add(parsedActor).expand().fill();
			}
			main.getPlayground().invalidateHierarchy();
			main.getPlayground().pack();
		} catch (final Throwable exception) {
			onParsingError(exception);
		}
	}

	private void onParsingError(final Throwable exception) {
		Gdx.app.error("ERROR", "Error occured while parsing LML.", exception);
		main.getParser().fill(main.getStage(), Gdx.files.internal("view/macro/dialog.lml"));
		main.setErrorMessage((Label) main.getParser().getActorsMappedById().get("resultMessage"));
	}

	public void switchLml(final Actor actor) {
		try {
			final String documentPath = actor.getName(); // LML documents assigned as IDs in template.lml.
			main.getTemplateInput().setText(Gdx.files.internal(documentPath).readString());
			parseLml(actor);
		} catch (final Throwable exception) {
			onParsingError(exception);
		}
	}

	// dialog.lml
	public boolean onErrorDecline(final Dialog dialog) {
		main.getErrorMessage().setText("It's not like you have a choice.");
		// Returning true on dialog-attached action cancels hiding.
		return true;
	}

	public void onErrorApprove(final Dialog dialog) {
		main.getErrorMessage().setText("Thanks!");
	}

	// tooltip.lml
	public Actor createCustomTooltip(final TextButton parent) {
		// Actor parsed from the file will be returned as &createCustomTooltip result and put into a tooltip.
		return Lml.parser(main.getSkin()).build().parse(Gdx.files.internal("view/macro/tooltipChild.lml"))
				.first();
	}

	// bundleExample.lml
	public String thisInvokesAction(final Actor actor) {
		return "This is result of thisInvokesAction method.";
	}

	// tableExample.lml
	public void onTableButtonClick(final TextButton button) {
		((Label) main.getParser().getActorsMappedById().get("label")).setText("Random: " + MathUtils.random()
				+ ", slider: " + ((Slider) main.getParser().getActorsMappedById().get("slider")).getValue());
	}

	public Value customValue(final Actor actor) {
		// Returns a custom Value implementation that will determine the size of chosen widget. Thanks to this
		// value implementation, ScrollPane will grow/shrink both horizontally and vertically on resize.
		return new Value() {
			@Override
			public float get(final Actor context) {
				final float stageWidth =
						context.getStage() == null ? Gdx.graphics.getWidth() : context.getStage().getWidth();
				return stageWidth * 0.2f;
			}
		};
	}

	// actionsExample.lml
	public void onLabelCreate(final Label label) {
		// Invoked upon label creation; the exact invocation time is A) if widget is closed (<tag/>): after
		// fully creating widget, B) if widget is parental (<tag>...</tag>): after widget is initiated and the
		// tag children are parsed.
		label.setText("This label's text was set by:\nonLabelCreate method call in Main.");
	}

	public void onSliderChange(final Slider slider) {
		if (main.getParser().getActorsMappedById().containsKey("sliderValue")) {
			((Label) main.getParser().getActorsMappedById().get("sliderValue")).setText(String
					.valueOf((int) slider.getValue()));
		}
	}
}
