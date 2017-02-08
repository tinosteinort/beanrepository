package com.github.tinosteinort.beanrepository;

class ConstructorWith3ParametersBeanDefinition<T, DEP_1, DEP_2, DEP_3> extends BeanDefinition<T> {

    private final ConstructorWith3Parameters<T, DEP_1, DEP_2, DEP_3> ctor3Params;
    private final Class<DEP_1> dependency1;
    private final Class<DEP_2> dependency2;
    private final Class<DEP_3> dependency3;

    ConstructorWith3ParametersBeanDefinition(final Scope scope, final Class<T> beanClass,
                                             final ConstructorWith3Parameters<T, DEP_1, DEP_2, DEP_3> ctor3Params,
                                             final Class<DEP_1> dependency1,
                                             final Class<DEP_2> dependency2,
                                             final Class<DEP_3> dependency3) {
        super(scope, beanClass);
        this.ctor3Params = ctor3Params;
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
        this.dependency3 = dependency3;
    }

    @Override protected BeanProvider asBeanProvider(final String repositoryName) {
        switch (getScope()) {
            case SINGLETON:
                return new SingletonProvider(repositoryName, beans -> ctor3Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3)));
            case PROTOTYPE:
                return new PrototypeProvider(repositoryName, beans -> ctor3Params.create(
                        beans.getBean(dependency1),
                        beans.getBean(dependency2),
                        beans.getBean(dependency3)));
            default:
                throw new IllegalArgumentException("Scope not supported: " + getScope());
        }
    }
}
