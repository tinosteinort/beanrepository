package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanAccessor;

public class ServiceWithBeanDependenciesAndParameter {

    private final PrintService printService;
    private final String id;

    public ServiceWithBeanDependenciesAndParameter(final BeanAccessor beans, final String id) {
        this.printService = beans.getBean(PrintService.class);
        this.id = id;
    }

    public void print(final String value) {
        printService.print(id + ": " + value);
    }
}
