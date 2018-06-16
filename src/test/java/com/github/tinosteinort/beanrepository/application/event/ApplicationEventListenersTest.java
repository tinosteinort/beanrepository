package com.github.tinosteinort.beanrepository.application.event;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApplicationEventListenersTest {

    private ApplicationEventListeners listeners;

    @Before public void setup() {
        listeners = new ApplicationEventListeners();
    }

    @Test public void registerSingleListener() {

        TestEventListener listener = new TestEventListener();
        listeners.register(listener);

        List<ApplicationEventListener<? extends ApplicationEvent>> result = listeners.listenersFor(TestEvent.class);
        assertEquals(result.size(), 1);
        assertEquals(listener, result.get(0));
    }

    @Test public void registerMultipleListener() {

        TestEventListener listener1 = new TestEventListener();
        listeners.register(listener1);
        TestEventListener listener2 = new TestEventListener();
        listeners.register(listener2);

        List<ApplicationEventListener<? extends ApplicationEvent>> result = listeners.listenersFor(TestEvent.class);
        assertEquals(2, result.size());
        assertEquals(listener1, result.get(0));
        assertEquals(listener2, result.get(1));
    }

    @Test public void emptyListIfNoListenerAvailable() {

        List<ApplicationEventListener<? extends ApplicationEvent>> result = listeners.listenersFor(TestEvent.class);

        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);
    }

    @Test public void emptyListIfNoClassIsNull() {

        List<ApplicationEventListener<? extends ApplicationEvent>> result = listeners.listenersFor(null);

        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);
    }
}

