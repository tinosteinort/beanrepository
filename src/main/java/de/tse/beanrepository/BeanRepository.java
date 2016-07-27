package de.tse.beanrepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanRepository {

    private final Map<Class<?>, BeanProvider> beanCreators;

    private BeanRepository(final Map<Class<?>, BeanProvider> beanCreators) {
        this.beanCreators = beanCreators;
    }

    public <T> T get(final Class<T> cls) {
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
            final Class<?> beanClass = bean.getClass();
            if (cls.isAssignableFrom(beanClass)) {
                result.add((T) bean);
            }
        }
        return result;
    }

    public static class BeanRepositoryBuilder {

        private final Map<Class<?>, BeanProvider> beanCreators = new HashMap<>();

        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Function<BeanRepository, T> creator) {
            beanCreators.put(cls, new SingletonProvider(creator));
            return this;
        }

        public <T> BeanRepositoryBuilder singleton(final Class<T> cls, final Supplier<T> creator) {
            beanCreators.put(cls, new SingletonProvider((Function<BeanRepository, T>) repository -> creator.get()));
            return this;
        }

        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Function<BeanRepository, T> creator) {
            beanCreators.put(cls, new PrototypeProvider(creator));
            return this;
        }

        public <T> BeanRepositoryBuilder prototype(final Class<T> cls, final Supplier<T> creator) {
            beanCreators.put(cls, new PrototypeProvider((Function<BeanRepository, T>) repository -> creator.get()));
            return this;
        }

        public <T> BeanRepositoryBuilder instance(final T instance) {
            beanCreators.put(instance.getClass(), new InstanceProvider(instance));
            return this;
        }

        public BeanRepository build() {
            final BeanRepository repository = new BeanRepository(beanCreators);
            for (BeanProvider beanProvider : repository.beanCreators.values()) {
                beanProvider.getBean(repository, true);
            }
            return repository;
        }
    }
}
