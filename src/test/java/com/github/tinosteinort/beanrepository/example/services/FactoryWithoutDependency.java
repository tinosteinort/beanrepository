package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.Factory;

public class FactoryWithoutDependency implements Factory<ServiceWithParameter> {

    @Override public ServiceWithParameter createInstance() {
        return new ServiceWithParameter("ID-123");
    }
}
