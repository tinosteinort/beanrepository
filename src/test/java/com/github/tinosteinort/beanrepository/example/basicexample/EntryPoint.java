package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationEvent;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEventListener;

public class EntryPoint extends BeansInitialisedEventListener {

    private final ArgumentPrinter argumentPrinter;

    public EntryPoint(final ArgumentPrinter argumentPrinter) {
        this.argumentPrinter = argumentPrinter;
    }

    @Override
    public void onEvent(final ApplicationEvent event) {

        System.out.println("Application started");

        argumentPrinter.printJavaArgs();
    }
}
