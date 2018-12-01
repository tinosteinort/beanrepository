package com.github.tinosteinort.beanrepository.application.spi;

import com.github.tinosteinort.beanrepository.BeanRepository;

/**
 * This class is a service providing interface which is used to integrate into a BeanRepository from the
 *  class path via service loader mechanism.
 */
public interface BeanRepositoryConfiguratorSpi {

    /**
     * Use this method to configure beans of the application.
     *
     * @param builder the builder which is used to build the {@link BeanRepository}
     */
    void configure(BeanRepository.BeanRepositoryBuilder builder);
}
