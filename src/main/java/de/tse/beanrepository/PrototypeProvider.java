package de.tse.beanrepository;

import java.util.function.Function;

class PrototypeProvider implements BeanProvider {

    private final Function<BeanRepository, ?> creator;

    PrototypeProvider(final Function<BeanRepository, ?> creator) {
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository) {
        final Object instance = creator.apply(repository);
        new PostConstructor(repository).postConstruct(instance);
        return (T) instance;
    }
}
