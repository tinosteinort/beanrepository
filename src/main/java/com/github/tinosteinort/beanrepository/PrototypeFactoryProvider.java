package com.github.tinosteinort.beanrepository;

import java.util.function.Function;

class PrototypeFactoryProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, Factory> creator;

    PrototypeFactoryProvider(final String repositoryId, final Function<BeanAccessor, Factory> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        if (dryRun) {
            // This call is only needed to detect cyclic dependencies on dryRun.
            creator.apply(repository.accessor());

            // In case of a dry run, whether the postConstruct() method nor the createInstance()
            //  method of the factory must not be called. So we can just return NULL.
            return null;
        }

        final Factory factory = creator.apply(repository.accessor());
        repository.postConstruct(factory);

        final Object beanInstance = factory.createInstance();
        repository.postConstruct(beanInstance);

        return (T) beanInstance;
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }

    @Override public Class<?> resolveBeanType(final BeanRepository repository) {
        final Factory tempFactory = creator.apply(repository.accessor());
        return tempFactory.getBeanType();
    }
}
