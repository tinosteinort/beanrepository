package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWithBeansAnd3Parameters<B, PARAM_1, PARAM_2, PARAM_3> {

    B create(BeanAccessor beans, PARAM_1 param1, PARAM_2 param2, PARAM_3 param3);
}
