package com.github.tinosteinort.beanrepository;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This Class gives Access to all configured Beans. The Implementation does not use the Reflection API
 *  or Annotations. Because of this it is possible to the BeanRepository in a Java Sandbox, even without
 *  signed Code.
 */
public class BeanRepository {

    private final String name;
    private final Optional<BeanRepository> parent;
    private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();
    private final BeanAccessor accessor = new BeanRepositoryShelter(this);
    private final PostConstructor postConstructor = new PostConstructor(this);
    private final DryRunAware dryRun = new DryRunAware();

    private BeanRepository(final String name, final BeanRepository parent, final Map<Class<?>, BeanProvider> beanCreators) {
        this.name = name;
        this.parent = Optional.ofNullable(parent);
        this.beanCreators.putAll(beanCreators);
    }

    /**
     * Returns the Bean of the given Type, specified by the Class {@code cls} on the Configuration of
     *  the {@link BeanRepository}. The Scope of the Bean depends on the Configuration. It is possible
     *  that the {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed.
     *
     * @param cls    The Class of the Bean, used in the Configuration of the BeanRepository
     * @param <T>    The Type of the Bean
     * @see PostConstructible
     * @return a constructed and full initialised Bean.
     */
    public <T> T getBean(final Class<T> cls) {
        final BeanProvider provider = beanProviderFor(cls);
        return provider.getBean(this, dryRun.isDryRun());
    }

    private BeanProvider beanProviderFor(final Class<?> cls) {
        final BeanProvider provider = beanCreators.get(cls);
        if (provider == null) {
            return parent
                    .orElseThrow(() -> new RuntimeException("No Bean registered for Class " + cls.getName()))
                    .beanProviderFor(cls);
        }
        return provider;
    }

    /**
     * Returns a new created Object with the given {@code creator}. This equates to a {@code prototype} Bean. It is
     *  not required to configure a {@code prototype} Bean in the BeanRepository before. This Method can be used
     *  to pass Parameters to the Constructor of an Object. Use this Method if the {@code prototype} Bean has
     *  <b>no</b> Dependencies to other Beans. The Method {@link PostConstructible#onPostConstruct(BeanRepository)}
     *  is executed for every Call of this Method.
     *
     * @param creator    The Code to create the new Object
     * @param <T>        The Type of the Bean
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T> T getPrototypeBean(final Supplier<T> creator) {
        final PrototypeProvider provider = new PrototypeProvider(name, repository -> creator.get());
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new created Object with the given {@code creator}. This equates to a {@code prototype} Bean. It is
     *  not required to configure a {@code prototype} Bean in the BeanRepository before. This Method can be used
     *  to pass Parameters to the Constructor of an Object. It is also possible to provide References to other
     *  Beans in the Constructor, because a {@link BeanAccessor} is provided. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method.
     *
     * @param creator    The Code to create the new Object
     * @param <T>        The Type of the Bean
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T> T getPrototypeBean(final Function<BeanAccessor, T> creator) {
        final PrototypeProvider provider = new PrototypeProvider(name, creator);
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1> T getPrototypeBean(final ConstructorWith1Parameter<T, P1> creator, final P1 param1) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(param1));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2> T getPrototypeBean(final ConstructorWith2Parameters<T, P1, P2> creator, final P1 param1,
            final P2 param2) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(param1, param2));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3> T getPrototypeBean(final ConstructorWith3Parameters<T, P1, P2, P3> creator,
            final P1 param1, final P2 param2, final P3 param3) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(param1, param2,
                param3));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3, P4> T getPrototypeBean(final ConstructorWith4Parameters<T, P1, P2, P3, P4> creator,
            final P1 param1, final P2 param2, final P3 param3, final P4 param4) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(param1, param2,
                param3, param4));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param param5     The 5th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @param <P5>       The Type of the 5th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3, P4, P5> T getPrototypeBean(final ConstructorWith5Parameters<T, P1, P2, P3, P4, P5> creator,
            final P1 param1, final P2 param2, final P3 param3, final P4 param4, final P5 param5) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(param1, param2,
                param3, param4, param5));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1> T getPrototypeBean(final ConstructorWithBeansAnd1Parameter<T, P1> creator,
            final P1 param1) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(beans, param1));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2> T getPrototypeBean(
            final ConstructorWithBeansAnd2Parameters<T, P1, P2> creator, final P1 param1, final P2 param2) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(beans, param1,
                param2));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3> T getPrototypeBean(
            final ConstructorWithBeansAnd3Parameters<T, P1, P2, P3> creator, final P1 param1, final P2 param2,
            final P3 param3) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(beans, param1, param2,
                param3));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3, P4> T getPrototypeBean(
            final ConstructorWithBeansAnd4Parameters<T, P1, P2, P3, P4> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(beans, param1, param2,
                param3, param4));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param param5     The 5th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @param <P5>       The Type of the 5th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    public <T, P1, P2, P3, P4, P5> T getPrototypeBean(
            final ConstructorWithBeansAnd5Parameters<T, P1, P2, P3, P4, P5> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4, final P5 param5) {
        final PrototypeProvider provider = new PrototypeProvider(name, beans -> creator.create(beans, param1, param2,
                param3, param4, param5));
        return provider.getBean(this, dryRun.isDryRun());
    }

    /**
     * Returns all Beans of the given Type which have a 'is a'-Relation to the Class of the Parameter {@code cls}.
     *  The Scope of the Bean depends on the Configuration. It is possible
     *  that the {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed.
     *
     * @param cls    The Class of the Bean, used in the Configuration of the BeanRepository
     * @param <T>    The Type of the Bean
     * @see PostConstructible
     * @return a constructed and full initialised Bean.
     */
    public <T> Set<T> getBeansOfType(final Class<T> cls) {
        final Set<T> result = new HashSet<>();
        parent.ifPresent(parent -> result.addAll(parent.getBeansOfType(cls)));
        for (BeanProvider provider : beanCreators.values()) {
            final Object bean = createNotInitialisedBeanInstance(provider);
            if (cls.isAssignableFrom(bean.getClass())) {
                result.add(provider.getBean(this, dryRun.isDryRun()));
            }
        }
        return result;
    }

