#LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

##Sample project
See [gdx-lml-tests](http://github.com/czyzby/gdx-lml-tests).

##Maven artifact
To import LML with Gradle, add this dependency to your core:
```
    compile "com.github.czyzby:gdx-lml:0.8.$gdxVersion"
```
Currently supported LibGDX version is **1.7.0**.

If you want to use LML with GWT, you have to add this module to your GdxDefinition:
```
	<inherits name='com.github.czyzby.lml.GdxLml' />
```

##Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843), [example project](http://github.com/czyzby/gdx-lml-tests) and [Wiki syntax page (work in progress)](https://github.com/czyzby/gdx-lml/wiki/Syntax).

## What's new
0.8 -> 0.9:

Use snapshot for new features.

- `@actor` macro. Looks for an action with the passed ID, invokes it (expecting an Actor) and adds the returned Actor to the current parent tag. For example, if you use `<@actor myMethodName />` tag inside `<table>...</table>` tags, your actor returned by `myMethodName` action will be added to the table. Useful if you want to create an actor from scratch without adding a new actor tag parser.
- Array-parsing macros (*forEach*, *nested*, etc.) now support methods that return object arrays. Before you had to manually convert arrays to iterables to properly handle method invocations; now simple object arrays can be returned and each of its elements will be safely converted to string. Primitive arrays are not supported (yet?).
- Now conditional macros (like *if*, *notNull*) evaluate to "false" if they receive no arguments, rather than throwing an exception. This might be especially useful when using nullable LML arguments (for example, `${someEmptyStringArgument}` would cause to print part after `<@if:else/>` when passed to *if* macro: `<@if ${someEmptyStringArgument}>`).
- Removed custom tooltips (which were kinda cool with their moving along with the mouse and jumping when near an edge, but still - now there's "native" support). Since LibGDX tooltips are not actors, `tooltipId` attribute is no longer supported. Old tooltip tags still work: any actor can have a TextTooltip attached with a customized style by using `tooltip` (content) and `tooltipStyle` (name of TextTooltipStyle in skin) attributes; if `tooltip` is an action (`&proceededWithThat`) and the action returns an Actor, `Tooltip<Actor>` will be created instead. As a separate tag, `<tooltip>...</tooltip>` still works like a table - internally, `Tooltip<Table>` will be created and all new tags between the tooltip entities will be added to the table; all table attributes are supported, so you can specify `background`, `oneColumn`, `tablePad`, etc. Tooltip tag can have two additional attributes: `always` and `instant`, which trigger its setter methods (as you might expect).


0.7 -> 0.8:

- Color tag attributes. Now it is possible to set actor's color from within the LML templates. `color` attribute expects either a method (or field) name that returns a Color, or a String which is the name of a color present in .json Skin. You can also manually set each color value with `r`, `red`, `b`, `blue`, `g`, `green`, `a` and `alpha` attributes - these will expect a float. These attributes are available to all actor tags, as they use only the Actor's API.

0.7.1.6.4 -> 0.7.1.6.5:

- Missing `maxLength` attribute for TextField and TextArea now supported.
- Null check macro can now be invoked without attributes and will not throw exceptions; instead it works as if it would receive null or false (shows "else" variant).
- Empty LML document will not throw exceptions on parsing; instead, it returns an empty array of actors, as you would expect.
- Note that even though LibGDX now supports tooltips, for now the initial LML mechanism is still used. `<tooltip>` tag still works similarly to a table, which can be filled with any widgets and is shown after a short delay.
 
0.6 -> 0.7:

- Wiki page with all tags and attributes is being created. Stay tuned.
- Added "disabled" attribute to ProgressBar.
- @ViewAction now takes an array of strings as action IDs. This won't break existing code, while allowing to map the selected method by multiple IDs.
- New common tag attribute: userObject. Allows to assign a custom object to the actor by using Actor#setUserObject method. If action operator is used (&), method will be found and executed - its result will be assigned as the user object; in other cases, string value of the attribute is set. Be careful - some widgets (Dialogs, Windows, Tooltips) use the same mechanism to store stage/actor attachment data; however, this attribute should be safe to use for actors that are not roots (as in not directly added to stage).
- Extended tooltip support. Now you can attach tooltips to actors by adding a `<tooltip>` tag inside them. Tooltip works pretty much like table, so you can fully customize tooltips' look and layout. Tooltip tag has 3 extra settings (additionally to all table attributes): fadingTime, movingTime, showingDelay (all in seconds; see Tooltip docs). Child `<tooltip/>` tag is NOT advised (especially since there is no way to set its content) - tooltips should be added with parental tags (`<tooltip>...</tooltip>`).

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
