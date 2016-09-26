package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class ServiceB {

    private final ServiceA serviceA;

    public ServiceB(final BeanAccessor beans) {
        this.serviceA = beans.getBean(ServiceA.class);
    }

    public void print(final String value) {
        System.out.println("ServiceB: " + value);
    }
}
