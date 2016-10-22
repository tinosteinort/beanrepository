package com.github.tinosteinort.beanrepository;

/**
 * Provides an Instance of a Bean.
 *
 * @param <T>   The Type of the Bean
 */
@FunctionalInterface
public interface Provider<T> {

    /**
     * Returns the Bean of the given Type of the Provider. The Scope of the Bean depends on the Configuration.
     *  If the Bean is not already initialised, the Bean will be initialised by calling this Method.
     *  It is possible that the {@link PostConstructible#onPostConstruct(BeanRepository)} Method is executed.
     *
     * @see PostConstructible
     * @return a constructed and full initialised Bean.
     */
    T get();
}
