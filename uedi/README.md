# UEDI for LibGDX

[Unsettlingly Easy Dependency Injection](https://github.com/czyzby/uedi) is a dependency injection framework (not surprisingly) which aims to be as *non-invasive* as possible. Instead of flooding your code with third-party annotations, you're free to use POJOs constructed or filled with some reflection magic.

`gdx-uedi` extension is basically a reimplementation of `uedi-core` library using LibGDX reflection wrappers instead of the official Java reflection API to support problematic platforms like GWT. If you don't care about GWT and want to use UEDI at its finest, don't bother with this library. If you want to make sure that it works on every platform, this might be the only way.

### Limitations

LibGDX reflection API misses method and constructor parameter names, so no parameter-aware context is provided (even for Java 8+).

Also, there is no reliable way of listing implemented interfaces of classes at runtime, so - contrary to `uedi-core` - LibGDX UEDI context maps components **only** by their class tree: you generally cannot inject components by their interfaces, unless explicitly mapping providers to interfaces. So, for example, if there is an `ArrayList` provider in the context, `uedi-core` would be able to inject an `ArrayList` into a `List` field; `gdx-uedi` would not. However, both would be able to inject `ArrayList` into an `AbstractList` field, as `AbstractList` is not an interface.

## Dependencies

To use UEDI with `Gradle`, add this dependency to your core project:
```
        compile "com.github.czyzby:gdx-uedi:$libVersion.$gdxVersion"
```

`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

If you want to use UEDI with GWT, you have to add this module to your `GdxDefinition`:
```
        <inherits name='com.github.czyzby.uedi.GdxUedi' />
```

[Standard UEDI scanners](https://github.com/czyzby/uedi) are fully compatible with `gdx-uedi`, so choose the ones appropriate for each platform. Note that the fastest way to set up UEDI application is using the unofficial [gdx-setup](https://github.com/czyzby/gdx-setup).

See [LML UEDI](../lml-uedi) for a LibGDX framework built around UEDI and [LML](../lml) (*LibGDX Markup Language*).
