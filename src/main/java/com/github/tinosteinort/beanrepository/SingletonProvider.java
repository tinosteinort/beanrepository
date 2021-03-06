package com.github.tinosteinort.beanrepository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class SingletonProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, ?> creator;
    private volatile Object instance;
    private final Lock lock = new ReentrantLock();

    SingletonProvider(final String repositoryId, final Function<BeanAccessor, ?> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
    }

    @Override public <T> T getBean(final BeanRepository repository, final DryRunAware dryRun) {
        if (instance == null) {
            return createAndGetBean(repository, dryRun.isDryRun());
        }
        return (T) instance;
    }

    private <T> T createAndGetBean(final BeanRepository repository, final boolean dryRun) {
        lock.lock();
        try {
            if (instance == null) {
                final Object beanInstance = creator.apply(repository.accessor());
                if (!dryRun) {
                    repository.postConstruct(beanInstance);
                    instance = beanInstance;
                }
                return (T) beanInstance;
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

    @Override public Class<?> resolveBeanType(final BeanRepository repository, final DryRunAware dryRun) {
        if (instance == null) {
            return dryRun.execute(() -> {
                final Object tempInstance = creator.apply(repository.accessor());
                return tempInstance.getClass();
            });
        }
        return instance.getClass();
    }
}
