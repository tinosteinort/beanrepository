package com.github.tinosteinort.beanrepository;

class ConstructorWith4ParametersFactoryBeanDefinition<T, DEP_1, DEP_2, DEP_3, DEP_4> extends BeanDefinition<T> {

    private final ConstructorWith4Parameters<Factory<T>, DEP_1, DEP_2, DEP_3, DEP_4> ctor4Params;
    private final Class<DEP_1> dependency1;
    private final Class<DEP_2> dependency2;
    private final Class<DEP_3> dependency3;
    private final Class<DEP_4> dependency4;

    ConstructorWith4ParametersFactoryBeanDefinition(final Scope scope, final Class<T> beanClass,
                                             final ConstructorWith4Parameters<Factory<T>, DEP_1, DEP_2, DEP_3,
                                                     DEP_4> ctor4Params,
                                             final Class<DEP_1> dependency1,
                                             final Class<DEP_2> dependency2,
                                             final Class<DEP_3> dependency3,
                                             final Class<DEP_4> dependency4) {
        super(scope, beanClass);
        this.ctor4Params = ctor4Params;
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
        this.dependency3 = dependency3;
        this.dependency4 = dependency4;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonFactoryProvider(repositoryName, beans -> ctor4Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3),
                        beans.getBean(dependency4)));
            case PROTOTYPE:
                return new PrototypeFactoryProvider(repositoryName, beans -> ctor4Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3),
                        beans.getBean(dependency4)));
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