    private Object createNotInitialisedBeanInstance(final BeanProvider provider) {
        try {
            return dryRun.execute(() -> {
                return provider.getBean(this, dryRun.isDryRun());
            });
        }
        catch (Exception ex) {
            throw new RuntimeException("Could not create Bean Instance", ex);
        }
    }

    /**
     * With a {@link Provider} it is possible to get an Accessor to a Bean, without initialise the Bean at the time of
     *  of getting the Accessor.
     *
     * @param cls    The Class of the Bean, used in the Configuration of the BeanRepository
     * @param <T>    The Type of the Bean
     * @return a Provider for the Bean of the given Class.
     */
    public <T> Provider<T> getProvider(final Class<T> cls) {
        return providerFor(beanProviderFor(cls));
    }

    private <T> Provider<T> providerFor(final BeanProvider beanProvider) {
        return () -> beanProvider.getBean(BeanRepository.this, dryRun.isDryRun());
    }

    /**
     * @see BeanRepository#getProvider(Class)
     * @return Providers for all registered {@code singleton} Beans
     */
    public Set<Provider<?>> getProvidersForSingletons() {
        return providers(SingletonProvider.class, SingletonFactoryProvider.class);
    }

    /**
     * @see BeanRepository#getProvider(Class)
     * @return Providers for all registered {@code prototype} Beans
     */
    public Set<Provider<?>> getProvidersForPrototypes() {
        return providers(PrototypeProvider.class, PrototypeFactoryProvider.class);
    }

    /**
     * @see BeanRepository#getProvider(Class)
     * @return Providers for all registered {@code instance} Beans
     */
    public Set<Provider<?>> getProvidersForInstances() {
        return providers(InstanceProvider.class);
    }

    private Set<Provider<?>> providers(final Class<? extends BeanProvider> ...beanProviderClasses) {
        final Set<Provider<?>> result = new HashSet<>();
        for (BeanProvider provider : beanCreators.values()) {
            for (Class<?> cls : beanProviderClasses) {
                if (cls.isAssignableFrom(provider.getClass())) {
                    result.add(providerFor(provider));
                    break;
                }
            }
        }
        return result;
    }

    BeanAccessor accessor() {
        return accessor;
    }

