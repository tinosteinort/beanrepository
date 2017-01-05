package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.Factory;

public class ServiceWithConstructorDependencyFactory implements Factory<ServiceWithConstructorDependency> {

    private final PrintService printService;

    public ServiceWithConstructorDependencyFactory(final PrintService printService) {
        this.printService = printService;
    }

    @Override public ServiceWithConstructorDependency createInstance() {
        return new ServiceWithConstructorDependency(printService);
    }
}
