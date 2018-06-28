package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.application.env.ArgsProvider;

/**
 * This is a sample service which just print the command line arguments.
 */
public class ArgumentPrinter {

    private final ArgsProvider argsProvider;

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
