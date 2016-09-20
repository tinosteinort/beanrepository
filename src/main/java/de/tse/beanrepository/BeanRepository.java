package de.tse.beanrepository;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanRepository {

    private final String name;
    private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();
    private final BeanAccessor accessor = new BeanRepositoryShelter(this);
    private final PostConstructor postConstructor = new PostConstructor(this);

    private BeanRepository(final String name, final Map<Class<?>, BeanProvider> beanCreators) {
        this.name = Objects.toString(name, "<anonymous>");
        this.beanCreators.putAll(beanCreators);
    }

    public <T> T getBean(final Class<T> cls) {
        final BeanProvider provider = beanCreators.get(cls);
        if (provider == null) {
            throw new RuntimeException("No Bean registered for Class " + cls.getName());
        }
        return provider.getBean(this, false);
    }

    public <T> Set<T> getBeansOfType(final Class<T> cls) {
        final Set<T> result = new HashSet<T>();
        for (BeanProvider provider : beanCreators.values()) {
            final Object bean = provider.getBean(this, true);
            if (cls.isAssignableFrom(bean.getClass())) {
                result.add((T) provider.getBean(this, false));
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

    @Override public String toString() {
        return "[" + BeanRepository.class.getSimpleName() + ": " + name + "]";
    }

    public static class BeanRepositoryBuilder {

        private final String name;
        private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();

        public BeanRepositoryBuilder(final String name) {
            this.name = name;
        }
        public BeanRepositoryBuilder() {
            this(null);
        }

        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Function<BeanAccessor, T> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonProvider(creator));
            return this;
        }

        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Supplier<T> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new SingletonProvider(repository -> creator.get()));
            return this;
        }

        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Function<BeanAccessor, T> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(creator));
            return this;
        }

        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Supplier<T> creator) {
            validateBeanId(cls);
            beanCreators.put(cls, new PrototypeProvider(repository -> creator.get()));
            return this;
        }

        public <T> BeanRepositoryBuilder instance(final T instance) {
            validateBeanId(instance.getClass());
            beanCreators.put(instance.getClass(), new InstanceProvider(instance));
            return this;
        }

        private void validateBeanId(final Class<?> cls) {
            if (beanCreators.containsKey(cls)) {
                throw new IllegalArgumentException("There is already a bean of Type: " + cls);
            }
        }

        public BeanRepository build(final BeanRepository ...otherRepositories) {

            final Map<Class<?>, BeanProvider> compositeCreators = new HashMap<>();
            for (BeanRepository other : otherRepositories) {
                transferBeans(other.beanCreators, compositeCreators);
            }
            transferBeans(beanCreators, compositeCreators);

            final BeanRepository repository = new BeanRepository(name, compositeCreators);
            executeDryRun(repository);

            return repository;
        }

        private BeanRepository executeDryRun(final BeanRepository repository) {
            for (BeanProvider beanProvider : repository.beanCreators.values()) {
                beanProvider.getBean(repository, true);
            }
            return repository;
        }

        private void transferBeans(final Map<Class<?>, BeanProvider> source, final Map<Class<?>, BeanProvider> target) {
            for (Map.Entry<Class<?>, BeanProvider> entry : source.entrySet()) {
                if (target.containsKey(entry.getKey())) {
                    throw new IllegalArgumentException("There is already a bean of Type: " + entry.getKey());
                }
                target.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
