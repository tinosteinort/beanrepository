package com.github.tinosteinort.beanrepository.example.services;

public class SomeServiceFactory {

    public SomeService create(final int number) {
        return new SomeService(number);
    }
}
