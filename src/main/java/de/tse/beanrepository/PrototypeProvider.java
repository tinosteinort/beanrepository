package de.tse.beanrepository;

import java.util.function.Function;

class PrototypeProvider implements BeanProvider {

    private final Function<BeanAccessor, ?> creator;

    PrototypeProvider(final Function<BeanAccessor, ?> creator) {
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        final Object instance = creator.apply(repository.accessor());
        new PostConstructor(repository).postConstruct(instance, dryRun);
        return (T) instance;
    }
}
