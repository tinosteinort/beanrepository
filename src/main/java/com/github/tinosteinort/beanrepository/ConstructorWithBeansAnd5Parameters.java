package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWithBeansAnd5Parameters<B, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5> {

    B create(BeanAccessor beans, PARAM_1 param1, PARAM_2 param2, PARAM_3 param3, PARAM_4 param4, PARAM_5 param5);
}
