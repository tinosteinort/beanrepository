package com.github.tinosteinort.beanrepository.application.event;

public interface ApplicationEventBus {

    <T extends ApplicationEvent> void register(ApplicationEventListener<T> listener);

    <T extends ApplicationEvent> void fireEvent(T event);
}
