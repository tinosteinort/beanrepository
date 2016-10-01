package com.github.tinosteinort.beanrepository.example.services;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.PostConstructible;

public class ServiceWithPostConstructCounter implements PostConstructible {

    private static int postConstructCount = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCount++;
    }

    public static int getPostConstructCount() {
        return postConstructCount;
    }
}
