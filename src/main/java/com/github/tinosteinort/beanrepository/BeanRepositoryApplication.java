package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.application.env.DefaultBeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedEvent;

import java.util.Objects;

public class BeanRepositoryApplication {

    public static void run(final String[] args, final BeanRepositoryConfigurator ...configurators) {

        Objects.requireNonNull(args, "args must not be null");
        Objects.requireNonNull(configurators, "configurator required");
        if (configurators.length == 0) {
            throw new IllegalArgumentException("configurator required");
        }

        final BeanRepository.BeanRepositoryBuilder builder = new BeanRepository.BeanRepositoryBuilder();
        new DefaultBeanRepositoryConfigurator(args).configure(builder);

        for (BeanRepositoryConfigurator configurator : configurators) {
            configurator.configure(builder);
        }

        final BeanRepository beanRepository = builder.build();

        final ApplicationEventBus eventBus = beanRepository.getBean(ApplicationEventBus.class);
        eventBus.fireEvent(new ApplicationStartedEvent());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eventBus.fireEvent(new ApplicationShutdownEvent());
        }));
    }
}
