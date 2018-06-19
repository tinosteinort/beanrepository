package com.github.tinosteinort.beanrepository.application.event;

public abstract class ApplicationStartedEventListener
        extends AbstractApplicationEventListener<ApplicationStartedEvent> {

    public ApplicationStartedEventListener() {
        super(ApplicationStartedEvent.class);
    }
}
