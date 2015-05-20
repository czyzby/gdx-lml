#LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

##Sample project
See [gdx-lml-tests](http://github.com/czyzby/gdx-lml-tests).

##Maven artifact
To import LML with Gradle, add this dependency to your core:
```
    compile "com.github.czyzby:gdx-lml:0.6.$gdxVersion"
```
Currently supported LibGDX version is **1.6.0**.

If you want to use LML with GWT, you have to add this module to your GdxDefinition:
```
	<inherits name='com.github.czyzby.lml.GdxLml' />
```

##Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843), [example project](http://github.com/czyzby/gdx-lml-tests) and [Wiki syntax page (work in progress)](https://github.com/czyzby/gdx-lml/wiki/Syntax).


## What's new
0.6 -> 0.7:

- Wiki page with all tags and attributes is being created. Stay tuned.
- Added "disabled" attribute to ProgressBar.
- @ViewAction now takes an array of strings as action IDs. This won't break existing code, while allowing to map the selected method by multiple IDs.
- New common tag attribute: userObject. Allows to assign a custom object to the actor by using Actor#setUserObject method. If action operator is used (&), method will be found and executed - its result will be assigned as the user object; in other cases, string value of the attribute is set. Be careful - some widgets (Dialogs, Windows) use the same mechanism to store stage attachment data; however, this attribute should be safe to use for actors that are not roots (as in not directly added to stage). 

0.5 -> 0.6:

- LML drops Autumn dependency thanks to method annotation API in LibGDX 1.6.0. Now ActionContainers using @ViewAction annotation need to be GWT-reflected in a standard LibGDX way and Autumn is no longer needed.
- onComplete attribute added to ProgressBar (and Slider). This expects an action which will be invoked once the progress bar reaches 100%.

0.4 -> 0.5:

- Changed window tags according to the new, refactored API in LibGDX 1.5.6. Now dialogs can also add children to the title table - see `DialogLmlParent` (`toTitleTable` will append tag children to the title table rather than the content).
- LML now uses [Autumn](http://github.com/czyzby/gdx-autumn) for method annotations (NOTE: Autumn dependency removed in 0.6), which are not normally supported by LibGDX reflection API. You can use `@ViewAction` annotation on `ActionContainer`s' methods to assign the method to a chosen ID, which can be referenced in template instead of method name. While requiring more work, this allows for method names refactoring without breaking the templates (as long as annotations are untouched). If you're planning on using this annotation on GWT, however, you have to register the container for Autumn reflection - see its documentation for more details: [Autumn GWT](http://github.com/czyzby/gdx-autumn-gwt). As long as you don't use the `@ViewAction` annotation, you don't have to worry about Autumn.
- `ActionContainer`s fields can be also referenced just like methods. If LML parser is unable to find method with the given ID/name, it will check if the class contains a field with that name - if it does, field's value converted to string will be returned instead of a method result. This, of course, should not be used for events (`onClick`, for example), but it will do just fine for titles, labels etc. This functionality, however, causes problems on - who would guess - GWT, as LibGDX reflection implementation does not seem to throw exceptions or return null for invalid field names - from what I can see, it returns broken objects that throw runtime exceptions on invocations. This is not a problem as long as you have only one action container or reference all action containers' methods with their prefixed ID (as in &someContainer.someMethod), but I can imagine it might get tedious and field extraction may be rarely used, so you can turn off field extraction by setting `Lml.EXTRACT_FIELDS_AS_METHODS` to false.
- Methods without parameters can now be referenced in templates. As you might guess, these are invoked without parameters instead of consuming the enclosing actor.
- @evalute macro. Finds a method (has to consume an Object argument or no arguments) and invokes it. First parameter has to be a method ID (& operator is optional); if a second argument is given, result of the found method will be converted to string and assigned as a parser argument (which you can ${reference} in LML templates) with the name matching the second macro argument, case sensitive. This macro cannot be parental. Aliases: evaluate, eval, invoke.
- User-defined macros just got a lot less awkward to use thanks to named parameters and default values. Macro syntax stays more or less the same, but now you can reference only the chosen parameters (in any order) by proceeding them with their name and "=" (like any other tag argument). Quotations are escaped (and optional). First argument determines if the macro arguments are named - you cannot use both named and unnamed arguments, as it would be near to impossible to determine correct argument order. For example:

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
