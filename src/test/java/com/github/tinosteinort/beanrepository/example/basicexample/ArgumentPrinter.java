package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.application.env.ArgsProvider;

public class ArgumentPrinter {

    private final ArgsProvider argsProvider;

    public ArgumentPrinter(final ArgsProvider argsProvider) {
        this.argsProvider = argsProvider;
    }

    public void printJavaArgs() {

        System.out.println("Java args[]:");

        for (String arg : argsProvider.get()) {
            System.out.println(" * '" + arg + "'");
        }
    }
}
