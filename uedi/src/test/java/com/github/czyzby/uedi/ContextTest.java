package com.github.czyzby.uedi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.czyzby.uedi.error.circular.CircularErrorA;
import com.github.czyzby.uedi.error.circular.CircularErrorB;
import com.github.czyzby.uedi.impl.DefaultContext;
import com.github.czyzby.uedi.scanner.ClassScanner;
import com.github.czyzby.uedi.scanner.impl.FixedClassScanner;
import com.github.czyzby.uedi.stereotype.Provider;
import com.github.czyzby.uedi.test.Root;
import com.github.czyzby.uedi.test.TestComponent;
import com.github.czyzby.uedi.test.TestFactory;
import com.github.czyzby.uedi.test.TestProperty;
import com.github.czyzby.uedi.test.TestProvider;
import com.github.czyzby.uedi.test.TestSingleton;
import com.github.czyzby.uedi.test.ambiguous.Ambiguous;
import com.github.czyzby.uedi.test.ambiguous.AmbiguousA;
import com.github.czyzby.uedi.test.ambiguous.AmbiguousB;
import com.github.czyzby.uedi.test.ambiguous.AmbiguousInjector;
import com.github.czyzby.uedi.test.ambiguous.ListDefaultProvider;
import com.github.czyzby.uedi.test.ambiguous.ListFactory;
import com.github.czyzby.uedi.test.ambiguous.NamedAmbiguous;
import com.github.czyzby.uedi.test.classpath.AbstractClassImplementingSingleton;
import com.github.czyzby.uedi.test.classpath.AbstractClassUser;
import com.github.czyzby.uedi.test.custom.CustomFactory;
import com.github.czyzby.uedi.test.custom.CustomFactory.CustomComponent;
import com.github.czyzby.uedi.test.custom.CustomSingleton;
import com.github.czyzby.uedi.test.inject.AbstractWithFields;
import com.github.czyzby.uedi.test.inject.Built;
import com.github.czyzby.uedi.test.inject.CircularA;
import com.github.czyzby.uedi.test.inject.CircularB;
import com.github.czyzby.uedi.test.inject.ConstructorDependency;
import com.github.czyzby.uedi.test.inject.Ignored;
import com.github.czyzby.uedi.test.inject.InjectFactory;
import com.github.czyzby.uedi.test.inject.InjectProperty;
import com.github.czyzby.uedi.test.inject.InjectProvider;
import com.github.czyzby.uedi.test.inject.Injected;
import com.github.czyzby.uedi.test.inject.Injector;
import com.github.czyzby.uedi.test.inject.Provided;
import com.github.czyzby.uedi.test.inject.UsingAbstractWithFields;
import com.github.czyzby.uedi.test.lifecycle.Counter;
import com.github.czyzby.uedi.test.lifecycle.DestroyedA;
import com.github.czyzby.uedi.test.lifecycle.DestroyedB;
import com.github.czyzby.uedi.test.lifecycle.DestroyedC;
import com.github.czyzby.uedi.test.lifecycle.InitiatedA;
import com.github.czyzby.uedi.test.lifecycle.InitiatedB;
import com.github.czyzby.uedi.test.lifecycle.InitiatedC;

/** Should be extended to test every {@link Context} variant with every {@link ClassScanner} implementation.
 *
 * @author MJ */
public class ContextTest {
    private final Context context = new DefaultContext(
            new FixedClassScanner(CircularErrorA.class, CircularErrorB.class, Root.class, TestComponent.class,
                    TestFactory.class, TestProperty.class, TestProvider.class, TestSingleton.class, Ambiguous.class,
                    AmbiguousA.class, AmbiguousB.class, AmbiguousInjector.class, ListDefaultProvider.class,
                    ListFactory.class, NamedAmbiguous.class, AbstractClassImplementingSingleton.class,
                    AbstractClassUser.class, CustomFactory.class, CustomSingleton.class, AbstractWithFields.class,
                    Built.class, CircularA.class, CircularB.class, ConstructorDependency.class, Ignored.class,
                    Injected.class, InjectFactory.class, Injector.class, InjectProperty.class, InjectProvider.class,
                    Provided.class, UsingAbstractWithFields.class, Counter.class, DestroyedA.class, DestroyedB.class,
                    DestroyedC.class, InitiatedA.class, InitiatedB.class, InitiatedC.class));

    @Before
    public void scan() {
        context.scan(Root.class);
    }

    @Test
    public void shouldFindSingletons() {
        assertNotNull(context.get(TestSingleton.class));
        assertEquals(context.get(TestSingleton.class), context.get(TestSingleton.class));
    }

    @Test
    public void shouldFindFactories() {
        assertNotNull(context.get(TestFactory.class));
        assertEquals(context.get(TestFactory.class), context.get(TestFactory.class));
    }

