package com.github.czyzby.context;

/** This class will be used to start component scanning. All annotated classes in this package and any child package
 * will be detected and initiated. This is just a utility to mark context location, it should not be used otherwise.
 *
 * @author MJ */
// Note: normally you could use your Core class equivalent for this, but since this example uses the same package as my
// libraries, I'd rather not risk breaking stuff. Just accept the fact that some (any) class has to mark the scanning
// root of your application - and you can choose multiple scanning roots, for that matter.
public class Root {
}
