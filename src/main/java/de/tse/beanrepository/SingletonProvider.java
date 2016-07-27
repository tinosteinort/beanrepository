package de.tse.beanrepository;

import java.util.function.Function;

class SingletonProvider implements BeanProvider {

    private final Function<BeanRepository, ?> creator;
    private Object instance;

    SingletonProvider(final Function<BeanRepository, ?> creator) {
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        if (instance == null) {
            final Object beanInstance = creator.apply(repository);
            new PostConstructor(repository).postConstruct(beanInstance, dryRun);
            if (!dryRun) {
                instance = beanInstance;
            }
            return (T) beanInstance;
        }
        return (T) instance;
    }
}
