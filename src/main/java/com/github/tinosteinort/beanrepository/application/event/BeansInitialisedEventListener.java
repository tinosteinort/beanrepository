package com.github.tinosteinort.beanrepository.application.event;

public abstract class BeansInitialisedEventListener
        extends AbstractApplicationEventListener<BeansInitialisedEvent> {

    public BeansInitialisedEventListener() {
        super(BeansInitialisedEvent.class);
    }
}
