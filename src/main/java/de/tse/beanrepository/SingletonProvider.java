package de.tse.beanrepository;

import java.util.function.Function;

class SingletonProvider implements BeanProvider {

    private final Function<BeanRepository, ?> creator;
    private Object instance;

    SingletonProvider(final Function<BeanRepository, ?> creator) {
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository) {
        if (instance == null) {
            instance = creator.apply(repository);
            new PostConstructor(repository).postConstruct(instance);
        }
        return (T) instance;
    }
}
