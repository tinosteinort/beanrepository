package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(final BeanAccessor beans) {
        this.serviceB = beans.getBean(ServiceB.class);
    }

    public void print(final String value) {
        System.out.println("ServiceA: " + value);
    }
}
