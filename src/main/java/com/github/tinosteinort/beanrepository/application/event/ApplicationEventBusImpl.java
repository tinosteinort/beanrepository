package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;

import java.util.Objects;

class ApplicationEventBusImpl implements ApplicationEventBus, PostConstructible {

    private final ApplicationEventListeners listeners = new ApplicationEventListeners();

    @Override public <T extends ApplicationEvent> void register(final ApplicationEventListener<T> listener) {
        listeners.register(listener);
    }

    @Override public <T extends ApplicationEvent> void fireEvent(final T event) {
        Objects.requireNonNull(event, "Event must not be null");
        for (ApplicationEventListener listener : listeners.listenersFor(event.getClass())) {
            listener.onEvent(event);
        }
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        for (ApplicationEventListener<? extends ApplicationEvent> listener : repository
                .getBeansOfType(ApplicationEventListener.class)) {
            listeners.register(listener);
        }
    }
}
