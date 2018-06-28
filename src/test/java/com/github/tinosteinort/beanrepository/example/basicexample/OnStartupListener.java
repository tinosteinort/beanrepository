package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationEvent;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEvent;
import com.github.tinosteinort.beanrepository.application.event.BeansInitialisedEventListener;

/**
 * Every bean, that implements BeansInitialisedEventListener, will be triggered
 *  right after the BeanRepository is built. This should be used to execute code
 *  on startup.
 */
public class OnStartupListener extends BeansInitialisedEventListener {

    private final ArgumentPrinter argumentPrinter;

    public OnStartupListener(final ArgumentPrinter argumentPrinter) {
        this.argumentPrinter = argumentPrinter;
    }

    @Override
    public void onEvent(final BeansInitialisedEvent event) {

        System.out.println("Application started");

        argumentPrinter.printCommandLineArgs();
    }
}
