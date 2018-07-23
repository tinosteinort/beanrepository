package com.github.tinosteinort.beanrepository;

interface BeanProvider {

    <T> T getBean(BeanRepository repository, DryRunAware dryRun);

    Class<?> resolveBeanType(BeanRepository repository, DryRunAware dryRun);

    String getRepositoryId();
}
