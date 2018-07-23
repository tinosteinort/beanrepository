package com.github.tinosteinort.beanrepository;

import java.util.function.Function;

class PrototypeProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, ?> creator;

    PrototypeProvider(final String repositoryId, final Function<BeanAccessor, ?> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final DryRunAware dryRun) {
        final Object instance = creator.apply(repository.accessor());
        if (!dryRun.isDryRun()) {
            repository.postConstruct(instance);
        }
        return (T) instance;
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }

    @Override public Class<?> resolveBeanType(final BeanRepository repository, final DryRunAware dryRun) {
        return dryRun.execute(() -> {
            final Object tempInstance = creator.apply(repository.accessor());
            return tempInstance.getClass();
        });
    }
}
