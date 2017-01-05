package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWith3Parameters<B, DEP_1, DEP_2, DEP_3> {

    B create(DEP_1 dependency1, DEP_2 dependency2, DEP_3 dependency3);
}
