package com.github.tinosteinort.beanrepository.application.event;

public abstract class ApplicationShutdownEventListener
        extends AbstractApplicationEventListener<ApplicationShutdownEvent> {

    public ApplicationShutdownEventListener() {
        super(ApplicationShutdownEvent.class);
    }
}
