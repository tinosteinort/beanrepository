package de.tse.beanrepository.example.services;

public class ServiceWithParameter {

    private final String id;

    public ServiceWithParameter(final String id) {
        this.id = id;
    }

    public void print(final String value) {
        System.out.println(id + ": " + value);
    }
}
