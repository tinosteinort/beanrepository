package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;
import com.github.tinosteinort.beanrepository.application.event.AbstractApplicationEventListener;
import com.github.tinosteinort.beanrepository.application.event.ApplicationEventBus;

public class HungryPerson implements PostConstructible {

    private final ApplicationEventBus eventBus;

    public HungryPerson(final ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onPostConstruct(final BeanRepository repository) {

        // there is just an anonymous class registered as listener
        eventBus.register(new AbstractApplicationEventListener<PizzaTimeEvent>(PizzaTimeEvent.class) {

            @Override
            public void onEvent(final PizzaTimeEvent event) {
                System.out.println("Hmmmm, I will eat some " + event.getPizza() + "!");
            }
        });
    }
}
