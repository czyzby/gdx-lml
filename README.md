#LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

##Sample project
See [gdx-lml-tests](http://github.com/czyzby/gdx-lml-tests).

##Maven artifact
To import LML with Gradle, add this dependency to your core:
```
    compile "com.github.czyzby:gdx-lml:0.5.$gdxVersion"
```
`$gdxVersion` can be replaced with 1.5.6.

If you want to use LML with GWT, you have to add this module to your GdxDefinition:
```
	<inherits name='com.github.czyzby.lml.GdxLml' />
```
Since LML is using some functionalities of Autumn, make sure to include Autumn module - see [Autumn GWT](http://github.com/czyzby/gdx-autumn-gwt). Also, if you're using reflected action with `ActionContainers` on GWT, don't forget to register them as reflected classes:
```
	<extend-configuration-property name="gdx.reflect.include" value="your.reflected.package" />
```

##Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843) and [example project](http://github.com/czyzby/gdx-lml-tests).

## What's new
0.4 -> 0.5:

- Changed window tags according to the new, refactored API in LibGDX 1.5.6. Now dialogs can also add children to the title table - see `DialogLmlParent` (`toTitleTable` will append tag children to the title table rather than the content).
- LML now uses [Autumn](http://github.com/czyzby/gdx-autumn) for method annotations, which are not normally supported by LibGDX reflection API. You can use `@ViewAction` annotation on `ActionContainer`s' methods to assign the method to a chosen ID, which can be referenced in template instead of method name. While requiring more work, this allows for method names refactoring without breaking the templates (as long as annotations are untouched). If you're planning on using this annotation on GWT, however, you have to register the container for Autumn reflection - see its documentation for more details: [Autumn GWT](http://github.com/czyzby/gdx-autumn-gwt). As long as you don't use the `@ViewAction` annotation, you don't have to worry about Autumn.
- `ActionContainer`s fields can be also referenced just like methods. If LML parser is unable to find method with the given ID/name, it will check if the class contains a field with that name - if it does, field's value converted to string will be returned instead of a method result. This, of course, should not be used for events (`onClick`, for example), but it will do just fine for titles, labels etc. This functionality, however, causes problems on - who would guess - GWT, as LibGDX reflection implementation does not seem to throw exceptions or return null for invalid field names - from what I can see, it returns broken objects that throw runtime exceptions on invocations. This is not a problem as long as you have only one action container or reference all action containers' methods with their prefixed ID (as in &someContainer.someMethod), but I can imagine it might get tedious and field extraction may be rarely used, so you can turn off field extraction by setting `Lml.EXTRACT_FIELDS_AS_METHODS` to false.
- Methods without parameters can now be referenced in templates. As you might guess, these are invoked without parameters instead of consuming the enclosing actor.
- Macros just got a lot less awkward to use thanks to named parameters and default values. Macro syntax stays more or less the same, but now you can reference only the chosen parameters (in any order) by proceeding them with their name and "=" (like any other tag argument). Quotations are escaped (and optional). First argument determines if the macro arguments are named - you cannot use both named and unnamed arguments, as it would be near to impossible to determine correct argument order. For example:

```
<@macro body;skeleton content title="Hello!" includeLabel=false>
    <window title=${title}>
        <@if ${includeLabel}>
             <label>Label!</label>
        </@if>
        ${content}
        ${content}
    </window>
</@macro>

```
The macro declaration above registers a new macro under `body` and `skeleton` names. Text between macro tags is injected twice as ${content} attribute. `title` argument defaults to `Hello!` if not given and `includeLabel` is false by default.

The general macro syntax is: `<@macro arrayOfNames contentArgument anyAmountOfNamedArgumentsWithOrWithoutDefaultValue> MACRO CONTENT </@macro>`. Macro names are generally separated by ; - these can also reference ranges and actions. (See LML forum post for more info.) All appearances of ${contentArgument} - any name of the argument can be chosen, of course. If only macro name is given, content argument defaults to ${MACRO}. Argument names are case-sensitive.

Some example invocations:

```
<@body/>
<!-- BECOMES: -->
<window title=Hello!>
</window>

<@body includeLabel=true />
<!-- BECOMES: -->
<window title=Hello!>
    <label>Label!</label>
</window>

<@skeleton includeLabel=true title="Hey!">
	<textbutton>My content.</textbutton>
</@skeleton>
<!-- BECOMES: -->
<window title=Hey!>
    <label>Label!</label>
    <textbutton>My content.</textbutton>
    <textbutton>My content.</textbutton>
</window>
```

As you can see - pretty simple and much less awkward than `<@body "" true />`, for example. Be careful with spaces though - macros are parsed a little differently and they are not huge fans of spaces in arguments, even escaped ones. Use i18n bundle texts instead (@).