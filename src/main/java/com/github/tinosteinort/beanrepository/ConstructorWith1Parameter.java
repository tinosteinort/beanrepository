package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWith1Parameter<B, DEP_1> {

    B create(DEP_1 dependency1);
}
