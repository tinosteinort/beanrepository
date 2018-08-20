package com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample;

public class DriverImpl implements Driver {

    private final Car car;

    public DriverImpl(final Car car) {
        this.car = car;
    }

    public void startEngine() {
        System.out.println("driver starts engine...");
        car.startEngine();
    }

    @Override
    public void inform(final String message) {
        System.out.println(message);
    }
}
