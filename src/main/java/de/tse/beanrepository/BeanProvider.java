package de.tse.beanrepository;

interface BeanProvider {

    <T> T getBean(BeanRepository repository, boolean dryRun);
}
