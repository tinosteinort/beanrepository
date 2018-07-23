package com.github.tinosteinort.beanrepository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class SingletonFactoryProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, Factory> creator;
    private volatile Object instance;
    private final Lock lock = new ReentrantLock();

    SingletonFactoryProvider(final String repositoryId, final Function<BeanAccessor, Factory> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        if (instance == null) {
            return createAndGetBean(repository, dryRun);
        }
        return (T) instance;
    }

    private <T> T createAndGetBean(final BeanRepository repository, final boolean dryRun) {
        lock.lock();
        try {
            if (instance == null) {
                if (dryRun) {
                    // This call is only needed to detect cyclic dependencies on dryRun.
                    creator.apply(repository.accessor());

                    // In case of a dry run, whether the postConstruct() method nor the createInstance()
                    //  method of the factory must not be called. So we can just return NULL.
                    return null;
                }
                else {
                    final Factory factory = creator.apply(repository.accessor());
                    repository.postConstruct(factory);

                    final Object beanInstance = factory.createInstance();
                    repository.postConstruct(beanInstance);

                    instance = beanInstance;
                }
            }
            return (T) instance;
        }
        finally {
            lock.unlock();
        }
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }

    @Override public Class<?> resolveBeanType(final BeanRepository repository) {
        final Factory tempFactory = creator.apply(repository.accessor());
        return tempFactory.getBeanType();
    }
}
