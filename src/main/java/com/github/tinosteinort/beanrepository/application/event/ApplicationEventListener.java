package com.github.tinosteinort.beanrepository.application.event;

public interface ApplicationEventListener<T extends ApplicationEvent> {

    Class<T> getEventClass();
    void onEvent(T event);
}
