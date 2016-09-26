package com.github.tinosteinort.beanrepository;

public interface PostConstructible {

    void onPostConstruct(BeanRepository repository);
}
