package com.github.tinosteinort.beanrepository;

import java.util.function.Supplier;

class SupplierBeanDefinition<T> extends BeanDefinition<T> {

    private final Supplier<T> supplier;

    SupplierBeanDefinition(final Scope scope, final Class<T> beanClass, final Supplier<T> supplier) {
        super(scope, beanClass);
        this.supplier = supplier;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonProvider(repositoryName, repo -> supplier.get());
            case PROTOTYPE:
                return new PrototypeProvider(repositoryName, repo -> supplier.get());
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
