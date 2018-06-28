package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryApplication;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;

/**
 * This sample shows
 *    * how to use an own event
 *    * different ways to register listeners
 *    * how to get notified for an application shutdown
 */
public class EventExampleApp implements BeanRepositoryConfigurator {

    public static void main(String[] args) {

        BeanRepositoryApplication.run(args, new EventExampleApp());
    }

    @Override
    public void configure(BeanRepository.BeanRepositoryBuilder builder) {

        builder
                .singleton(OnStartupListener.class, OnStartupListener::new)
                .singleton(ShutdownListener.class, ShutdownListener::new)

                // Concrete listener
                .singleton(PizzaTimeListener.class, PizzaTimeListener::new)

                // This bean registers an anonymous listener within its onPostConstruct method!
                .singleton(HungryPerson.class, HungryPerson::new, ApplicationEventBus.class);
    }
}
