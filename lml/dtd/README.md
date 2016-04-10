# LML DTD

*LML* files can also be valid *XML*. *DTD* files are basically schemas that can be used by your IDE of choice to provide content assist.

While a custom *DTD* file for *LML* written by hand would be a great addition, it also requires a lot of work and does not go well with *LML* extensions. Imagine every user would have to manually add all their custom tags and attributes to the *DTD* file with its awkward syntax - an automated solution was the obvious choice.

To make up for the lack of official schema, `Dtd` class is provided: it can generate a *DTD* file by analyzing an existing `LmlParser` object.

## Usage

To generate a *DTD* file, fully create your parser (including the loaded skin!) and pass it to *Dtd* class:

```
        LmlParser parser = Lml.parser(getMySkin()).build();
        parser.setStrict(false); // This will prevent some possible exceptions -
                                 // DTD schema will contain more tags and attributes.
        // TODO If you have any custom tags or attributes, add them before generating!
        // Also, parse your custom macro templates - they will also be added.
        try (PrintStream out = new PrintStream(new FileOutputStream("lml.dtd"))) {
            Dtd.saveSchema(parser, out); // Use saveMinifiedSchema for no comments.
        } catch (final Exception exception) {
            // Should never happen for valid files.
        }
        
       // Using LibGDX file API - needs a running application to work:
       Gdx.files.absolute("path/to/my.dtd").writeString(Dtd.getSchema(parser), true, "UTF-8");
```

Any problems with your parser will be printed into the console. Some are expected, don't worry about it too much.

If you don't want to generate the *DTD* file yourself, try using schemas from this folder.

Most modern IDEs will recognize referenced *DTD* files and provide content assist:

```
        <?xml version="1.0"?>
        <!DOCTYPE table SYSTEM "lml.dtd">

       <table>
           <!-- Your template goes here. -->
       </table>
```

`table` can be replaced with any root tag you want to use. Typical root actor tags: `table`, `window`, `dialog`, any actor group.

Eclipse is a bit rusty when it comes to *DTD* content assist, but I'm sure you and your favorite search engine will be able to solve it. IntelliJ works fine, not sure about other IDEs.

## Known issues

*XML* files need a single root tag. You can either use a single container actor (like `table` or `window`) or a no-op macro:
```
        <?xml version="1.0"?>
        <!DOCTYPE :noop SYSTEM "lml.dtd">

        <:noop>
            <!-- This template can contain multiple actors. -->
        </:noop>
```
Tags and attributes are case-insensitive; all case data is lost during their registration. That's why tags and attributes in *DTD* files have lowercase names, even if they contain multiple words. This might make your templates slightly harder to read.

There are no *XML* comments in content assist, not really. However, all elements and attributes in *DTD* file are proceeded with a comment containing the name of class handling the tag or attribute. All classes contains further informations about the actors or specific functionalities provided by them. Some IDEs allow to easily find the occurrence of *XML* tags and attributes in *DTD* - for example, in IntelliJ you just have to `CTRL + click` on the tag or attribute to find it in *DTD* and then you can quickly copy and check corresponding Java class.

Some attributes are marked as allowed for certain tags, but available only under some conditions. For example, all tags can have table cell attributes, but if they are not actually in a table, an exception will be thrown in runtime rather than by the IDE.

Also, there is no way to allow an element to have *any* attributes in *DTD*. There are some macros where you can enter any attribute names and would work as expected - they *could* be valid if only attribute name validation could be turned off for them. *XSD* schema generator might be provided some day, but for now - you can either accept that most of your macro tags will be marked red, or modify *DTD* files to add your attributes manually.

So, why would you even want to use *DTD*? Two words: content assist. And comments in *XML* (kinda, sorta). Who knows, if you don't use advanced macros much, your templates might even be valid *XML*.

### Pre 1.6
`gdx-lml` changed macro marker from `@` to `:` in version 1.6 to support XML restrictions. However, this is not the case with older versions.

There is a number of restricted characters in XML, including `@` - which is used was the macro special character. If you use `1.5` LML version, you have to replace it manually:

```
        LmlParser parser = Lml.parser(getDefaultSkin()).syntax(new DefaultLmlSyntax() {
            @Override
            public char getMacroMarker() {
                return ':'; // TODO Custom macro marker.
            }
        }).build()
```
Note that pre-generated *DTD* files in this folder use `:`, but you might prefer any other valid XML special character, like '.', '-' or '_'.

If you use `gdx-lml-vis`, you have to create a custom `VisLmlSyntax` instead:
```
        new VisLmlSyntax() {
            @Override
            public char getMacroMarker() {
                return ':';
            }
        }
```

However, it's pretty easy to migrate to `1.6+`. Provided that you haven't used whitespace characters between `<` and `@`, all you have to do is replace `<@` and `</@` with `<:` and `</:` in all `*.lml` files.