package com.github.tinosteinort.beanrepository;

import org.junit.Test;

/**
 * This test checks if there is an error thrown, when there is a cycle in which factories are involved.
 *  The cycle should be detected in the dryRun, when the BeanRepository is built. Because of this it is
 *  important to enable lazy singletons, and expect the StackOverflowError
 */
public class FactoryReferenceCycleTest {

    @Test(expected = StackOverflowError.class)
    public void referenceCycleSingletonFactory() {
        new BeanRepository.BeanRepositoryBuilder()
                .enableLazySingletonBeans(true)
                .singleton(CarCompany.class, CarCompany::new, Car.class)
                .singletonFactory(Car.class, CarFactory::new, CarCompany.class)
        .build();
    }

    @Test(expected = StackOverflowError.class)
    public void referenceCyclePrototypeFactory() {
        new BeanRepository.BeanRepositoryBuilder()
                .enableLazySingletonBeans(true)
                .singleton(CarCompany.class, CarCompany::new, Car.class)
                .prototypeFactory(Car.class, CarFactory::new, CarCompany.class)
        .build();
    }
}

class Car {

}

class CarCompany {

    private final Car car;

    public CarCompany(Car car) {
        this.car = car;
    }
}

class CarFactory implements Factory<Car> {

    private final CarCompany company;

    CarFactory(CarCompany company) {
        this.company = company;
    }

    @Override public Car createInstance() {
        return new Car();
    }

    @Override public Class<Car> getBeanType() {
        return Car.class;
    }
}