package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class ServiceWithBeanDependenciesAndParameter {

    private final PrintService printService;
    private final String id;

    public ServiceWithBeanDependenciesAndParameter(final BeanAccessor beans, final String id) {
        this.printService = beans.getBean(PrintService.class);
        this.id = id;
    }

    public void print(final String value) {
        printService.print(id + ": " + value);
    }
}
