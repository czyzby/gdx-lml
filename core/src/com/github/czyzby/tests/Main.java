package com.github.czyzby.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.tests.reflected.UiActions;

public class Main extends AbstractApplicationListener {
	private static final Array<String> TEMPLATES = ImmutableArray.ofSorted("assign", "bundle", "comment",
			"condition", "forEach", "include", "loop", "nullCheck", "tooltip", "macro", "label", "table",
			"tree", "groups", "actions", "dialog", "customAttribute", "namedParameters", "evaluate");

	private LmlParser parser;
	private Stage stage;
	private Skin skin;

	private Table playground;
	private TextArea templateInput;
	private Label errorMessage;

	@Override
	public void create() {
		stage = new Stage(new ScreenViewport());

		skin = new Skin(Gdx.files.internal("ui/ui.json"));
		final I18NBundle i18nBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/bundle"));
		final I18NBundle customBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/custom"));
		final Preferences preferences = preparePreferences();
		final Preferences customPreferences = prepareCustomPreferences();

		parser =
				Lml.parser().skin(skin).i18nBundle(i18nBundle).preferences(preferences)
						.actionContainer("main", new UiActions(this)).arguments(getTemplateArguments())
						.action("onButtonClick", getOnButtonClickActorConsumer())
						.arguments(getExamplesArguments()).i18nBundle("custom", customBundle)
						.preferences("custom", customPreferences)
						.attributeParser("label", getCustomLabelAttributeParser()).build();
		parser.fill(stage, Gdx.files.internal("template.lml"));

		final ObjectMap<String, Actor> actorsByIds = parser.getActorsMappedById();
		// Note: widgets could be also assigned with onCreate action.
		templateInput = (TextArea) actorsByIds.get("templateInput");
		playground = (Table) actorsByIds.get("playground");

		Gdx.input.setInputProcessor(stage);
	}

	private ObjectMap<String, String> getExamplesArguments() {
		return GdxMaps.newObjectMap(
				// nullCheckExample.lml:
				"validParameter", Lml.toBundleLine("validParameter"),
				// conditionExample.lml:
				"checkedArgument", "7.2",
				// forEachExample.lml:
				"arrayParameter", Lml.toArrayArgument("Param1", "Param2"), "anotherArray",
				Lml.toArrayArgument("Another1", "Another2", "Another3"),
				// bundleExample.lml:
				"argumentFromParser", "Third", "thisIsAnArgument", "This is an argument's value.");
	}

	private Preferences preparePreferences() {
		final Preferences preferences = Gdx.app.getPreferences("lml.test.properties");
		preferences.putString("buttonRowSize", "2"); // template.lml
		preferences.putString("thisIsAPreference", "This is a text from preference."); // bundleExample.lml
		preferences.putString("fromPreferences", "Text from preference."); // customAttributeExample.lml
		preferences.flush();
		return preferences;
	}

	private Preferences prepareCustomPreferences() {
		final Preferences preferences = Gdx.app.getPreferences("lml.test.custom.properties");
		preferences.putString("thisIsAPreference",
				"This text is from preferences\nreferenced with \"custom\" key."); // bundleExample.lml
		preferences.flush();
		return preferences;
	}

	private ObjectMap<String, String> getTemplateArguments() {
		final FileHandle templatesDirectory = Gdx.files.internal("view");
		final Array<String> templates = GdxArrays.newArray();
		final Array<String> templateBundleNames = GdxArrays.newArray();
		for (final String template : TEMPLATES) {
			templates.add(templatesDirectory.child(template + "Example.lml").path());
			templateBundleNames.add(Lml.toBundleLine(template + "Example"));
		}
		return GdxMaps.newObjectMap("documents", Lml.toArrayArgument(templates), "documentButtons",
				Lml.toArrayArgument(templateBundleNames));
	}

	@Override
	public void resize(final int width, final int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(final float deltaTime) {
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void dispose() {
		parser.setDefaultPreferences(null);
		Disposables.disposeOf(stage, skin);
	}

	// customAttributeExample.lml - defining custom attribute:
	private LmlTagAttributeParser getCustomLabelAttributeParser() {
		return new LmlTagAttributeParser() {
			private final String[] attributeNames = new String[] { "onHover" };

			@Override
			public String[] getAttributeNames() {
				return attributeNames;
			}

			@Override
			public void apply(final Object actor, final LmlParser parser, final String attributeValue,
					final LmlTagData lmlTagData) {
				// This attribute takes two strings separated by ; and adds a listener to label that changes
				// its text on hover according to passed values.
				final Label label = (Label) actor;
				final String[] values = attributeValue.split(";");
				// LmlAttributes contain utilities for attribute parsing. For example, using parseString gives
				// you i18n bundle (@ prefix), preference (#) and actions (&) support.
				label.addListener(getListener(label, LmlAttributes.parseString(label, parser, values[0]),
						LmlAttributes.parseString(label, parser, values[1])));
			}

			private EventListener getListener(final Label label, final String onHover, final String onExit) {
				return new ClickListener() {
					@Override
					public void enter(final InputEvent event, final float x, final float y,
							final int pointer, final Actor fromActor) {
						super.enter(event, x, y, pointer, fromActor);
						label.setText(onHover);
					}

					@Override
					public void exit(final InputEvent event, final float x, final float y, final int pointer,
							final Actor toActor) {
						super.exit(event, x, y, pointer, toActor);
						label.setText(onExit);
					}
				};
			}
		};
	}

	// For reflection-based actions, see UiActions.
	private ActorConsumer<Void, TextButton> getOnButtonClickActorConsumer() {
		// Action registered without reflection.
		return new ActorConsumer<Void, TextButton>() {
			@Override
			public Void consume(final TextButton actor) {
				if (actor.getUserObject() == null || !(actor.getUserObject() instanceof Integer)) {
					actor.setUserObject(0);
				}
				actor.setUserObject(((Integer) actor.getUserObject()).intValue() + 1);
				actor.setText("I was clicked " + actor.getUserObject() + " times.");
				return null;
			}
		};
	}

	public Skin getSkin() {
		return skin;
	}

	public Stage getStage() {
		return stage;
	}

	public LmlParser getParser() {
		return parser;
	}

	public Table getPlayground() {
		return playground;
	}

	public TextArea getTemplateInput() {
		return templateInput;
	}

	public Label getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final Label errorMessage) {
		this.errorMessage = errorMessage;
	}
}
