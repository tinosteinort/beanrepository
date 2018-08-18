package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;

public class ApplicationEventBusConfigurator implements BeanRepositoryConfigurator {

    @Override public void configure(final BeanRepository.BeanRepositoryBuilder builder) {

        builder.singleton(ApplicationEventBusImpl.class, ApplicationEventBusImpl::new);
        builder.singleton(ApplicationEventBus.class, ApplicationEventBusDelegate::new);
    }
}
