package com.github.tinosteinort.beanrepository;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Describes a Bean. This includes the Scope and the way how the Bean is created.
 */
public abstract class BeanDefinition<T> {

    private final Scope scope;
    private final Class<T> beanClass;

    BeanDefinition(final Scope scope, final Class<T> beanClass) {
        this.scope = scope;
        this.beanClass = beanClass;
    }

    protected abstract BeanProvider asBeanProvider(final String repositoryName);

    Scope getScope() {
        return scope;
    }

    Class<?> getBeanClass() {
        return beanClass;
    }

    public static <T> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                               final Supplier<T> supplier) {

        return new ProviderBeanDefinition<>(scope, beanClass, supplier);
    }

    public static <T> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                               final Function<BeanAccessor, T> function) {

        return new FunctionBeanDefinition<>(scope, beanClass, function);
    }

    public static <T> BeanDefinition<T> createInstance(final T instance) {

        return new InstanceBeanDefinition<>(instance);
    }

    public static <T, DEP_1> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                                      final ConstructorWith1Parameter<T, DEP_1> ctor1Param,
                                                      final Class<DEP_1> dependency1) {

        return new ConstructorWith1ParameterBeanDefinition<>(scope, beanClass, ctor1Param, dependency1);
    }

    public static <T, DEP_1, DEP_2> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                                         final ConstructorWith2Parameters<T, DEP_1, DEP_2> ctor2Params,
                                                         final Class<DEP_1> dependency1,
                                                         final Class<DEP_2> dependency2) {

        return new ConstructorWith2ParametersBeanDefinition<>(scope, beanClass, ctor2Params, dependency1, dependency2);
    }

    public static <T, DEP_1, DEP_2, DEP_3> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                                         final ConstructorWith3Parameters<T, DEP_1, DEP_2,
                                                                 DEP_3> ctor3Params,
                                                         final Class<DEP_1> dependency1,
                                                         final Class<DEP_2> dependency2,
                                                         final Class<DEP_3> dependency3) {

        return new ConstructorWith3ParametersBeanDefinition<>(scope, beanClass, ctor3Params, dependency1, dependency2,
                dependency3);
    }

    public static <T, DEP_1, DEP_2, DEP_3, DEP_4> BeanDefinition<T> create(final Scope scope, final Class<T> beanClass,
                                                         final ConstructorWith4Parameters<T, DEP_1, DEP_2,
                                                                 DEP_3, DEP_4> ctor4Params,
                                                         final Class<DEP_1> dependency1,
                                                         final Class<DEP_2> dependency2,
                                                         final Class<DEP_3> dependency3,
                                                         final Class<DEP_4> dependency4) {

        return new ConstructorWith4ParametersBeanDefinition<>(scope, beanClass, ctor4Params, dependency1, dependency2,
                dependency3, dependency4);
    }

    public static <T, DEP_1, DEP_2, DEP_3, DEP_4, DEP_5> BeanDefinition<T> create(final Scope scope,
                                                         final Class<T> beanClass,
                                                         final ConstructorWith5Parameters<T, DEP_1, DEP_2,
                                                                 DEP_3, DEP_4, DEP_5> ctor5Params,
                                                         final Class<DEP_1> dependency1,
                                                         final Class<DEP_2> dependency2,
                                                         final Class<DEP_3> dependency3,
                                                         final Class<DEP_4> dependency4,
                                                         final Class<DEP_5> dependency5) {

        return new ConstructorWith5ParametersBeanDefinition<>(scope, beanClass, ctor5Params, dependency1, dependency2,
                dependency3, dependency4, dependency5);
    }
}
