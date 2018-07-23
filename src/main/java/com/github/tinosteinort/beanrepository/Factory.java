package com.github.tinosteinort.beanrepository;

/**
 * Creates an Instance of a Bean. Even if a Factory is registered in the {@link BeanRepository} it is by itself
 *  not a Bean. The created Object is the Bean. {@link PostConstructible#onPostConstruct(BeanRepository)} is
 *  executed for the Factory Object and for the returned Bean, if {@link PostConstructible} is implemented.
 *
 * @param <T>    The Type of the Bean
 */
public interface Factory<T> {

    /**
     * Creates an Instance of a Bean. {@link PostConstructible#onPostConstruct(BeanRepository)} of the Bean
     *  must not executed from this Method. This Method may executed from the {@link BeanRepository} later.
     *
     * @return The created Instance of this Factory.
     */
    T createInstance();

    /**
     * This method is needed to get the type of the bean.
     *
     * @return the {@code Class} of the bean
     */
    Class<T> getBeanType();
}
