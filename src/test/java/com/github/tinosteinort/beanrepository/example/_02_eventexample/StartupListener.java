package com.github.tinosteinort.beanrepository.example._02_eventexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedListener;

public class StartupListener extends ApplicationStartedListener {

    private final ApplicationEventBus eventBus;

    public StartupListener(final ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onEvent(final ApplicationStartedEvent event) {

        System.out.println("Application started");
        System.out.println("-------------------");


        final Pizza special = new Pizza("salami", "ham", "mushrooms");
        final Pizza hawaii = new Pizza("ham", "pineapple");

        eventBus.fireEvent(new PizzaTime(special));
        System.out.println("-------------------");

        eventBus.fireEvent(new PizzaTime(hawaii));
        System.out.println("-------------------");
    }
}
