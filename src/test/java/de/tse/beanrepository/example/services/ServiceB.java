package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanAccessor;

public class ServiceB {

    private final ServiceA serviceA;

    public ServiceB(final BeanAccessor beans) {
        this.serviceA = beans.getBean(ServiceA.class);
    }

    public void print(final String value) {
        System.out.println("ServiceB: " + value);
    }
}
