package com.github.tinosteinort.beanrepository;

class InstanceProvider implements BeanProvider {

    private final String repositoryId;
    private final Object instance;

    InstanceProvider(final String repositoryId, final Object instance) {
        this.repositoryId = repositoryId;
        if (instance == null) {
            throw new IllegalArgumentException("Instance must not be null");
        }
        this.instance = instance;
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        return (T) instance;
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }
}
