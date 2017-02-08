package com.github.tinosteinort.beanrepository;

class ConstructorWith2ParametersFactoryBeanDefinition<T, DEP_1, DEP_2> extends BeanDefinition<T> {

    private final ConstructorWith2Parameters<Factory<T>, DEP_1, DEP_2> ctor2Params;
    private final Class<DEP_1> dependency1;
    private final Class<DEP_2> dependency2;

    ConstructorWith2ParametersFactoryBeanDefinition(final Scope scope, final Class<T> beanClass,
                                             final ConstructorWith2Parameters<Factory<T>, DEP_1, DEP_2> ctor2Params,
                                             final Class<DEP_1> dependency1,
                                             final Class<DEP_2> dependency2) {
        super(scope, beanClass);
        this.ctor2Params = ctor2Params;
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonFactoryProvider(repositoryName, beans -> ctor2Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2)));
            case PROTOTYPE:
                return new PrototypeFactoryProvider(repositoryName, beans -> ctor2Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2)));
            default:
                throw new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
