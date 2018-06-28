package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationEvent;

public class PizzaTimeEvent implements ApplicationEvent {

    private final Pizza pizza;

    public PizzaTimeEvent(final Pizza pizza) {
        this.pizza = pizza;
    }

    public Pizza getPizza() {
        return pizza;
    }
}
