package com.github.tinosteinort.beanrepository;

import java.util.function.Function;
import java.util.function.Supplier;

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

    /**
     * Creates a new, full initialised, {@code prototype} Bean with the given {@code creator}. It is possible
     *  to pass Arguments to the Constructor of the Bean.
     *
     * @param creator   The Code to create the new Object
     * @param <T>       The Type of the Bean
     * @return a new created Bean
     */
    <T> T getBean(Supplier<T> creator);

    /**
     * Creates a new, full initialised, {@code prototype} Bean with the given {@code creator}. It is possible
     *  to pass Arguments to the Constructor of the Bean. Also Dependencies for this Bean can be defined.
     *
     * @param creator   The Code to create the new Object
     * @param <T>       The Type of the Bean
     * @return a new created Bean
     */
    <T> T getBean(Function<BeanAccessor, T> creator);

    /**
     * Get an Accessor which provides a Bean.
     *
     * @param cls    The Class for which a Bean is registered for.
     * @param <T>    The Type of the Bean
     * @return The full initialised Bean with all Dependencies.
     */
    <T> Provider<T> getProvider(Class<T> cls);
}
