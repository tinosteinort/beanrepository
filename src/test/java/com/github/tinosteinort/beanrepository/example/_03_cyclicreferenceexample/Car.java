package com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample;

public class Car {

    private final Driver driver;

    public Car(final Driver driver) {
        this.driver = driver;
    }

    public void startEngine() {
        System.out.println("car starts engine...");
        driver.inform("engine started!");
    }
}
