package com.github.tinosteinort.beanrepository.application.event;

public abstract class ApplicationStartedListener
        extends AbstractApplicationEventListener<ApplicationStartedEvent> {

    public ApplicationStartedListener() {
        super(ApplicationStartedEvent.class);
    }
}
