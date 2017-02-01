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
}
