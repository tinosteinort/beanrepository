package com.github.tinosteinort.beanrepository;

import java.util.function.Function;

class FunctionBeanDefinition<T> extends BeanDefinition<T> {

    private final Function<BeanAccessor, T> function;

    FunctionBeanDefinition(final Scope scope, final Class<T> beanClass,
                           final Function<BeanAccessor, T> function) {
        super(scope, beanClass);
        this.function = function;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonProvider(repositoryName, function::apply);
            case PROTOTYPE:
                return new PrototypeProvider(repositoryName, function::apply);
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}