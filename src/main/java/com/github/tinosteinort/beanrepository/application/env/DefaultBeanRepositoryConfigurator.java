package com.github.tinosteinort.beanrepository.application.env;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventEmitter;

public class DefaultBeanRepositoryConfigurator implements BeanRepositoryConfigurator {

    private final String[] args;

    public DefaultBeanRepositoryConfigurator(final String[] args) {
        this.args = args;
    }

    @Override public void configure(final BeanRepository.BeanRepositoryBuilder builder) {

        builder.singleton(ArgsProvider.class, () -> new ArgsProvider(args));
        builder.singleton(ApplicationEventBus.class, ApplicationEventBus::new);
        builder.singleton(ApplicationEventEmitter.class, ApplicationEventEmitter::new, ApplicationEventBus.class);
    }
}
