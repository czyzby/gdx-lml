#LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

##Examples
See [gdx-lml-tests](http://github.com/czyzby/gdx-lml-tests) for example uses of all available tags and macros.

###Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843), [example project](http://github.com/czyzby/gdx-lml-tests), [tutorial](https://github.com/czyzby/gdx-lml/wiki/Tutorial) and [old syntax page (work in progress)](https://github.com/czyzby/gdx-lml/wiki/Syntax).

##Extensions
[VisUI](https://github.com/kotcrab/VisEditor/wiki/VisUI) syntax support is available through [gdx-lml-vis library](https://github.com/czyzby/gdx-lml-vis). Additionally to replacing standard Scene2D actors with improved VisUI widgets, it offers full support for other VisUI features - including color picker, file chooser, other new widgets, listeners and validators.

## Dependencies
To import LML with `Gradle`, add this dependency to your core project:
```
    compile "com.github.czyzby:gdx-lml:1.5.$gdxVersion"
```
Currently supported LibGDX version is **1.9.2**.

If you want to use LML with GWT, you have to add this module to your `GdxDefinition`:
```
    <inherits name='com.github.czyzby.lml.GdxLml' />
```

## What's new
1.5.1.8.0 -> 1.5.1.9.2

- Now when parsing a string value, a single character will not be treated as bundle line, preference or action (etc.) - even if it matches `@`, `$` or any other functional character. So, `$` will be parsed to `"$"`, but `$$$` will still look for an action (mapped to `$$` key). This is a simple convenience for printing a single character - these cannot be properly used as property names or bundle lines (and so on) anyway, as at least 2 characters are required. Note that if you want to use multiple restricted characters, you can always use i18n bundles.
- *DTD* file generator. Now you can generate a *DTD* file based on your customized *LmlParser* with `Dtd` class.

1.4 -> 1.5

- `argument` macro added. Contrary to `assign` macro, this macro evaluates passed arguments. For example, `assign` macro would assign `@someBundleLine` to argument name, while `argument` value would convert it to `Actual bundle line value in .properties file.` and assign it to the argument. See `ArgumentLmlMacroTag` for more informations.
- Ranges now accept bundle lines, preferences and methods. For example, now you can customize range size with i18n bundle file: `range[@start,@end]`, provided that `.properties` file has numeric `start` and `end` lines.
- Equations (available through `if`, `while` and `calculate` macros) now parse bundle lines and preferences. For example, `<@if @someLine < 20>` will check if bundle line mapped to `someLine` is shorter than 20 characters in the current locale.
- Equation marker. Now equations can be used pretty much anywhere, using mechanism similar to arguments. Normally, you insert parser arguments `{likeThat}` - this will look for an argument named `likeThat` and replace the braces block with its value (or `null`). To use equation instead, add `=` character at the beginning. For example, `{=3+5}` will replace the block with `8`. Bundle lines, preferences and methods are also supported by these equations. Equation marker aims to be a simplified alternative to `calculate` macro. As usual: to change equation marker, extend `DefaultLmlSyntax` class and override appropriate method. See new equations example in `gdx-lml-tests`.

1.3 -> 1.4

- `PooledList` is now used instead of `LinkedList`, which should slightly speed up the parsing (as in: limit its garbage collection) thanks to cached nodes and iterators.

1.2 -> 1.3

- `ButtonGroup` support through specialized `Table` extension: `ButtonTable`. `<buttonTable>` tag works like a regular table (can have any children), except it adds all its direct `Button`-extending children to an internal `ButtonGroup` instance, validating their checked status. This allows to easily build groups of widgets with min and max amounts of checked buttons.
- `image` (with `icon` alias) attribute available for `ImageButton` and `ImageTextButton`. These attributes replace `imageUp` drawable in buttons' styles: if this is the only drawable in the style, it will be always drawn on the button. You can just use a single style without any icons and manage buttons' images through these LML attributes.

### Archive
Older change logs are available in `CHANGES.md` file.