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
    private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();
    private final BeanAccessor accessor = new BeanRepositoryShelter(this);
    private final PostConstructor postConstructor = new PostConstructor(this);
    private final DryRunAware dryRun = new DryRunAware();

    private BeanRepository(final String name, final Map<Class<?>, BeanProvider> beanCreators) {
        this.name = name;
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
            throw new RuntimeException("No Bean registered for Class " + cls.getName());
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
    public <T> T getBean(final Supplier<T> creator) {
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
    public <T> T getBean(final Function<BeanAccessor, T> creator) {
        final PrototypeProvider provider = new PrototypeProvider(name, creator);
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
        for (BeanProvider provider : beanCreators.values()) {
            final Object bean = provider.getBean(this, true);
            if (cls.isAssignableFrom(bean.getClass())) {
                result.add(provider.getBean(this, dryRun.isDryRun()));
            }
        }
        return result;
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
        return providers(SingletonProvider.class);
    }

    /**
     * @see BeanRepository#getProvider(Class)
     * @return Providers for all registered {@code prototype} Beans
     */
    public Set<Provider<?>> getProvidersForPrototypes() {
        return providers(PrototypeProvider.class);
    }

    /**
     * @see BeanRepository#getProvider(Class)
     * @return Providers for all registered {@code instance} Beans
     */
    public Set<Provider<?>> getProvidersForInstances() {
        return providers(InstanceProvider.class);
    }

    public <T extends BeanProvider> Set<Provider<?>> providers(final Class<T> beanProviderCls) {
        final Set<Provider<?>> result = new HashSet<>();
        for (BeanProvider provider : beanCreators.values()) {
            if (beanProviderCls.isAssignableFrom(provider.getClass())) {
                result.add(providerFor(provider));
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
        private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();

        /**
         * Creates a new Builder for a {@link BeanRepository}.
         *
         * @param name    The optional name for the constructed {@link BeanRepository}.
         */
        public BeanRepositoryBuilder(final String name) {
            this.name = Objects.toString(name, ANONYMOUS);
        }

        /**
         * Creates a new Builder for a {@link BeanRepository}.
         */
        public BeanRepositoryBuilder() {
            this(null);
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
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonProvider(name, creator));
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
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonProvider(name, repository -> creator.get()));
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
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonFactoryProvider(name, repository -> creator.get()));
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
        public <T> BeanRepositoryBuilder singletonFactory(final Class<T> cls, final Function<BeanAccessor, Factory<T>> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonFactoryProvider(name, repository -> creator.apply(repository)));
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
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(name, creator));
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
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(name, repository -> creator.get()));
            return this;
        }

        /**
         * Registers a Factory for a Bean of {@code prototype} Scope. The Factory by itself is not a Bean, but
         *  the created Instance is. No {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for the
         *  Factory Object. The {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed on the
         *  created Bean, if the {@link PostConstructible} Interface is implemented.
         *
         * @param cls        The Key for the Bean. An Instance of this Class has to be returned by the Factory.
         * @param factory    The Factory which returns a Bean of the specified Class
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototypeFactory(final Class<T> cls, final Supplier<Factory<T>> factory) {
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(name, repository -> factory.get().createInstance()));
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
         * @param creator    The Factory which returns a Bean of the specified Class
         * @param <T>        The Type of the Bean
         * @return The {@link BeanRepositoryBuilder} to construct other Beans. Part of the fluent API.
         */
        public <T> BeanRepositoryBuilder prototypeFactory(final Class<T> cls, final Function<BeanAccessor, Factory<T>> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(name, repository -> creator.apply(repository).createInstance()));
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
            validateBeanId(instance.getClass());
            beanCreators.put(instance.getClass(), new InstanceProvider(name, instance));
            return this;
        }

        private void validateBeanId(final Class<?> cls) {
            if (beanCreators.containsKey(cls)) {
                throw new IllegalArgumentException("There is already a bean of Type: " + cls);
            }
        }

        /**
         * Constructs a {@link BeanRepository} with all for the Builder defined Beans.
         *
         * @param otherRepositories    If the Beans should be grouped in Modules, it is possible to merge different
         *                             BeanRepositories together. One BeanRepository equates to one Module. All Beans
         *                             of {@code otherRepositories} are summarised in one new BeanRepository. Do not
         *                             access Beans from a BeanRepository while not all Modules are wired together.
         *                             This can lead to an inconsistent state, because not all Beans may be
         *                             registered.
         * @return a full configured and initialised {@link BeanRepository}
         */
        public BeanRepository build(final Collection<BeanRepository> otherRepositories) {
            return build(otherRepositories.toArray(new BeanRepository[otherRepositories.size()]));
        }

        /**
         * Constructs a {@link BeanRepository} with all for the Builder defined Beans.
         *
         * @param otherRepositories    If the Beans should be grouped in Modules, it is possible to merge different
         *                             BeanRepositories together. One BeanRepository equates to one Module. All Beans
         *                             of {@code otherRepositories} are summarised in one new BeanRepository. Do not
         *                             access Beans from a BeanRepository while not all Modules are wired together.
         *                             This can lead to an inconsistent state, because not all Beans may be
         *                             registered.
         * @return a full configured and initialised {@link BeanRepository}
         */
        public BeanRepository build(final BeanRepository ...otherRepositories) {

            final Map<Class<?>, BeanProvider> compositeCreators = new HashMap<>();
            for (BeanRepository other : otherRepositories) {
                transferBeans(other.beanCreators, compositeCreators);
            }
            transferBeans(beanCreators, compositeCreators);

            final BeanRepository repository = new BeanRepository(name, compositeCreators);
            repository.executeDryRun();

            return repository;
        }

        private void transferBeans(final Map<Class<?>, BeanProvider> source, final Map<Class<?>, BeanProvider> target) {
            for (Map.Entry<Class<?>, BeanProvider> entry : source.entrySet()) {
                final BeanProvider existing = target.get(entry.getKey());
                if (existing != null) {
                    throw new IllegalArgumentException(String.format("Error while integrate Modules. " +
                                    "Bean [%s@%s] already exist in Repository %s."
                            , entry.getKey().getName(), entry.getValue().getRepositoryId()
                            , existing.getRepositoryId())
                    );
                }
                target.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
