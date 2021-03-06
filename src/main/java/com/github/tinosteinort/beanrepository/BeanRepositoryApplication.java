package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.application.env.DefaultBeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBusConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedEvent;

import java.util.Objects;

/**
 * This class is used to run an application with dependency injection support by the BeanRepository.
 */
public class BeanRepositoryApplication {

    /**
     * Startup initialisation of the BeanRepository. After the BeanRepository was initialised with
     *  the given configurator classes, a {@link ApplicationStartedEvent} is thrown. Use this event
     *  as starting point for the code of your application.
     *
     * @param args the program arguments
     * @param configurators the configurator classes, which provides the beans to the repository.
     */
    public static void run(final String[] args, final BeanRepositoryConfigurator ...configurators) {

        validate(args, configurators);

        final BeanRepository beanRepository = buildAndConfigureBeanRepository(args, configurators);

        final ApplicationEventBus eventBus = beanRepository.getBean(ApplicationEventBus.class);
        eventBus.fireEvent(new ApplicationStartedEvent());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eventBus.fireEvent(new ApplicationShutdownEvent());
        }));
    }

    private static void validate(final String[] args, final BeanRepositoryConfigurator[] configurators) {
        Objects.requireNonNull(args, "args must not be null");
        Objects.requireNonNull(configurators, "configurator required");
        if (configurators.length == 0) {
            throw new IllegalArgumentException("configurator required");
        }
    }

    private static BeanRepository buildAndConfigureBeanRepository(final String[] args,
                                                                  final BeanRepositoryConfigurator[] configurators) {
        final BeanRepository.BeanRepositoryBuilder builder = new BeanRepository.BeanRepositoryBuilder();
        new DefaultBeanRepositoryConfigurator(args).configure(builder);
        new ApplicationEventBusConfigurator().configure(builder);

        for (BeanRepositoryConfigurator configurator : configurators) {
            configurator.configure(builder);
        }

        return builder.build();
    }
}
