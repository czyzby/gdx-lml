package com.github.czyzby.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.TextLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.vis.util.ColorPickerContainer;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.tests.reflected.CustomActionContainer;
import com.github.czyzby.tests.reflected.MainView;
import com.github.czyzby.tests.reflected.widgets.BlinkingLabel;
import com.github.czyzby.tests.reflected.widgets.CodeTextArea.CodeTextAreaLmlTagProvider;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle;

/** Main application's listener. Manages {@link MainView} instance.
 *
 * @author MJ
 * @author Kotcrab */
public class Main extends AbstractApplicationListener {
    private static final Array<String> EXAMPLES = ImmutableArray.of(
            // Tags:
            "actor", "animatedImage", "button", "buttonGroup", "checkBox", "container", "dialog", "horizontalGroup",
            "imageButton", "image", "imageTextButton", "label", "list", "progressBar", "scrollPane", "selectBox",
            "slider", "splitPane", "stack", "table", "textArea", "textButton", "textField", "tooltip", "touchpad",
            "tree", "verticalGroup", "window",
            // Syntax:
            "i18n", "preferences", "arguments", "actions", "arrays", "equations",
            // Macros:
            "actorMacro", "anyNotNull", "argument", "argumentReplace", "assign", "calculate", "comment", "conditional",
            "evaluate", "exception", "forEach", "import", "loop", "logger", "meta", "nestedForEach", "newAttribute",
            "newTag", "nullCheck", "tableColumn", "tableRow", "while",
            // Custom elements:
            "customAttribute", "customMacro", "customTag",
            // Vis unique tags:
            "vis/basicColorPicker", "vis/collapsibleWidget", "vis/colorPicker", "vis/draggable", "vis/dragPane",
            "vis/fixedSizeGridGroup", "vis/formValidator", "vis/gridGroup", "vis/horizontalFlow", "vis/linkLabel",
            "vis/listView", "vis/menu", "vis/numberSelector", "vis/tabbedPane", "vis/radioButton", "vis/separator",
            "vis/tooltip", "vis/validatableTextField", "vis/verticalFlow");
    private static final String MAIN_VIEW_TEMPLATE = "templates/main.lml";

    private MainView view;

    @Override
    public void create() {
        // Turning on all logging levels for logging macro examples:
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Creating LML parser:
        final LmlParser parser = constructParser();
        // Thanks to using createView(Class, FileHandle) method, an instance of MainView will be created using no-arg
        // constructor. Its stage - retrieved by getStage() method - will be filled with root actors in the template.
        // Its annotated fields will be filled.
        view = parser.createView(MainView.class, Gdx.files.internal(MAIN_VIEW_TEMPLATE));
        // Focusing input on view's stage:
        Gdx.input.setInputProcessor(view.getStage());
    }

    /** @return a new fully constructed instance of LML parser. */
    private static LmlParser constructParser() {
        VisUI.load();
        createCodeTextAreaStyle();
        return VisLml.parser().i18nBundle(getDefaultI18nBundle()).preferences(getDefaultPreferences())
                .i18nBundle("custom", getCustomI18nBundle()).preferences("custom", getCustomPreferences())
                // Custom text area used to display LML code of current example:
                .tag(new CodeTextAreaLmlTagProvider(), "codeTextArea")
                // {examples} argument will allow to iterate over Main#EXAMPLES array in LML templates:
                .argument("examples", EXAMPLES)
                // templates/examples/arguments.lml:
                .argument("someArgument", "Extracted from argument.").argument("someNumber", 50)
                .argument("lmlSnippet", "<label>Created with argument.</label>")
                // templates/examples/actions.lml:
                .action("labelConsumer", getActorConsumer()).actions("custom", CustomActionContainer.class)
                // Custom tags, attributes and macros created in Java:
                .attribute(getCustomAttribute(), "href", "showLink") // Custom attribute - opens a link.
                .macro(getCustomMacroProvider(), "uppercase", "toUppercase") // Custom macro - converts to upper case.
                .tag(getCustomTagProvider(), "blink", "blinkingLabel") // Custom tag - creates a blinking label.
                .attribute(getCustomBlinkingLabelAttribute(), "blinkingTime") // Additional blinking label's attribute.
                // Preparing the parser:
                .build();
    }

    private static void createCodeTextAreaStyle() {
        final Skin skin = VisUI.getSkin();

        // VisUI doesn't have font that would be good showing for source code so we load and add it to skin manually
        final BitmapFont hackFont = new BitmapFont(Gdx.files.internal("fonts/hackFont.fnt"));
        skin.add("hack-font", hackFont, BitmapFont.class);

        // Clone default VisTextField style and change its font to our just loaded hack font then add it to skin
        final VisTextFieldStyle codeStyle = new VisTextFieldStyle(VisUI.getSkin().get(VisTextFieldStyle.class));
        codeStyle.font = hackFont;
        skin.add("source-code", codeStyle, VisTextFieldStyle.class);
    }

    // This bundle will be available through "default" ID and used if no ID is specified.
    private static I18NBundle getDefaultI18nBundle() {
        return I18NBundle.createBundle(Gdx.files.internal("i18n/bundle"));
    }

    // This bundle will be available through "custom" ID.
    private static I18NBundle getCustomI18nBundle() {
        return I18NBundle.createBundle(Gdx.files.internal("i18n/custom"));
    }

    // These preferences will be available through "default" ID and used if no ID is specified.
    private static Preferences getDefaultPreferences() {
        final Preferences preferences = Gdx.app.getPreferences("lml.test.preferences");
        preferences.putString("somePreference", "Extracted from default preferences.");
        preferences.putString("someNumber", "200"); // Added as string, as other types are problematic on GWT.
        return preferences;
    }

