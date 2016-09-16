package de.tse.beanrepository;

import java.util.Objects;

class InstanceProvider implements BeanProvider {

    private final Object instance;

    InstanceProvider(final Object instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance must not be null");
        }
        this.instance = instance;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        return (T) instance;
    }
}
