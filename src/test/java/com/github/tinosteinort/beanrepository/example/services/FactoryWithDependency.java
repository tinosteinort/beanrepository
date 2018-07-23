package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanAccessor;
import com.github.tinosteinort.beanrepository.Factory;

public class FactoryWithDependency implements Factory<MailService> {

    private final BeanAccessor beans;

    public FactoryWithDependency(final BeanAccessor beans) {
        this.beans = beans;
    }

    @Override public MailService createInstance() {
        return new MailService(beans);
    }

    @Override public Class<MailService> getBeanType() {
        return MailService.class;
    }
}