    // These preferences will be available through "custom" ID.
    private static Preferences getCustomPreferences() {
        final Preferences preferences = Gdx.app.getPreferences("lml.test.custom.preferences");
        preferences.putString("somePreference", "Extracted from custom preferences.");
        return preferences;
    }

    // Available as LML method through "labelConsumer" ID.
    private static ActorConsumer<?, ?> getActorConsumer() {
        return new ActorConsumer<String, Label>() {
            @Override
            public String consume(final Label actor) {
                actor.setText("Customized with actor consumer.");
                actor.setColor(Color.CYAN);
                return Strings.EMPTY_STRING;
            }
        };
    }

    // Will be available as attribute for all tags under "href" and "showLink" names. Displays a link
    private static LmlAttribute<Actor> getCustomAttribute() {
        return new LmlAttribute<Actor>() {
            @Override
            public Class<Actor> getHandledType() {
                return Actor.class;
            }

            @Override
            public void process(final LmlParser parser, final LmlTag tag, final Actor actor,
                    final String rawAttributeData) {
                // By using LmlParser#parseString(String, Object) method rather than using rawAttributeData directly, we
                // add i18n, preferences and methods support. So yeah, it's worth the extra code.
                final String url = parser.parseString(rawAttributeData, actor);
                actor.addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        // Opens browser with the selected URL:
                        Gdx.net.openURI(url);
                    }
                });
            }
        };
    }

    // Provides tags of a custom macro available under "uppercase", "toUppercase" names.
    private static LmlTagProvider getCustomMacroProvider() {
        // LmlTagProvider is a simple functional interface that provides LmlTag instances.
        return new LmlTagProvider() {
            @Override
            public LmlTag create(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
                return getCustomMacro(parser, parentTag, rawTagData);
            }
        };
    }

    // Changes text between tags into upper case.
    private static LmlTag getCustomMacro(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        return new AbstractMacroLmlTag(parser, parentTag, rawTagData) {
            @Override
            public void handleDataBetweenTags(final String rawData) {
                // appendTextToParse(String) method forces the template reader to parse passed text. This is the default
                // method to add macro result. In this case, we want to change text that the macro received between tags
                // to upper case.
                appendTextToParse(rawData.toUpperCase());
            }
        };
    }

    // Provides tags of a custom actor: overridden label that blinks. Available under "blink", "blinkingLabel".
    private static LmlTagProvider getCustomTagProvider() {
        return new LmlTagProvider() {
            @Override
            public LmlTag create(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
                return getCustomTag(parser, parentTag, rawTagData);
            }
        };
    }

    // Creates a custom Label that blinks.
    private static LmlTag getCustomTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        return new AbstractActorLmlTag(parser, parentTag, rawTagData) {
            @Override
            protected LmlActorBuilder getNewInstanceOfBuilder() {
                // Normally you don't have to override this method, but we want to support String constructor, so we
                // supply one of default, extended builders:
                return new TextLmlActorBuilder();
                // By using this builder, we're automatically support "text", "txt" and "value" attributes, which will
                // use #setText(String) method to modify the builder.
            }

            @Override
            protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
                // Safe to cast builder. Always the same object type as returned by getNewInstanceOfBuilder:
                final TextLmlActorBuilder textBuilder = (TextLmlActorBuilder) builder;
                return new BlinkingLabel(textBuilder.getText(), getSkin(builder), builder.getStyleName());
            }

            @Override
            protected void handlePlainTextLine(final String plainTextLine) {
                final BlinkingLabel label = (BlinkingLabel) getActor();
                // By using LmlParser#parseString, we add i18n, preferences and methods support.
                label.setText(label.getText() + getParser().parseString(plainTextLine, label));
            }

            @Override
            protected void handleValidChild(final LmlTag childTag) {
                getParser().throwErrorIfStrict("Labels cannot have children. Even the blinking ones.");
                // Appending children is pretty easy. If label was a Group, you could just replace the exception with:
                // ((Group) getActor()).addActor(childTag.getActor());
                // Also, you can use AbstractGroupLmlTag which already handles children for you.
            }
        };
    }

    // Allows to set blinking time of blinking labels - our custom widget. This attribute will be available only for
    // widgets that extend BlinkingLabel class.
    private static LmlAttribute<BlinkingLabel> getCustomBlinkingLabelAttribute() {
        return new LmlAttribute<BlinkingLabel>() {
            @Override
            public Class<BlinkingLabel> getHandledType() {
                return BlinkingLabel.class;
            }

            @Override
            public void process(final LmlParser parser, final LmlTag tag, final BlinkingLabel actor,
                    final String rawAttributeData) {
                actor.setBlinkingTime(parser.parseFloat(rawAttributeData, actor));
            }
        };
    }

    @Override
    protected void render(final float deltaTime) {
        // This is a utility method from AbstractLmlView that updates and draws the stage:
        view.render(deltaTime);
    }

    @Override
    public void resize(final int width, final int height) {
        view.resize(width, height, true); // Resizes stage (width, height) and centers camera ("true" argument).
    }

    @Override
    public void dispose() {
        Disposables.disposeOf(view); // Null-safe disposing utility from gdx-kiwi.
        // ColorPicker is a heavy widget which should be reused and disposed of. Custom LML attributes reuse a single
        // ColorPicker instance which should be manually destroyed when the dialog is no longer expected to be shown:
        ColorPickerContainer.dispose();
    }
}
