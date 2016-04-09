package com.github.czyzby.context.provider;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Provider;
import com.github.czyzby.autumn.provider.DependencyProvider;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** When you annotate a class with {@link Provider} interface and it implements {@link DependencyProvider} interface, it
 * will be used to provide a single type of object. In this case, {@link MyArrayProvider} provides {@link Array}
 * instances. Using {@link DependencyProvider} interface allows to invoke the provider without the use of reflection,
 * although it is not required. See {@link MyLabelProvider}.
 *
 * @author MJ */
@Provider
public class MyArrayProvider implements DependencyProvider<Array<?>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<Array<?>> getDependencyType() {
        // Ugly cast due to generics use. Don't worry, this will compile and work.
        return (Class<Array<?>>) (Object) Array.class;
    }

    @Override
    public Array<?> provide() {
        // You would generally expect a collection provider to return an empty collection, but this is just an example.
        return GdxArrays.newArray("A", "B", "C");
    }

}
