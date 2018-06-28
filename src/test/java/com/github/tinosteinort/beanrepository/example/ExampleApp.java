package com.github.tinosteinort.beanrepository.example;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryApplication;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.PostConstructible;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEventListener;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEventListener;
import com.github.tinosteinort.beanrepository.example.services.MailService;
import com.github.tinosteinort.beanrepository.example.services.PrintService;

public class ExampleApp implements BeanRepositoryConfigurator {

    public static void main(String[] args) {

        BeanRepositoryApplication.run(args, new ExampleApp());
    }

    @Override public void configure(final BeanRepository.BeanRepositoryBuilder builder) {

        builder
                .singleton(PrintService.class, PrintService::new)
                .singleton(MailService.class, MailService::new)
                .singleton(EventListenerService.class, EventListenerService::new, ApplicationEventBus.class)
                .singleton(StartupListener.class, StartupListener::new)
                .singleton(ShutdownListener.class, ShutdownListener::new);
    }
}

class StartupListener extends BeansInitialisedEventListener {

    @Override public void onEvent(ApplicationEvent event) {
        System.out.println("Application started");
    }
}

class ShutdownListener extends ApplicationShutdownEventListener {

    @Override public void onEvent(ApplicationEvent event) {
        System.out.println("Application shutdown");
    }
}

class EventListenerService implements PostConstructible {

    private final ApplicationEventBus eventBus;

    public EventListenerService(final ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {

        System.out.println("PostConstruct");
        eventBus.register(new ApplicationShutdownEventListener() {

            @Override public void onEvent(ApplicationEvent event) {
                System.out.println("Anonymous application shutdown listener triggered");
            }
        });
    }
}