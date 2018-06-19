package com.github.tinosteinort.beanrepository.application.event;

import com.github.tinosteinort.beanrepository.Executed;

class OtherTestEventListener extends AbstractApplicationEventListener<TestEvent> {

    private Executed executed;

    public OtherTestEventListener() {
        super(TestEvent.class);
    }

    public OtherTestEventListener(final Executed executed) {
        super(TestEvent.class);
        this.executed = executed;
    }

    @Override public void onEvent(ApplicationEvent event) {
        if (executed == null) {
            return;
        }
        executed.executed = true;
    }
}
