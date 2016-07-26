package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;
import de.tse.beanrepository.PostConstructible;

public class ServiceWithPostConstruct implements PostConstructible {

    @Override public void onPostConstruct(final BeanRepository repository) {
        System.out.println("onPostConstruct() was called");
    }
}
