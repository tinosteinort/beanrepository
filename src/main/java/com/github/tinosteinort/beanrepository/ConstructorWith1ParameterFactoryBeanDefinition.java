package com.github.tinosteinort.beanrepository;

class ConstructorWith1ParameterFactoryBeanDefinition<T, DEP_1> extends BeanDefinition<T> {

    private final ConstructorWith1Parameter<Factory<T>, DEP_1> ctor1Param;
    private final Class<DEP_1> dependency1;

    ConstructorWith1ParameterFactoryBeanDefinition(final Scope scope, final Class<T> beanClass,
                                            final ConstructorWith1Parameter<Factory<T>, DEP_1> ctor1Param,
                                            final Class<DEP_1> dependency1) {
        super(scope, beanClass);
        this.ctor1Param = ctor1Param;
        this.dependency1 = dependency1;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonFactoryProvider(repositoryName, beans -> ctor1Param.create(
                        beans.getBean(dependency1)));
            case PROTOTYPE:
                return new PrototypeFactoryProvider(repositoryName, beans -> ctor1Param.create(
                        beans.getBean(dependency1)));
            default:
                throw new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
