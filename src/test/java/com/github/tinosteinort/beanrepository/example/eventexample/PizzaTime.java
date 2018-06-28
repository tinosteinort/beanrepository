package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationEvent;

public class PizzaTime implements ApplicationEvent {

    private final Pizza pizza;

    public PizzaTime(final Pizza pizza) {
        this.pizza = pizza;
    }

    public Pizza getPizza() {
        return pizza;
    }
}
