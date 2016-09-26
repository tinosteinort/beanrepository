package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;

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
