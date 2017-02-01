package com.github.tinosteinort.beanrepository;

/**
 * Describes the Scope of a Bean.
 */
public enum Scope {

    /**
     * Only one Instance is created within the BeanRepository. If the Bean is
     *  requested, the same Instance will be returned.
     */
    SINGLETON,

    /**
     * Every Time a Bean is requested, a new Instance of this Bean will be created.
     */
    PROTOTYPE,

    /**
     * There is one registered Bean of this Class in the BeanRepository.
     */
    INSTANCE
}
