# LML DTD

*LML* files can also be valid *XML*. *DTD* files are basically schemas that can be used by your IDE of choice to provide
content assist.

While a custom *DTD* file for *LML* written by hand would be a great addition, it also requires a lot of work and does
not go well with *LML* extensions. Imagine every user would have to manually add all their custom tags and attributes
to the *DTD* file with its awkward syntax - an automated solution was the obvious choice.

To make up for the lack of official schema, `Dtd` class is provided: it can generate a *DTD* file by analyzing an
existing `LmlParser` object.

## Usage

To generate a *DTD* file, fully create your parser (including the loaded skin!) and pass it to *Dtd* class:

```Java
LmlParser parser = Lml.parser(getMySkin()).build();
parser.setStrict(false); // This will prevent some possible exceptions -
                         // DTD schema will contain more tags and attributes.
// If you have any custom tags or attributes, add them before generating!
// Parse your templates with custom LML-defined macros - they will be added as well.

try {
    Writer writer = Gdx.files.absolute("/path/to/your/file.dtd").writer(false);
    Dtd.saveSchema(parser, writer);
    writer.close();
} catch (Exception exception) {
    throw new GdxRuntimeException(exception);
}
```

Any problems with your parser will be printed into the console. Some are expected, don't worry about it too much.

If you don't want to generate the *DTD* file yourself, try using schemas from this folder.

Most modern IDEs will recognize referenced *DTD* files and provide content assist:

```XML
<?xml version="1.0"?>
<!DOCTYPE table SYSTEM "lml.dtd">

<table>
   <!-- Your template goes here. -->
</table>
```

`table` can be replaced with any root tag you want to use. Typical root actor tags: `table`, `window`, `dialog`, any
actor group.

Eclipse is a bit rusty when it comes to *DTD* content assist, but it should generally work.
IntelliJ  - on the other hand - works great. Not sure about other IDEs, though.

## Known issues

*XML* files need a single root tag. You can either use a single container actor (like `table` or `window`) or a no-op macro:
```XML
<?xml version="1.0"?>
<!DOCTYPE :noop SYSTEM "lml.dtd">

<:noop>
    <!-- This template can contain multiple actors. -->
</:noop>
```
Tags and attributes are case-insensitive; all case data is lost during their registration. That's why tags and
attributes in *DTD* files have lowercase names, even if they contain multiple words. This might make your templates
slightly harder to read.

There are no *XML* comments in content assist, not really. However, all elements and attributes in *DTD* file are
proceeded with a comment containing the name of class handling the tag or attribute. All classes contains further
information about the actors or specific functionalities provided by them. Some IDEs allow to easily find the
occurrence of *XML* tags and attributes in *DTD* - for example, in IntelliJ you just have to `CTRL + click` on the
tag or attribute to find it in *DTD* and then you can quickly copy and check corresponding Java class.

Some attributes are marked as allowed for certain tags, but available only under some conditions. For example, all
tags can have table cell attributes, but if they are not actually in a table, an exception will be thrown in runtime
rather than by the IDE.

Also, there is no way to allow an element to have *any* attributes in *DTD*. There are some macros where you can enter
any attribute names and would work as expected - they *could* be valid if only attribute name validation could be turned
off for them. *XSD* schema generator might be provided some day, but for now - you can either accept that most of your
macro tags will be marked red, or modify *DTD* files to add your attributes manually.

Input validators from `gdx-lml-vis` might not have all their attributes properly listed, as some are wrapped with form
validators at runtime, getting a whole bunch of additional attributes. It's safe to assume that most attributes from
`customValidator` tag are also available in tags like `isInt`, `isFloat`, and so on.

So, why would you even want to use *DTD*? Two words: content assist. And comments in *XML* (kinda, sorta). Who knows,
if you don't use advanced macros much, your templates might even be valid *XML*.

### DYI

DTD schema is pretty straightforward. You can easily append your custom tags and attributes - and I'm not talking only
about editing files by hand; I assume you're familiar with string concatenation and `Appendable` interface - you can
easily add your custom, hard written lines to your DTD generation script. The generator *might* fail to find some tag,
macro or attribute due to various reasons, like internal validation in their classes that prevents from creating mock-up
instances needed to generate DTD data. If that's the case, you can always hook up to the generator and append the missing
schema data yourself.

Adding a new tag that can have any children:
```DTD
<!ELEMENT tagName ANY>
```

Adding a list of attributes:
```DTD
<!ATTLIST tagName
    attributeName CDATA #IMPLIED
    secondName CDATA #IMPLIED>
```

Adding a single attribute:
```DTD
<!ATTLIST tagName attributeName CDATA #IMPLIED>
```

Look up DTD syntax if you want more advanced schema features, like limiting the type of children a tag can have.
