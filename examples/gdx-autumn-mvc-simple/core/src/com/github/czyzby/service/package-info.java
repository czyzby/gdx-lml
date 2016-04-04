/** In a regular game, you'd eventually want to create some logic-related classes. These can also benefit from Autumn
 * MVC utilities - if you annotate a class with {@link com.github.czyzby.autumn.annotation.Component}, it will be
 * automatically initiated and effectively will become a singleton (unless you spawn another instance manually). Such
 * objects can be {@link com.github.czyzby.autumn.annotation.Inject}ed into other annotated classes' fields. You can
 * also control the flow of application's initiation and destruction through
 * {@link com.github.czyzby.autumn.annotation.Initiate}- and {@link com.github.czyzby.autumn.annotation.Destroy}
 * -annotated methods. See Autumn MVC GitHub page or java docs for more data. */
package com.github.czyzby.service;
