package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.HitCounter;

class OtherTestEventListener extends AbstractApplicationEventListener<TestEvent> {

    private HitCounter counter;

    public OtherTestEventListener() {
        super(TestEvent.class);
    }

    public OtherTestEventListener(final HitCounter counter) {
        super(TestEvent.class);
        this.counter = counter;
    }

    @Override public void onEvent(TestEvent event) {
        counter.hit("OtherTestEventListener - TestEvent occurred");
    }
}
