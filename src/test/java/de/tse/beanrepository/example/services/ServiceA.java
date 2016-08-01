package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanAccessor;

public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(final BeanAccessor beans) {
        this.serviceB = beans.getBean(ServiceB.class);
    }

    public void print(final String value) {
        System.out.println("ServiceA: " + value);
    }
}
