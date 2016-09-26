package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;

public class ServiceWithPostConstruct implements PostConstructible {

    @Override public void onPostConstruct(final BeanRepository repository) {
        System.out.println("onPostConstruct() was called");
    }
}
