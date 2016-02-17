# LML DTD

*LML* files can also be valid *XML*. *DTD* files are basically schemas that can be used by your IDE of choice to provide content assist.

While a custom *DTD* file for *LML* written by hand would be a great addition, it also requires a lot of work and does not go well with *LML* extensions. To make up for this, *Dtd* class is provided: it can generate a *DTD* file by analyzing an existing *LmlParser* object.

## Usage

To generate a *DTD* file, fully create your parser (including the loaded skin!) and pass it to *Dtd* class:
```
LmlParser parser = Lml.parser(getMySkin()).build();
// TODO If you have any custom tags or attributes, add them before generating!
try (PrintStream out = new PrintStream(new FileOutputStream("lml.dtd"))) {
    out.print(Dtd.getSchema(parser));
} catch (final Exception exception) {
    // Should never happen for valid files.
}
```
Any problems with your parser will be printed into the console.

If you don't want to generate the file yourself, try using schemas from this folder.

Most modern IDEs will recognized referenced *DTD* files and provide content assist:

```
<?xml version="1.0"?>
<!DOCTYPE table SYSTEM "lml.dtd">

<table>
    <!-- Your template goes here. -->
</table>
```

## Known issues

There is a number of restricted characters in XML, including `@` - which is used as the macro beginning character. In future versions macro marker might change, but for now you might have to replace it manually:

```
LmlParser parser = Lml.parser(getDefaultSkin()).syntax(new DefaultLmlSyntax() {
    @Override
    public char getMacroMarker() {
        return ':'; // TODO Custom macro marker.
    }
}).build()
```
Note that pre-generated *DTD* files in this folder use `:`, but you might prefer any other valid XML special character, like '.', '-' or '_'.

*XML* files need single root tag. You can either use a single container actor (like table or window) or a no-op macro:
```
<?xml version="1.0"?>
<!DOCTYPE :noop SYSTEM "lml.dtd">

<:noop>
    <!-- This template can contain multiple actors. -->
</:noop>
```
Tags and attributes are case-insensitive; all case data is lost during registration. That's why *DTD* files have lowercase values, even if they contain multiple words. This might make your templates slightly harder to read.

There are no *XML* comments in content assist, not really. However, all elements and attributes in *DTD* file are proceeded with a comment containing the name of the tag or attribute class. All classes contains further informations about the actors or specific functionalities provided by them. Some IDEs allow to easily find the occurrence of *XML* tags and attributes in *DTD* - for example, in IntelliJ you just have to `CTRL + click` on the tag or attribute to find it in *DTD* and check corresponding Java class.

Some attributes are marked as allowed for certain tags, but available only under some conditions. For example, any tag can have table cell attributes, but if they are not actually in a table, an exception will be thrown in runtime.

Also, there is no way to allow an element to have any attributes in *DTD*. Of course, things like this:
```
<:if {argument} < 13 || {argument} != 20>
```
...are invalid *XML* tags no matter how hard we try, but there are some macros that *could* be valid if only they could use any attributes. *XSD* schema generator might be provided some day, but for now - you can either accept that most of your macro tags will be marked red, or modify *DTD* files to add your attributes manually.

So, why you even want to use *DTD*? Two words: content assist. And comments in *XML* (kinda, sorta). Who knows, if you don't use macros much, your templates might even be valid *XML*.