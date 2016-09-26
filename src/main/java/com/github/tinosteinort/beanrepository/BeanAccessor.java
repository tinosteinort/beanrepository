package com.github.tinosteinort.beanrepository;

/**
 * This Class is used to get Access to other Beans from of a Constructor of a Bean.
 *  It allows only to get a single Bean, because at this Time not all Beans are registered
 *  to get a complete List with {@link BeanRepository#getBeansOfType(Class)}.
 */
public interface BeanAccessor {

    /**
     * Get the full initialised Bean from the Repository which is registered for the given Class {@code cls}.
     *
     * @param cls    The Class for which a Bean is registered for.
     * @param <T>    The Type of the Bean
     * @return The full initialised Bean with all Dependencies.
     */
    <T> T getBean(Class<T> cls);
}
