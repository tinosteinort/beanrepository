package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;
import de.tse.beanrepository.PostConstructible;

import java.util.HashSet;
import java.util.Set;

public class CollectorServiceOnPostConstruct implements PostConstructible {

    private final Set<MyInterface> implementations = new HashSet<>();

    public Set<MyInterface> getImplementations() {
        return implementations;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        implementations.addAll(repository.getBeansOfType(MyInterface.class));
    }
}