    @Test
    public void shouldFindProviders() {
        assertNotNull(context.get(TestProvider.class));
        assertEquals(context.get(TestProvider.class), context.get(TestProvider.class));
    }

    @Test
    public void shouldFindProperties() {
        assertNotNull(context.get(TestProperty.class));
        assertEquals(context.get(TestProperty.class), context.get(TestProperty.class));
        assertEquals(TestProperty.VALUE, context.getProperty(TestProperty.ID));
    }

    @Test
    public void shouldFindClassesWithImplementingSuperClass() {
        assertNotNull(context.get(AbstractClassUser.class));
    }

    @Test
    public void shouldNotInitiateAbstractClassesAndInterfaces() {
        assertEquals(context.get(AbstractClassImplementingSingleton.class), context.get(AbstractClassUser.class));
    }

    @Test
    public void shouldInjectContext() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.context);
        assertEquals(context, injector.context);
        assertEquals(context.get(Context.class), injector.context);
        assertEquals(context.get(Context.class), context.get(Context.class));
    }

    @Test
    public void shouldInjectSingletons() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.injected);
        assertEquals(context.get(Injected.class), injector.injected);
        assertEquals(context.get(Injected.class), context.get(Injected.class));
    }

    @Test
    public void shouldInjectObjectsBuiltByFactories() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.built);
        assertNotEquals(context.get(Built.class), injector.built);
        assertNotEquals(context.get(Built.class), context.get(Built.class));
    }

    @Test
    public void shouldAllowPassingConsumersToFactoryMethods() {
        final float consumer = 2f;
        // InjectFactory contains square method, which consumes a Float parameter and returns a Double. Since this call
        // requests a Double instance for (boxed) Float object, consumer should be injected as factory method parameter.
        final double result = context.get(Double.class, consumer);
        assertEquals(4, result, 0.001);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotConvertStaticFactoryMethodsToProviders() {
        assertNotNull(context.get(InjectFactory.class));
        context.get(BigDecimal.class); // Type returned by a static method in InjectFactory.
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotConvertNonPublicFactoryMethodsToProviders() {
        assertNotNull(context.get(InjectFactory.class));
        context.get(BigInteger.class); // Type returned by non-public methods in InjectFactory.
    }

    @Test
    public void shouldInjectObjectsProvidedByProviders() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.provided);
        assertNotEquals(context.get(Provided.class), injector.provided);
        assertNotEquals(context.get(Provided.class), context.get(Provided.class));
    }

    @Test
    public void shouldInjectProperties() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.property);
        assertEquals(context.getProperty("property"), injector.property);
        assertEquals(InjectProperty.VALUE, injector.property);
        assertEquals(context.getProperty("property"), context.getProperty("property"));
    }

    @Test
    public void shouldInjectIntoFieldsFromSuperTypes() {
        final UsingAbstractWithFields child = context.get(UsingAbstractWithFields.class);
        assertNotNull(child.getChildField());
        assertNotNull(child.getSuperField());
        assertNotNull(((AbstractWithFields) child).getSuperField());
    }

    @Test
    public void shouldNotInjectIntoTransientFields() {
        assertNull(context.get(Injector.class).transientValue);
    }

    @Test
    public void shouldNotInjectIntoNotNullFields() {
        final Injector injector = context.get(Injector.class);
        assertNotNull(injector.notNull);
        assertEquals(Ignored.TEST_INSTANCE, injector.notNull);
    }

    @Test
    public void shouldNotInjectIntoStaticFields() {
        assertNull(Injector.staticValue);
    }

    @Test
    public void shouldNotInjectIntoPrimitiveFields() {
        assertEquals(42, context.get(Injector.class).primitive);
    }

    @Test
    public void shouldResolveCircularFieldInjections() {
        assertEquals(context.get(CircularA.class), context.get(CircularB.class).getA());
        assertEquals(context.get(CircularB.class), context.get(CircularA.class).getB());
    }

    @Test
    public void shouldResolveConstructorInjections() {
        final ConstructorDependency constructor = context.get(ConstructorDependency.class);
        assertEquals(context.get(CircularA.class), constructor.getA());
        assertEquals(context.get(CircularB.class), constructor.getB());
    }

    @Test
    public void shouldConstructInstanceOnDemand() {
        final ConstructorDependency created = context.create(ConstructorDependency.class);
        assertEquals(context.get(CircularA.class), created.getA());
        assertEquals(context.get(CircularB.class), created.getB());
        assertEquals(context.get(Injected.class), created.getInjected());
        assertNotEquals(context.get(ConstructorDependency.class), created);
    }

    @Test
    public void shouldInitiateMarkedComponents() {
        assertTrue(context.get(InitiatedA.class).wasInitiated());
        assertTrue(context.get(InitiatedB.class).wasInitiated());
        assertTrue(context.get(InitiatedC.class).wasInitiated());
    }

    @Test
    public void shouldInitiateMarkedComponentsAfterDependenciesAreInjected() {
        assertTrue(context.get(InitiatedA.class).wereDependenciesNotNull());
        assertTrue(context.get(InitiatedB.class).wereDependenciesNotNull());
        assertTrue(context.get(InitiatedC.class).wereDependenciesNotNull());
    }

    @Test
    public void shouldInitiateMarkedComponentsInTheChosenOrder() {
        assertTrue(context.get(InitiatedA.class).getActualOrder() < context.get(InitiatedB.class).getActualOrder());
        assertTrue(context.get(InitiatedB.class).getActualOrder() < context.get(InitiatedC.class).getActualOrder());
    }

    @Test
    public void shouldInitiateOnDemand() {
        final InitiatedA initiatedA = new InitiatedA(context.get(InitiatedB.class));
        assertFalse(initiatedA.wasInitiated());
        context.initiate(initiatedA);
        assertTrue(initiatedA.wasInitiated());
    }

    @Test
    public void shouldInitiateComponentsCreatedOnDemand() {
        final InitiatedA initiatedA = context.create(InitiatedA.class);
        assertTrue(initiatedA.wasInitiated());
        assertNotEquals(context.get(InitiatedA.class), initiatedA);
    }

    @Test
    public void shouldDestroyMarkedComponents() {
        assertFalse(context.get(DestroyedA.class).wasDestroyed());
        assertFalse(context.get(DestroyedB.class).wasDestroyed());
        assertFalse(context.get(DestroyedC.class).wasDestroyed());
        context.destroy();
        assertTrue(context.get(DestroyedA.class).wasDestroyed());
        assertTrue(context.get(DestroyedB.class).wasDestroyed());
        assertTrue(context.get(DestroyedC.class).wasDestroyed());
    }

    @Test
    public void shouldDestroyMarkedComponentsAfterDependenciesAreInjected() {
        context.destroy();
        assertTrue(context.get(DestroyedA.class).wereDependenciesNotNull());
        assertTrue(context.get(DestroyedB.class).wereDependenciesNotNull());
        assertTrue(context.get(DestroyedC.class).wereDependenciesNotNull());
    }

    @Test
    public void shouldDestroyMarkedComponentsInTheChosenOrder() {
        context.destroy();
        assertTrue(context.get(DestroyedA.class).getActualOrder() < context.get(DestroyedB.class).getActualOrder());
        assertTrue(context.get(DestroyedB.class).getActualOrder() < context.get(DestroyedC.class).getActualOrder());
    }

    @Test
    public void shouldDestroyOnDemand() {
        final DestroyedA destroyedA = context.get(DestroyedA.class);
        assertFalse(destroyedA.wasDestroyed());
        context.destroy(destroyedA);
        assertTrue(destroyedA.wasDestroyed());
    }

    @Test
    public void shouldDestroyComponentsCreatedOnDemand() {
        final DestroyedA destroyedA = context.create(DestroyedA.class);
        assertFalse(destroyedA.wasDestroyed());
        context.destroy(destroyedA);
        assertTrue(destroyedA.wasDestroyed());
        assertNotEquals(context.get(DestroyedA.class), destroyedA);

        final DestroyedB destroyedB = new DestroyedB(context.create(InitiatedB.class));
        assertFalse(destroyedB.wasDestroyed());
        assertNull(destroyedB.getInjectedDependency());
        context.initiate(destroyedB);
        assertFalse(destroyedB.wasDestroyed());
        assertNotNull(destroyedB.getInjectedDependency());
        context.destroy();
        assertTrue(destroyedB.wasDestroyed());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenAmbiguousDependencyWithNoDefaultIsRequested() {
        context.get(Ambiguous.class);
    }

    @Test
    public void shouldReturnValidAmbiguousDependencyWhenGivenCorrectID() {
        final Ambiguous ambiguous = context.get("ambiguousA", Ambiguous.class);
        assertTrue(ambiguous instanceof AmbiguousA);
        assertEquals(context.get(AmbiguousA.class), ambiguous);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenNamedDependencyIsRequestedByLowerCamelCaseClassName() {
        context.get("namedAmbiguous", Ambiguous.class);
    }

    @Test
    public void shouldReturnValidAmbiguousDependencyWhenGivenCorrectNameOfNamedComponent() {
        final String name = NamedAmbiguous.NAME;
        final Ambiguous ambiguous = context.get(name, Ambiguous.class);
        assertTrue(ambiguous instanceof NamedAmbiguous);
        assertEquals(context.get(NamedAmbiguous.class), ambiguous);
    }

    @Test
    public void shouldInjectCorrectAmbiguousDependenciesWhenFieldNamesMatchComponentIds() {
        final AmbiguousInjector injector = context.get(AmbiguousInjector.class);
        assertTrue(injector.getA() instanceof AmbiguousA);
        assertEquals(context.get(AmbiguousA.class), injector.getA());
        assertTrue(injector.getB() instanceof AmbiguousB);
        assertEquals(context.get(AmbiguousB.class), injector.getB());
        assertTrue(injector.getNamed() instanceof NamedAmbiguous);
        assertEquals(context.get(NamedAmbiguous.class), injector.getNamed());
    }

    @Test
    public void shouldResolveAmbiguousDependencyGivenDefaultProvider() {
        // ListDefaultProvider implements Default interface, so it should be automatically used when trying to inject a
        // value without an ID. It returns array lists.
        assertTrue(context.get(AbstractList.class) instanceof ArrayList<?>);
    }

    @Test
    public void shouldTreatFactoryMethodNamesAsProviderIds() {
        // ListFactory contains a "linkedList" method which returns instances of linked lists.
        assertTrue(context.get("linkedList", List.class) instanceof LinkedList<?>);
    }

    @Test(expected = RuntimeException.class)
    public void shouldRemoveProvidersAfterContextClearing() {
        try {
            context.get(TestComponent.class);
        } catch (final Exception exception) {
            fail(exception + " should not be thrown yet.");
        }
        context.clear();
        context.get(TestComponent.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldRemoveProviderForClass() {
        try {
            context.get(TestComponent.class);
        } catch (final Exception exception) {
            fail(exception + " should not be thrown yet.");
        }
        context.remove(TestComponent.class);
        context.get(TestComponent.class);
    }

    @Test
    public void shouldStillProvideContextInstanceEvenAfterClearing() {
        context.clear();
        assertEquals(context, context.get(Context.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldClearProvidersForClassTree() {
        try {
            context.get(AbstractList.class);
        } catch (final Exception exception) {
            fail(exception + " should not be thrown yet.");
        }
        context.clear(ArrayList.class);
        context.get(AbstractList.class);
    }

    @Test
    public void shouldReplaceProvider() {
        final TestComponent previous = context.get(TestComponent.class);
        final Provider<TestComponent> newProvider = new Provider<TestComponent>() {
            @Override
            public Class<? extends TestComponent> getType() {
                return TestComponent.class;
            }

            @Override
            public TestComponent provide(final Object target, final Member member) {
                return new TestComponent();
            }
        };
        context.replace(TestComponent.class, newProvider);
        final TestComponent current = context.get(TestComponent.class);
        assertNotEquals(previous, current);
    }

    @Test
    public void shouldAddCustomProperty() {
        context.setProperty("custom", "custom value");
        assertEquals("custom value", context.getProperty("custom"));
        context.setProperty("custom", "new value");
        assertEquals("new value", context.getProperty("custom"));
    }

    @Test
    public void shouldAddCustomSingleton() {
        assertFalse(context.isAvailable(CustomSingleton.class));
        final CustomSingleton custom = new CustomSingleton();
        context.add(custom);
        assertTrue(context.isAvailable(CustomSingleton.class));
        assertEquals(custom, context.get(CustomSingleton.class));
        assertEquals(context.get(CustomSingleton.class), context.get(CustomSingleton.class));
    }

    @Test
    public void shouldAddCustomFactory() {
        assertFalse(context.isAvailable(CustomFactory.class));
        assertFalse(context.isAvailable(CustomComponent.class));
        final CustomFactory custom = new CustomFactory();
        context.addFactory(custom);
        assertTrue(context.isAvailable(CustomFactory.class));
        assertTrue(context.isAvailable(CustomComponent.class));
        // CustomFactory always returns the same instance of CustomComponent.
        assertEquals(custom.getComponent(), context.get(CustomComponent.class));
    }

    @Test
    public void shouldAddCustomProvider() {
        assertFalse(context.isAvailable(CustomComponent.class));
        context.addProvider(new Provider<CustomComponent>() {
            @Override
            public Class<? extends CustomComponent> getType() {
                return CustomComponent.class;
            }

            @Override
            public CustomComponent provide(final Object target, final Member member) {
                return new CustomComponent();
            }
        });
        assertTrue(context.isAvailable(CustomComponent.class));
        assertNotEquals(context.get(CustomComponent.class), context.get(CustomComponent.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForUnknownComponent() {
        context.get(Assert.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenCircularConstructorDependencyIsDetected() {
        context.scan(CircularErrorA.class);
    }
}
