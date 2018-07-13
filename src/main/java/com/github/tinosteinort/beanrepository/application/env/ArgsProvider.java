package com.github.tinosteinort.beanrepository.application.env;

import java.util.function.Supplier;

/**
 * A bean that is registered by the BeanRepository itself. Should be used to
 *  determine the startup arguments of the application
 */
public class ArgsProvider implements Supplier<String[]> {

    private final String[] args;

    public ArgsProvider(final String[] args) {
        this.args = args;
    }

    /**
     * Provides the arguments which was passed to the class with the {@code main} method.
     *
     * @return the arguments
     */
    @Override public String[] get() {
        return args;
    }
}
