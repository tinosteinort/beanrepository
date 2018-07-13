package com.github.tinosteinort.beanrepository;

/**
 * A configurator class for the {@link BeanRepository}. Instances of this class can be passed
 *  to the {@link BeanRepositoryApplication#run(String[], BeanRepositoryConfigurator...)} method.
 */
public interface BeanRepositoryConfigurator {

    /**
     * Use this method to configure beans of the application.
     *
     * @param builder the builder which is used to build the {@link BeanRepository}
     */
    void configure(BeanRepository.BeanRepositoryBuilder builder);
}
