package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class ServiceWithoutPostConstruct {

    private final ServiceWithPostConstructCounter serviceWithPostConstructCounter;

    public ServiceWithoutPostConstruct(final BeanAccessor beans) {
        this.serviceWithPostConstructCounter = beans.getBean(ServiceWithPostConstructCounter.class);
    }
}
