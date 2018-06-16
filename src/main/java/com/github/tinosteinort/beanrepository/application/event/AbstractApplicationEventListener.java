package com.github.tinosteinort.beanrepository.application.event;

public abstract class AbstractApplicationEventListener<T extends ApplicationEvent>
        implements ApplicationEventListener<T> {

    private final Class<T> eventClass;

    protected AbstractApplicationEventListener(final Class<T> eventClass) {
        this.eventClass = eventClass;
    }

    @Override public Class<T> getEventClass() {
        return eventClass;
    }
}
