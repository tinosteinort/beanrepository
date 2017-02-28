package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWithBeansAnd1Parameter<B, PARAM_1> {

    B create(BeanAccessor beans, PARAM_1 param1);
}
