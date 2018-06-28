package com.github.tinosteinort.beanrepository.example.eventexample;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pizza {

    private final String[] toppings;

    public Pizza(final String ...toppings) {
        this.toppings = toppings;
    }

    @Override
    public String toString() {
        return "Pizza with " + Stream.of(toppings)
                .collect(Collectors.joining(" and "));
    }
}
