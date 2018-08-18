package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class ApplicationEventBusDelegate implements ApplicationEventBus {

    private final BeanAccessor beans;

    public ApplicationEventBusDelegate(final BeanAccessor beans) {
        this.beans = beans;
    }

    @Override public <T extends ApplicationEvent> void register(final ApplicationEventListener<T> listener) {
        beans.getBean(ApplicationEventBusImpl.class).register(listener);
    }

    @Override public <T extends ApplicationEvent> void fireEvent(T event) {
        beans.getBean(ApplicationEventBusImpl.class).fireEvent(event);
    }
}
