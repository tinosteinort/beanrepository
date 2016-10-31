package com.github.tinosteinort.beanrepository.example.services;

public class SomeService {

    private final int number;

    public SomeService(final int number) {
        this.number = number;
    }

    public void print() {
        System.out.println(number);
    }
}
