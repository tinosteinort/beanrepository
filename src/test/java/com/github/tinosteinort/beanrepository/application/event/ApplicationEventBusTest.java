package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.HitCounter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationEventBusTest {

    private ApplicationEventBus eventBus;

    @Before public void setup() {
        eventBus = new ApplicationEventBus();
    }

    @Test public void listenerNotificatedOnEvent() {
        HitCounter counter = new HitCounter();

        TestEventListener listener = new TestEventListener(counter);
        eventBus.register(listener);
        eventBus.fireEvent(new TestEvent());

        assertEquals(1, counter.hits());
        assertTrue(counter.hasHit("TestEventListener - TestEvent occurred"));
    }

    @Test public void collectionListenersAndTestNotification() {

        HitCounter counter = new HitCounter();

        TestEventListener listener1 = new TestEventListener(counter);
        OtherTestEventListener listener2 = new OtherTestEventListener(counter);

        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ApplicationEventBus.class, ApplicationEventBus::new)
                .singleton(TestEventListener.class, () -> listener1)
                .singleton(OtherTestEventListener.class, () -> listener2)
                .build();

        repo.getBean(ApplicationEventBus.class).fireEvent(new TestEvent());

        assertTrue(counter.hasHit("TestEventListener - TestEvent occurred"));
        assertTrue(counter.hasHit("OtherTestEventListener - TestEvent occurred"));
    }
}