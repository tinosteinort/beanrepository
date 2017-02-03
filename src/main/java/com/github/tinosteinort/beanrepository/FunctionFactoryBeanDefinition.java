package com.github.tinosteinort.beanrepository;

import java.util.function.Function;

class FunctionFactoryBeanDefinition<T> extends BeanDefinition<T> {

    private final Function<BeanAccessor, Factory<T>> function;

    FunctionFactoryBeanDefinition(final Scope scope, final Class<T> beanClass,
            final Function<BeanAccessor, Factory<T>> function) {
        super(scope, beanClass);
        this.function = function;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonFactoryProvider(repositoryName, function::apply);
            case PROTOTYPE:
                return new PrototypeFactoryProvider(repositoryName, function::apply);
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
