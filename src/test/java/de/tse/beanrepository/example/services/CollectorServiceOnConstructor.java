package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;

import java.util.HashSet;
import java.util.Set;

public class CollectorServiceOnConstructor {

    private final Set<MyInterface> implementations = new HashSet<>();

    public CollectorServiceOnConstructor(final BeanRepository repository) {
        implementations.addAll(repository.getBeansOfType(MyInterface.class));
    }

    public Set<MyInterface> getImplementations() {
        return implementations;
    }
}
