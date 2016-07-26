package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;

public class ServiceB {

    private final ServiceA serviceA;

    public ServiceB(final BeanRepository repo) {
        this.serviceA = repo.get(ServiceA.class);
    }

    public void print(final String value) {
        System.out.println("ServiceB: " + value);
    }
}
