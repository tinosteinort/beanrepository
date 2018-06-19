package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.Executed;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationEventBusTest {

    private ApplicationEventBus eventBus;

    @Before public void setup() {
        eventBus = new ApplicationEventBus();
    }

    @Test public void listenerNotificatedOnEvent() {
        Executed executed = new Executed();
        assertFalse(executed.executed);

        TestEventListener listener = new TestEventListener(executed);
        eventBus.register(listener);
        eventBus.fireEvent(new TestEvent());

        assertTrue(executed.executed);
    }

    @Test public void collectionListenersAndTestNotification() {

        Executed executed1 = new Executed();
        TestEventListener listener1 = new TestEventListener(executed1);
        Executed executed2 = new Executed();
        OtherTestEventListener listener2 = new OtherTestEventListener(executed2);

        assertFalse(executed1.executed);
        assertFalse(executed2.executed);

        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ApplicationEventBus.class, ApplicationEventBus::new)
                .singleton(TestEventListener.class, () -> listener1)
                .singleton(OtherTestEventListener.class, () -> listener2)
                .build();

        repo.getBean(ApplicationEventBus.class).fireEvent(new TestEvent());

        assertTrue(executed1.executed);
        assertTrue(executed2.executed);
    }
}