package com.github.czyzby.context.platform;

import com.badlogic.gdx.ApplicationListener;
import com.github.czyzby.Core;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.scanner.ClassScanner;

/** Adding platform-specific classes can quickly become tedious. You can use global (static) variables that you have to
 * override on each platform, but this approach isn't always possible - sometimes the object has to be created
 * <b>after</b> the application already started, so you cannot just set the variable <b>before</b> creating the
 * {@link ApplicationListener}. You could pull it off with factories (setting a global object that <i>provides</i>
 * platform-specific instances), but that's another layer of complexity for a pretty simple task.
 *
 * <p>
 * You can also pass platform-specific object in {@link ApplicationListener}, like we did with {@link ClassScanner} in
 * {@link Core}. This is an acceptable approach, as long as there aren't too many such objects - otherwise your
 * application listener implementation will become unreadable. Also, there's the same problem with objects that need to
 * be created after application is initiated.
 *
 * <p>
 * Autumn makes it much easier to introduce platform-specific objects. Injection mechanism allows to inject objects by
 * their whole class tree - so, for example, if B extends A, and you have got B component in the context, it will be
 * injected both when A and B are requested. The only limitation is that A (the base class) has to be an abstract class
 * rather than interface, because LibGDX reflection lacks interfaces listing - we can only determine actual class tree
 * at runtime, without the implemented interfaces.
 *
 * <p>
 * This is an example of basic platform-specific class. Any time {@link PlatformSpecific} will be requested, a
 * platform-specific component extending {@link PlatformSpecific} will be injected. See each platform project for an
 * extension of this class.
 *
 * @author MJ */
public abstract class PlatformSpecific {
    /** Platform-specific method that will be automatically initiated thanks to the fact that this abstract method is
     * annotated with {@link Initiate}. */
    @Initiate
    public abstract void create();
}
