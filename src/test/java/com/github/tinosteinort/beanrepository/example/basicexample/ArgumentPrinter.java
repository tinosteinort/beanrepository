package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.application.env.ArgsProvider;

/**
 * This is a sample service which just print the command line arguments.
 */
public class ArgumentPrinter {

    private final ArgsProvider argsProvider;

    /**
     * The ArgsProvider is responsible for getting the command line arguments,
     *  so we need it as a dependency. The ArgsProvider bean is provided by
     *  the BeanRepository.
     */
    public ArgumentPrinter(final ArgsProvider argsProvider) {
        this.argsProvider = argsProvider;
    }

    public void printCommandLineArgs() {

        System.out.println("Commandline args[]:");

        for (String arg : argsProvider.get()) {
            System.out.println(" * '" + arg + "'");
        }
    }
}
