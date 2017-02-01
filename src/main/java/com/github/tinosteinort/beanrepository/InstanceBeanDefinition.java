package com.github.tinosteinort.beanrepository;

class InstanceBeanDefinition<T> extends BeanDefinition<T> {

    private final T instance;

    InstanceBeanDefinition(final T instance) {
        super(Scope.INSTANCE, (Class<T>) instance.getClass());
        this.instance = instance;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        return new InstanceProvider(repositoryName, instance);
    }
}