    void postConstruct(final Object bean) {
        postConstructor.postConstruct(bean);
    }

    private void executeDryRun() {
        dryRun.execute(() -> {
            for (BeanProvider beanProvider : beanCreators.values()) {
                beanProvider.getBean(this, dryRun.isDryRun());
            }
        });
    }

    @Override public String toString() {
        return "[" + BeanRepository.class.getSimpleName() + ": " + name + "]";
    }

    /**
     * This Class is used to create an Instance of a {@link BeanRepository}. It provides a fluent API for simple
     *  Configuration in Java code.
     */
    public static class BeanRepositoryBuilder {

        private static final String ANONYMOUS = "<anonymous>";

        private final String name;
        private final BeanRepository parentRepository;
        private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();

        /**
         * Creates a new Builder for a {@link BeanRepository}.
         *
         * @param name    The optional name for the constructed {@link BeanRepository}.
         * @param parentRepository  The parent BeanRepository.
         */
        public BeanRepositoryBuilder(final String name, final BeanRepository parentRepository) {
            this.name = Objects.toString(name, ANONYMOUS);
            this.parentRepository = parentRepository;
        }

        /**
         * Creates a new Builder for a {@link BeanRepository}.
         *
         * @param name    The optional name for the constructed {@link BeanRepository}.
         */
        public BeanRepositoryBuilder(final String name) {
            this(name, null);
        }

        /**
         * Creates a new Builder for a {@link BeanRepository} with a parent BeanDirectory.
         *
         * @param parentRepository  The parent BeanDirectory.
         */
        public BeanRepositoryBuilder(final BeanRepository parentRepository) {
            this(null, parentRepository);
        }

