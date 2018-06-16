package com.github.tinosteinort.beanrepository.application.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ApplicationEventListeners {

    private final Map<Class<? extends ApplicationEvent>, List<ApplicationEventListener<? extends ApplicationEvent>>>
            listeners = new HashMap<>();

    public <T extends ApplicationEvent> void register(final ApplicationEventListener<T> listener) {

        final List<ApplicationEventListener<? extends ApplicationEvent>> listenersForEventClass = listeners
                .computeIfAbsent(listener.getEventClass(), key -> new ArrayList<>());
        listenersForEventClass.add(listener);
    }

    public <T extends ApplicationEvent> List<ApplicationEventListener<? extends ApplicationEvent>> listenersFor(
            final Class<T> eventClass) {

        final List<ApplicationEventListener<? extends ApplicationEvent>> eventListeners = listeners.get(eventClass);
        if (eventListeners == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(eventListeners);
    }
}
