package com.github.tinosteinort.beanrepository.application.event;

public class ApplicationEventEmitter {

    private final ApplicationEventBus eventBus;

    public ApplicationEventEmitter(final ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void emitApplicationStartedEvent() {
        this.eventBus.fireEvent(new ApplicationStartedEvent());
    }

    public void emitApplicationShutdownEvent() {
        this.eventBus.fireEvent(new ApplicationShutdownEvent());
    }
}
