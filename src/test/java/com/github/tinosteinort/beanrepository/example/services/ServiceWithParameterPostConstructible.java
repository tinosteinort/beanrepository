package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;

import java.util.HashSet;
import java.util.Set;

public class ServiceWithParameterPostConstructible implements PostConstructible {

    private final String id;
    private final Set<MyInterface> manyBeans = new HashSet<>();

    public ServiceWithParameterPostConstructible(final String id) {
        this.id = id;
    }

    public void doSomething() {
        for (MyInterface bean : manyBeans) {
            bean.doSomething(id);
        }
    }

    public Set<MyInterface> getManyBeans() {
        return manyBeans;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        manyBeans.addAll(repository.getBeansOfType(MyInterface.class));
    }
}
