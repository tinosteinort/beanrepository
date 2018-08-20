package com.github.tinosteinort.beanrepository.example._02_eventexample;

import com.github.tinosteinort.beanrepository.application.event.AbstractApplicationEventListener;

public class PizzaTimeListener extends AbstractApplicationEventListener<PizzaTime> {

    protected PizzaTimeListener() {
        super(PizzaTime.class);
    }

    @Override
    public void onEvent(final PizzaTime event) {
        System.out.println("Time for some " + event.getPizza() + ".");
    }
}
