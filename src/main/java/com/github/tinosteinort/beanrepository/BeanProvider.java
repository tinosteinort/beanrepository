package com.github.tinosteinort.beanrepository;

interface BeanProvider {

    <T> T getBean(BeanRepository repository, boolean dryRun);

    String getRepositoryId();
}
