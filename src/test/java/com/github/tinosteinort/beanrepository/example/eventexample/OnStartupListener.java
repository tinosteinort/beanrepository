package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.BeanAccessor;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEvent;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEventListener;

public class OnStartupListener extends BeansInitialisedEventListener {

    private final BeanAccessor beans;

    /**
     * For classes that extends BeansInitialisedEventListener it is not possible to depend on
     *  ApplicationEventBus because that is a cyclic dependency reference. To get on the
     *  ApplicationEventBus instance, you have to use the BeanAccessor.
     *
     * For other classes, which are not implementing BeansInitialisedEventListener, it is possible
     *  to get a direct dependency of type ApplicationEventBus, see class "HungryPerson".
     */
    public OnStartupListener(final BeanAccessor beans) {
        this.beans = beans;
    }

    @Override
    public void onEvent(final BeansInitialisedEvent event) {

        System.out.println("Application started");
        System.out.println("-------------------");


        final Pizza salami = new Pizza("salami", "ham", "mushrooms");
        final Pizza hawaii = new Pizza("ham", "pineapple");

        // So, if you want to throw an event, e.g. from within an event, as in this case, you have
        //  to get the event bus on execution time:
        final ApplicationEventBus eventBus = beans.getBean(ApplicationEventBus.class);

        eventBus.fireEvent(new PizzaTime(salami));
        System.out.println("-------------------");

        eventBus.fireEvent(new PizzaTime(hawaii));
        System.out.println("-------------------");
    }
}
