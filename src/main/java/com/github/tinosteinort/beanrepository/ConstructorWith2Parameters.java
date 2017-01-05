package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWith2Parameters<B, DEP_1, DEP_2> {

    B create(DEP_1 dependency1, DEP_2 dependency2);
}
