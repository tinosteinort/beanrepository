package de.tse.beanrepository;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class SingletonProvider implements BeanProvider {

    private final Function<BeanAccessor, ?> creator;
    private volatile Object instance;
    private final Lock lock = new ReentrantLock();

    SingletonProvider(final Function<BeanAccessor, ?> creator) {
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
}
