package com.github.tinosteinort.beanrepository;

import java.util.function.Function;

public class SingletonFactoryProvider implements BeanProvider {

    private final String repositoryId;
    private final Function<BeanAccessor, Factory> creator;
//    private final SingletonProvider singletonProvider;

    SingletonFactoryProvider(final String repositoryId, final Function<BeanAccessor, Factory> creator) {
        this.repositoryId = repositoryId;
        this.creator = creator;
//        final SingletonProvider singletonProvider
//        this.singletonProvider = new SingletonProvider(repositoryId, (repo) -> this.getBean(repo, false));
    }

    @Override public <T> T getBean(final BeanRepository repository, final boolean dryRun) {
        final Factory factory = creator.apply(repository.accessor());
        if (dryRun) {
            return null;
        }
        return (T) factory.createInstance();
    }

    @Override public String getRepositoryId() {
        return repositoryId;
    }
}
