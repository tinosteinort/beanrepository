package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.application.env.DefaultBeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventEmitter;

import java.util.Objects;

public class BeanRepositoryApplication {

    public static void run(final String[] args, final BeanRepositoryConfigurator configurator) {

        Objects.requireNonNull(args, "args must not be null");
        Objects.requireNonNull(configurator, "configurator required");

        final BeanRepository.BeanRepositoryBuilder builder = new BeanRepository.BeanRepositoryBuilder();
        new DefaultBeanRepositoryConfigurator(args).configure(builder);
        configurator.configure(builder);

        final BeanRepository beanRepository = builder.build();

        final ApplicationEventEmitter appEventEmitter = beanRepository.getBean(ApplicationEventEmitter.class);
        appEventEmitter.emitApplicationStartedEvent();

        Runtime.getRuntime().addShutdownHook(new Thread(appEventEmitter::emitApplicationShutdownEvent));
    }
}
