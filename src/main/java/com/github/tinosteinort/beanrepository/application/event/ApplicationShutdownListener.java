package com.github.tinosteinort.beanrepository.application.event;

public abstract class ApplicationShutdownListener extends AbstractApplicationEventListener<ApplicationShutdownEvent> {

    public ApplicationShutdownListener() {
        super(ApplicationShutdownEvent.class);
    }
}
