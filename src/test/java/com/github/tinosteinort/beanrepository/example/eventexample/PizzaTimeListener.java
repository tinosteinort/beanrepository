package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.application.event.AbstractApplicationEventListener;

public class PizzaTimeListener extends AbstractApplicationEventListener<PizzaTimeEvent> {

    protected PizzaTimeListener() {
        super(PizzaTimeEvent.class);
    }

    @Override
    public void onEvent(final PizzaTimeEvent event) {
        System.out.println("Time for some " + event.getPizza() + ".");
    }
}
