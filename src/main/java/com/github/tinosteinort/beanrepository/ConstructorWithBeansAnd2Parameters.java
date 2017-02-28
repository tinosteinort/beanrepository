package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWithBeansAnd2Parameters<B, PARAM_1, PARAM_2> {

    B create(BeanAccessor beans, PARAM_1 param1, PARAM_2 param2);
}
