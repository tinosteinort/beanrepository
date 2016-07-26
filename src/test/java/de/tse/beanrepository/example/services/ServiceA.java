package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;

public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(final BeanRepository repo) {
        this.serviceB = repo.get(ServiceB.class);
    }

    public void print(final String value) {
        System.out.println("ServiceA: " + value);
    }
}
