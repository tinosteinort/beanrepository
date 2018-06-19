package com.github.tinosteinort.beanrepository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class PrototypeFactoryProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, Factory> creator;
    private volatile Object instance;
    private final Lock lock = new ReentrantLock();

    PrototypeFactoryProvider(final String repositoryId, final Function<BeanAccessor, Factory> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        final Factory factory = creator.apply(repository.accessor());

        final Object beanInstance;
        if (dryRun) {
            return null;
        }

        repository.postConstruct(factory);
        beanInstance = factory.createInstance();
        repository.postConstruct(beanInstance);
        return (T) beanInstance;
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }
}
