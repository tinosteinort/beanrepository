package com.github.tinosteinort.beanrepository;

import java.util.function.Supplier;

class SupplierFactoryBeanDefinition<T> extends BeanDefinition<T> {

    private final Supplier<Factory<T>> supplier;

    SupplierFactoryBeanDefinition(final Scope scope, final Class<T> beanClass, final Supplier<Factory<T>> supplier) {
        super(scope, beanClass);
        this.supplier = supplier;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonFactoryProvider(repositoryName, repo -> supplier.get());
            case PROTOTYPE:
                return new PrototypeFactoryProvider(repositoryName, repo -> supplier.get());
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
