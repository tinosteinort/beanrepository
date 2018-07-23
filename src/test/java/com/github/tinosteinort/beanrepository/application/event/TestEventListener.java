package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.HitCounter;

class TestEventListener extends AbstractApplicationEventListener<TestEvent> {

    private HitCounter counter;

    public TestEventListener() {
        super(TestEvent.class);
    }

    public TestEventListener(final HitCounter counter) {
        super(TestEvent.class);
        this.counter = counter;
    }

    @Override public void onEvent(TestEvent event) {
        counter.hit("TestEventListener - TestEvent occurred");
    }
}
