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

1.7 -> 1.8

- Added `<:random>` macro, which allows to choose a random value from the passed array.
- Added LML style sheets support. See wiki for more info.
- Added `<:style>` macro, which allows to set default values of tag attributes at runtime, similarly to LML style sheets.
- Added `<:importStyleSheet>` macro, which allows to import LML style sheet file from within LML templates.
- Added missing `programmaticChangeEvents` attribute to all button tags.
- Added support for `<container>` pad-related attributes, even when outside of `<table>` tag.
- `SelectBox` instances now can store any type of objects.

1.6 -> 1.7

- A new mechanism - isolation - was introduced through `<isolate>` tag. Isolated actors are parsed along with the rest of the templates, but are not immediately added to stage or they tag parents. This basically allows to create actors with LML without adding them to the stage - something that previously wasn't achievable with `fillStage` or `createView` methods. `ActorStorage` is an `Actor` extension that keeps track of a list of actors, but does not draw them in any way: this actor is used internally by isolation tags and can be injected if you want to access the list of its parsed children actors.
- `Lml#EXTRACT_UNANNOTATED_METHODS` setting added. When this is set to `false`, `ActionContainer` methods and fields that are **not** annotated with `@LmlAction` will not be available in LML templates. Defaults to `true`, but if you consequently annotate methods and fields that are accessed by LML actors, you should consider setting this value to `false`, as it will considerably speed up method look-up time.
- `DefaultLmlSyntax.INSTANCE` was removed. It was entirely unnecessary when using a different syntax, like `VisLmlSyntax`.
- `DefaultLmlSyntax` now extends `EmptyLmlSyntax`. `EmptyLmlSyntax` implements all `LmlSyntax` interface methods and manages all tag, attribute and macro mechanisms internally, but registers no tags or attributes on its own. This can be a very useful utility class if you want to manually choose which tags, macros and attributes should be supported.
- Added conditions evaluating in arguments, similarly to how you can evaluate equations `{=likeThis}`. Using this syntax: `{? condition ? onTrue : onFalse}`, you can process simple conditions anywhere in the code. See new condition syntax examples in `gdx-lml-tests` and `gdx-lml-vis-tests` projects.
- Added `:cell` macro. Adds an empty cell to the chosen table and allows to parse any cell attributes. Useful if you need to preserve a certain table layout, but do not want to create unnecessary mock-up actors with the sole purpose to fill a cell.

### Archive
Older change logs are available in `CHANGES.md` file.
