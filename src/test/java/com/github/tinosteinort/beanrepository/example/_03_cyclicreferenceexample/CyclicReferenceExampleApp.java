package com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryApplication;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;

/**
 * If someone really needs a bean relationship with a cycle, this example
 *  shows a possibility.
 *
 * Usecase:
 *  A car has a driver
 *  A driver has a car
 */
public class CyclicReferenceExampleApp implements BeanRepositoryConfigurator {

    public static void main(String[] args) {

        BeanRepositoryApplication.run(args, new CyclicReferenceExampleApp());
    }

    @Override
    public void configure(final BeanRepository.BeanRepositoryBuilder builder) {
        builder.singleton(StartupListener.class, StartupListener::new, Driver.class)

                // configuration of the cyclic relationship
                .singleton(Car.class, Car::new, Driver.class)
                .singleton(DriverImpl.class, DriverImpl::new, Car.class)
                .singleton(Driver.class, DriverDelegate::new);

    }
}