        /**
         * Creates a new Builder for a {@link BeanRepository}.
         */
        public BeanRepositoryBuilder() {
            this(null, null);
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean which has Dependencies to other Beans. In a BeanRepository is only one Instance of a
         *  {@code singleton} Bean created.
         *
         * @param cls        The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         *                   It is guaranteed that in every BeanRepository only one Instance exist for the given Key.
         * @param creator    The {@link Function} which creates an Instance of the Bean. It Provides
         *                   a {@link BeanAccessor} to get Access to other Beans
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Function<BeanAccessor, T> creator) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean which has <b>no</b> Dependencies to other Beans. In a BeanRepository is only one Instance of a
         *  {@code singleton} Bean created.
         *
         * @param cls        The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         *                   It is guaranteed that in every BeanRepository only one Instance exist for the given Key.
         * @param creator    The {@link Function} which creates an Instance of the Bean
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Supplier<T> creator) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean with one Dependency to an other Bean. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1> BeanRepositoryBuilder singleton(final Class<T> cls,
                final ConstructorWith1Parameter<T, DEP_1> creator,
                final Class<DEP_1> dependency1) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator, dependency1));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean with two Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2> BeanRepositoryBuilder singleton(final Class<T> cls,
                final ConstructorWith2Parameters<T, DEP_1, DEP_2> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator, dependency1, dependency2));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean with three Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3> BeanRepositoryBuilder singleton(final Class<T> cls,
                final ConstructorWith3Parameters<T, DEP_1, DEP_2, DEP_3> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator, dependency1, dependency2, dependency3));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean with four Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4> BeanRepositoryBuilder singleton(final Class<T> cls,
                final ConstructorWith4Parameters<T, DEP_1, DEP_2, DEP_3, DEP_4> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator, dependency1, dependency2, dependency3,
                    dependency4));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code singleton} in the BeanRepository. This Method is used to create
         *  a Bean with five Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param dependency5   Class of the 5th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @param <DEP_5>       The Type of the 5th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> BeanRepositoryBuilder singleton(final Class<T> cls,
                final ConstructorWith5Parameters<T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4, final Class<DEP_5> dependency5) {
            definition(BeanDefinition.create(Scope.SINGLETON, cls, creator, dependency1, dependency2, dependency3,
                    dependency4, dependency5));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. The Factory by itself is not a Bean, but
         *  the created Instance is. No {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the
         *  Factory Object. The {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the
         *  created Bean, if the {@link PostConstructible} Interface is implemented.
         *
         * @param cls        The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator    The {@link Supplier} which creates an Instance of the Factory
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder singletonFactory(final Class<T> cls, final Supplier<Factory<T>> creator) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final ConstructorWith1Parameter<Factory<T>, DEP_1> creator,
                final Class<DEP_1> dependency1) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator, dependency1));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final ConstructorWith2Parameters<Factory<T>, DEP_1, DEP_2> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator, dependency1, dependency2));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final ConstructorWith3Parameters<Factory<T>, DEP_1, DEP_2, DEP_3> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator, dependency1, dependency2,
                    dependency3));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final ConstructorWith4Parameters<Factory<T>, DEP_1, DEP_2, DEP_3, DEP_4> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator, dependency1, dependency2,
                    dependency3, dependency4));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param dependency5   Class of the 5th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @param <DEP_5>       The Type of the 5th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final ConstructorWith5Parameters<Factory<T>, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4, final Class<DEP_5> dependency5) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator, dependency1, dependency2,
                    dependency3, dependency4, dependency5));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code singleton} Scope. The Factory by itself is not a Bean, but
         *  the created Instance is. No {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the
         *  Factory Object. The {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the
         *  created Bean, if the {@link PostConstructible} Interface is implemented. This function provides the
         *  {@link BeanAccessor} to the Factory for resolving other Dependencies.
         *
         * @param cls        The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator    The {@link Function} which creates an Instance of the Factory
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder singletonFactory(final Class<T> cls,
                final Function<BeanAccessor, Factory<T>> creator) {
            definition(BeanDefinition.createFactory(Scope.SINGLETON, cls, creator));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean which has Dependencies to other Beans. Beans of this Scope a created every Time when the
         *  {@link BeanRepository} is requested for the Bean.
         *
         * @param cls        The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean
         * @param creator    The {@link Function} which creates an Instance of the Bean. It Provides
         *                   a {@link BeanAccessor} to get Access to other Beans
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Function<BeanAccessor, T> creator) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean which has <b>no</b> Dependencies to other Beans. Beans of this Scope a created every Time when the
         *  {@link BeanRepository} is requested for the Bean.
         *
         * @param cls        The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean
         * @param creator    The {@link Function} which creates an Instance of the Bean
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Supplier<T> creator) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean with one Dependency to an other Bean. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1> BeanRepositoryBuilder prototype(final Class<T> cls,
                final ConstructorWith1Parameter<T, DEP_1> creator,
                final Class<DEP_1> dependency1) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator, dependency1));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean with two Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2> BeanRepositoryBuilder prototype(final Class<T> cls,
                final ConstructorWith2Parameters<T, DEP_1, DEP_2> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator, dependency1, dependency2));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean with three Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3> BeanRepositoryBuilder prototype(final Class<T> cls,
                final ConstructorWith3Parameters<T, DEP_1, DEP_2, DEP_3> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator, dependency1, dependency2, dependency3));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean with four Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4> BeanRepositoryBuilder prototype(final Class<T> cls,
                final ConstructorWith4Parameters<T, DEP_1, DEP_2, DEP_3, DEP_4> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator, dependency1, dependency2, dependency3,
                    dependency4));
            return this;
        }

        /**
         * Registers a Bean with the Scope {@code prototype} in the BeanRepository. This Method is used to create
         *  a Bean with five Dependencies to other Beans. Constructor Injection is used to populate the Dependency.
         *
         * @param cls           The Key for the Bean. Has to be the same Class as the Bean or a super Class of the Bean.
         * @param creator       The Constructor Reference.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param dependency5   Class of the 5th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @param <DEP_5>       The Type of the 5th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> BeanRepositoryBuilder prototype(final Class<T> cls,
                final ConstructorWith5Parameters<T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4, final Class<DEP_5> dependency5) {
            definition(BeanDefinition.create(Scope.PROTOTYPE, cls, creator, dependency1, dependency2, dependency3,
                    dependency4, dependency5));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. The Factory by itself is not a Bean, but
         *  the created Instance is. No {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the
         *  Factory Object. The {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the
         *  created Bean, if the {@link PostConstructible} Interface is implemented.
         *
         * @param cls        The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param factory    The {@link Supplier} which creates an Instance of the Factory
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototypeFactory(final Class<T> cls, final Supplier<Factory<T>> factory) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, factory));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final ConstructorWith1Parameter<Factory<T>, DEP_1> creator,
                final Class<DEP_1> dependency1) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator, dependency1));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final ConstructorWith2Parameters<Factory<T>, DEP_1, DEP_2> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator, dependency1, dependency2));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final ConstructorWith3Parameters<Factory<T>, DEP_1, DEP_2, DEP_3> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator, dependency1, dependency2,
                    dependency3));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final ConstructorWith4Parameters<Factory<T>, DEP_1, DEP_2, DEP_3, DEP_4> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator, dependency1, dependency2,
                    dependency3, dependency4));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. Dependencies are injected into the Constructor.
         *  The Factory by itself is not a Bean, but the created Instance is. No
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the Factory Object. The
         *  {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the created Bean, if the
         *  {@link PostConstructible} Interface is implemented.
         *
         * @param cls           The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator       The Constructor Reference to the Factory Class.
         * @param dependency1   Class of the 1st referenced Dependency in the Constructor.
         * @param dependency2   Class of the 2nd referenced Dependency in the Constructor.
         * @param dependency3   Class of the 3rd referenced Dependency in the Constructor.
         * @param dependency4   Class of the 4th referenced Dependency in the Constructor.
         * @param dependency5   Class of the 5th referenced Dependency in the Constructor.
         * @param <T>           The Type of the Bean.
         * @param <DEP_1>       The Type of the 1st Dependency.
         * @param <DEP_2>       The Type of the 2nd Dependency.
         * @param <DEP_3>       The Type of the 3rd Dependency.
         * @param <DEP_4>       The Type of the 4th Dependency.
         * @param <DEP_5>       The Type of the 5th Dependency.
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final ConstructorWith5Parameters<Factory<T>, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> creator,
                final Class<DEP_1> dependency1, final Class<DEP_2> dependency2, final Class<DEP_3> dependency3,
                final Class<DEP_4> dependency4, final Class<DEP_5> dependency5) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator, dependency1, dependency2,
                    dependency3, dependency4, dependency5));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. The Factory by itself is not a Bean, but
         *  the created Instance is. No {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the
         *  Factory Object. The {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the
         *  created Bean, if the {@link PostConstructible} Interface is implemented. This function provides the
         *  {@link BeanAccessor} to the Factory for resolving other Dependencies.
         *
         * @param cls        The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param creator    The {@link Function} which creates an Instance of the Factory
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototypeFactory(final Class<T> cls,
                final Function<BeanAccessor, Factory<T>> creator) {
            definition(BeanDefinition.createFactory(Scope.PROTOTYPE, cls, creator));
            return this;
        }

        /**
         * Registers a already created Object as Bean. To access a Bean of the Scope {@code instance}, use the Class
         *  of the Object as Key: {@link Object#getClass()}.
         * @param instance    The constructed Object which should be available as Bean
         * @param <T>         The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder instance(final T instance) {
            definition(BeanDefinition.createInstance(instance));
            return this;
        }

        /**
         * Registers a previous created Bean Definition
         *
         * @param definition    The Definition of how the Beans has to be created.
         * @param <T>           The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder definition(final BeanDefinition definition) {
            validateBeanId(definition.getBeanClass());
            beanCreators.put(definition.getBeanClass(), definition.asBeanProvider(name));
            return this;
        }

        private void validateBeanId(final Class<?> cls) {
            if (beanCreators.containsKey(cls)) {
                throw new IllegalArgumentException("There is already a bean of Type: " + cls);
            }
            if (parentRepository != null && parentRepository.beanCreators.containsKey(cls)) {
                throw new IllegalArgumentException(String.format("Error while register Bean: " +
                                "[%s@%s] already exist in Repository [%s]."
                        , cls.getName(), name
                        , parentRepository.name)
                );
            }
        }

        /**
         * Constructs a {@link BeanRepository} with all for the Builder defined Beans.
         *
         * @return a full configured and initialised {@link BeanRepository}
         */
        public BeanRepository build() {
            final BeanRepository repository = new BeanRepository(name, parentRepository, beanCreators);
            repository.executeDryRun();

            return repository;
        }
    }
}
