package com.github.tinosteinort.beanrepository;

@FunctionalInterface
public interface ConstructorWith4Parameters<B, DEP_1, DEP_2, DEP_3, DEP_4> {

    B create(DEP_1 dependency1, DEP_2 dependency2, DEP_3 dependency3, DEP_4 dependency4);
}
