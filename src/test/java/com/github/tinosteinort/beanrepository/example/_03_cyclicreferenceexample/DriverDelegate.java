package com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample;

import com.github.tinosteinort.beanrepository.BeanAccessor;

public class DriverDelegate implements Driver {

    private final BeanAccessor beans;

    public DriverDelegate(final BeanAccessor beans) {
        this.beans = beans;
    }

    @Override
    public void startEngine() {
        beans.getBean(DriverImpl.class).startEngine();
    }

    @Override
    public void inform(final String message) {
        beans.getBean(DriverImpl.class).inform(message);
    }
}
