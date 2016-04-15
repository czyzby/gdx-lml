# LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

## Examples
See [gdx-lml-tests](../examples/gdx-lml-tests) for example uses of all available tags and macros.

Check it out [on-line](http://czyzby.github.io/gdx-lml/lml).

### Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843), [example project](../examples/gdx-lml-tests), [tutorial](https://github.com/czyzby/gdx-lml/wiki/LibGDX-Markup-Language) and [DTD schema section](dtd).

## Extensions
[VisUI](https://github.com/kotcrab/VisEditor/wiki/VisUI) syntax support is available through [gdx-lml-vis library](../lml-vis). Additionally to replacing standard Scene2D actors with improved VisUI widgets, it offers full support for other VisUI features - including color picker, file chooser, other new widgets, listeners and validators.

Check it out [on-line](http://czyzby.github.io/gdx-lml/lml-vis).

## Dependencies

`gdx-lml` is available through the official project creator tool: `gdx-setup` (in third party extensions). However, its version might not be up to date.

To import LML with `Gradle`, add this dependency to your core project:
```
        compile "com.github.czyzby:gdx-lml:$libVersion.$gdxVersion"
```

`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

If you want to use LML with GWT, you have to add this module to your `GdxDefinition`:
```
        <inherits name='com.github.czyzby.lml.GdxLml' />
```

## What's new

1.5 -> 1.6

- Macro marker was changed from `@` to `:`. While it required to switch a single character in the actual source code, this is actually a major update. This change breaks all LML templates that used any macro tags. `@` was a poor choice in the first place: it can be confused with the i18n bundle marker (also `@`) and is not a valid XML character. Since `DTD` supported was added, invalid macro sign was no longer an option. Quick conversion tip: replace `<@` and `</@` with `<:` and `</:` in all `*.lml` files.
- Removed `/*` alias for comment macro. Since `DTD` creator was added to LML, now it is possible to create templates that are somewhat-valid `XML` files. `/*` was the only default tag that used forbidden `XML` characters.
- New actor: `AnimatedImage`. Extends the regular `Image`; manages an array of drawables updated on `act` method. Allows to display a set of images in the specified way. Available through `animatedImage` tag.
- Proper `<!DOCTYPE>` tag parsing according to the XML standards. Added helper methods: `LmlSyntax#getDocumentTypeOpening()`, `LmlTemplateReader#startsWith(CharSequence)`. These interface modifications will not affect your applications unless you modified or extended LML internals.
- Major internal API change: `AbstractLmlTag` now consumes `StringBuilder` instead of `String` in its constructor. If you had any custom tags created in Java, this change will break your classes. The good news: using `StringBuilders` should slightly limit the amount of created objects (less garbage to collect!).
- Proper attribute parsing. To use a space character in attributes before you had to escape it with `\`. Valid attributes with whitespaces - like `message="You have died!"` would have to look as awkward as this: `message="You\ have\ died!"`, otherwise parser would separate it into 3 parts: `message="You, have, died!"`. Now whitespace escaping is completely removed: instead, all you have to do is use quotation.
- Most macros now support named parameters: conditional macros support `test`, import macros - `path`, `replace`, logging macros - `log`, loop macro: `times`, assignment macros - `key`, `value`, evaluation macros - `method`, `id`, exception macro - `message`, `strict`. DTD creator will now extract expected macro and put them in the file. This does *not* break existing templates, as named parameters are optional. This change was made to make LML more XML-friendly.
- Added `Dtd#saveSchema` and `Dtd#saveMinifiedSchema` methods, which should be preferred over `getSchema`.
- Generated DTD files are now valid and should be properly recognized by most IDEs.
- `progressBar` and `slider` tags, previously completely non-parental, can parse text between their tags - provided that it's a valid float. The parsed number will be set their initial value. Note that it's *not* a simple `value` attribute alias: `value` attribute is parsed *before* the actor is created, so it cannot trigger any registered change listeners. On the other hand, data between tags is parsed *after* actor is created (and has processed its attributes), so it *can* trigger the listeners.
- Added `onClick`, `onChange`, `onInput` tags and `:onClick`, `:onChange`, `:onInput` macros that allow to attach specialized listeners to actors. When the event occurs, children actor of the listener tags are added to the stage. Useful for displaying dialogs. See Javadocs or updated `gdx-lml-tests` and `gdx-lml-vis-tests` for more informations and usage examples.
- Added `LmlApplicationListener` - an abstract `ApplicationListener` implementation that manages a set of `AbstractLmlViews`. What LibGDX `Game` is to `Screen`, this is the same thing to `AbstractLmlView`. Includes support for smooth view transitions and registers some default actions available in all templates. Highly customizable. See updated `gdx-lml-vis-websocket` example project that uses this utility class.
- Expanded `AbstractLmlView` API with optional methods: `getTemplateFile(), pause(), resume(), hide(), show(), clear()`. By default, they do nothing (well, one of them returns `null`).
- To keep less meta-data and make DTD smaller, multiple aliases were removed. Thanks to this change, LML creates and keeps less objects and the DTD schemas are now acceptable. Note that this is likely to break your LML templates, but you can prevent that by using `lmlParser.getSyntax().addTagProvider(new SomeActorLmlTagProvider(), "yourFavoriteAlias");` or `lmlParser.getSyntax().addMacroTagProvider(new SomeMacroLmlMacroTagProvider(), "yourFavoriteMacroAlias");`. While I understand that some aliases were useful and less verbose than class names, LML aims to keep as little data at runtime as possible and now it is even more important with DTD around. See `DefaultLmlSyntax` for the leftover aliases. A few unmentioned attribute obscure aliases were removed. Now each actor is mapped to tag name that represents it's class name. Removed the following aliases:
  - Actor:  `"empty", "mock", "blank", "placeholder"`. Both `"actor"` and `"group`` are still available. 
  - CheckBox: `"check"`.
  - Container: `"single"`.
  - Dialog: `"popup"`.
  - HorizontalGroup: `"horizontal"`.
  - Image: `"img", "icon"`.
  - Label: `"text", "txt", "li"`.
  - List: `"ul"`.
  - ProgressBar: `"progress", "loadingBar", "loading"`.
  - ScrollPane: `"scroll", "scrollable"`.
  - SelectBox: `"select"`.
  - SplitPane: `"split", "splitable"`.
  - Table: `"div", "td", "th", "tr"`.
  - TextArea: `"inputArea", "multilineInput"`.
  - TextButton: `"a"`.
  - TextField: `"input", "textInput"`.
  - Touchpad: `"touch"`.
  - Tree: `"root"`.
  - VerticalGroup: `"vertical"`.
  - Actor macro: `"widget"`.
  - Any not null macro: `"anyExists", "anyPresent"`.
  - Argument macro: `"i18n", "bundle"`.
  - Argument replace macro: `"replaceArgs", "argumentsReplace", "argsReplace", "noOperation", "doNothing"`.
  - Assign macro: `"toArgument"`.
  - Calculation macro: `"equation", "calc"`.
  - Conditional (if) macro: `"try", "verify", "inspect", "validate", "onCondition", "condition", "conditional"`.
  - Evaluation macro: `"invokeAndAssign", "evaluateAndAssign"`.
  - Exception macro: `"throwException", "throwError", "system.exit"`. (It still hurts.)
  - For each macro: `"iterate", "iterateOver"`.
  - Absolute import macro: `"absoluteInclude", "absoluteRequire", "absoluteTemplate"`.
  - Classpath import macro: `"classpathInclude", "classpathRequire", "classpathTemplate"`.
  - External import macro: `"externalInclude", "externalRequire", "externalTemplate"`.
  - Internal import macro: `"include", "require", "template", "internalInclude", "internalRequire", "internalTemplate"`.
  - Local import macro: `"localInclude", "localRequire", "localTemplate"`.
  - Loop macro: `"repeat"`.
  - For each nested macro: `"nestedForEach", "nest", "nestedLoop"`.
  - New attribute macro: `"createAttribute", "newProperty"`.
  - New tag macro: `"createTag", "newActor"`.
  - Null check macro: `"allNotNull", "ifPresent", "ifExists", "nullCheck", "ifTrue"`.
  - Table column macro: `"columnDefaults"`.
  - Table row macro: `"addRow", "newRow", "nextLine", "nextRow", "tr", "rowDefaults"`.
  - Conditional loop (while) macro: `"whileTrue", "repeatWhile", "untilTrue"`.
- Added `LmlParserListener` interface that allows you to hook up into `LmlParser` events. You can register this simple functional interface to listen to pre- or post-parsing events (or both).
- Added missing `LmlTemplateReader#append(CharSequence, String)` method. Every other significant appending method had its named equivalent - now `CharSequence` variant has it as well. Limited avoidable `String` creations in macros by using `CharSequences` (where possible). Some utility methods in internal abstract macros were refactored to return/consume `CharSequences` instead of `Strings` - this is unlikely to affect your application's code, though.

### Archive
Older change logs are available in `CHANGES.md` file.
