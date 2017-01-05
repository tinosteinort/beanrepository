package com.github.tinosteinort.beanrepository.example.services;

public class ServiceWithConstructorDependency {

    private final PrintService printService;

    public ServiceWithConstructorDependency(final PrintService service) {
        this.printService = service;
    }

    public void doSomething() {
        printService.print("something");
    }
}
