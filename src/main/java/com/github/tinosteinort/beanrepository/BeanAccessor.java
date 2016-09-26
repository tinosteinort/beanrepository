package com.github.tinosteinort.beanrepository;

public interface BeanAccessor {

    <T> T getBean(Class<T> cls);
}
