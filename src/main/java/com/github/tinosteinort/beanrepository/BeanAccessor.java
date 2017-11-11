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
     * @param <R>    The Type or a super Type of the Bean
     * @param <T>    The Type of the Bean
     * @return The full initialised Bean with all Dependencies.
     */
    <R, T extends R> T getBean(Class<R> cls);

    /**
     * Creates a new, full initialised, {@code prototype} Bean with the given {@code creator}. It is possible
     *  to pass Arguments to the Constructor of the Bean.
     *
     * @param creator   The Code to create the new Object
     * @param <T>       The Type of the Bean
     * @return a new created Bean
     */
    <T> T getPrototypeBean(Supplier<T> creator);

    /**
     * Creates a new, full initialised, {@code prototype} Bean with the given {@code creator}. It is possible
     *  to pass Arguments to the Constructor of the Bean. Also Dependencies for this Bean can be defined.
     *
     * @param creator   The Code to create the new Object
     * @param <T>       The Type of the Bean
     * @return a new created Bean
     */
    <T> T getPrototypeBean(Function<BeanAccessor, T> creator);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1> T getPrototypeBean(ConstructorWith1Parameter<T, P1> creator, P1 param1);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2> T getPrototypeBean(ConstructorWith2Parameters<T, P1, P2> creator, P1 param1, P2 param2);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3> T getPrototypeBean(ConstructorWith3Parameters<T, P1, P2, P3> creator, P1 param1, P2 param2,
            P3 param3);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3, P4> T getPrototypeBean(ConstructorWith4Parameters<T, P1, P2, P3, P4> creator, P1 param1,
            P2 param2, P3 param3, P4 param4);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param param5     The 5th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @param <P5>       The Type of the 5th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3, P4, P5> T getPrototypeBean(ConstructorWith5Parameters<T, P1, P2, P3, P4, P5> creator, P1 param1,
            P2 param2, P3 param3, P4 param4, P5 param5);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1> T getPrototypeBean(ConstructorWithBeansAnd1Parameter<T, P1> creator, P1 param1);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2> T getPrototypeBean(ConstructorWithBeansAnd2Parameters<T, P1, P2> creator, P1 param1, P2 param2);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3> T getPrototypeBean(ConstructorWithBeansAnd3Parameters<T, P1, P2, P3> creator, P1 param1,
            P2 param2, P3 param3);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3, P4> T getPrototypeBean(ConstructorWithBeansAnd4Parameters<T, P1, P2, P3, P4> creator, P1 param1,
            P2 param2, P3 param3, P4 param4);

    /**
     * Returns a new {@code prototype} Bean, created by the given Function. It is possible to pass Parameter
     *  to the Constructor of the Bean, and determine Dependencies with the {@link BeanAccessor}. The Method
     *  {@link PostConstructible#onPostConstruct(BeanRepository)} is executed for every Call of this Method, if
     *  the Interface is implemented.
     *
     * @param creator    The Code to create the new Object
     * @param param1     The 1st Parameter
     * @param param2     The 2nd Parameter
     * @param param3     The 3rd Parameter
     * @param param4     The 4th Parameter
     * @param param5     The 5th Parameter
     * @param <T>        The Type of the Bean
     * @param <P1>       The Type of the 1st Parameter
     * @param <P2>       The Type of the 2nd Parameter
     * @param <P3>       The Type of the 3rd Parameter
     * @param <P4>       The Type of the 4th Parameter
     * @param <P5>       The Type of the 5th Parameter
     * @see BeanAccessor
     * @see PostConstructible
     * @return a new created Object
     */
    <T, P1, P2, P3, P4, P5> T getPrototypeBean(ConstructorWithBeansAnd5Parameters<T, P1, P2, P3, P4, P5> creator,
            P1 param1, P2 param2, P3 param3, P4 param4, P5 param5);
    
    /**
     * Get an Accessor which provides a Bean.
     *
     * @param cls    The Class for which a Bean is registered for.
     * @param <R>    The Type or a super Type of the Bean
     * @param <T>    The Type of the Bean
     * @return The full initialised Bean with all Dependencies.
     */
    <R, T extends R> Provider<T> getProvider(Class<R> cls);
}
