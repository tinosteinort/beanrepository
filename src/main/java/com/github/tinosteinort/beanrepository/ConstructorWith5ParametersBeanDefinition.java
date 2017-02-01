package com.github.tinosteinort.beanrepository;

class ConstructorWith5ParametersBeanDefinition<T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> extends BeanDefinition<T> {

    private final ConstructorWith5Parameters<T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> ctor5Params;
    private final Class<DEP_1> dependency1;
    private final Class<DEP_2> dependency2;
    private final Class<DEP_3> dependency3;
    private final Class<DEP_4> dependency4;
    private final Class<DEP_5> dependency5;

    ConstructorWith5ParametersBeanDefinition(final Scope scope, final Class<T> beanClass,
                                             final ConstructorWith5Parameters<T, DEP_1, DEP_2, DEP_3,
                                                     DEP_4, DEP_5> ctor5Params,
                                             final Class<DEP_1> dependency1,
                                             final Class<DEP_2> dependency2,
                                             final Class<DEP_3> dependency3,
                                             final Class<DEP_4> dependency4,
                                             final Class<DEP_5> dependency5) {
        super(scope, beanClass);
        this.ctor5Params = ctor5Params;
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
        this.dependency3 = dependency3;
        this.dependency4 = dependency4;
        this.dependency5 = dependency5;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonProvider(repositoryName, beans -> ctor5Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3),
                        beans.getBean(dependency4),
                        beans.getBean(dependency5)));
            case PROTOTYPE:
                return new PrototypeProvider(repositoryName, beans -> ctor5Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3),
                        beans.getBean(dependency4),
                        beans.getBean(dependency5)));
            default:
                throw  new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
