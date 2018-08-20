package com.github.tinosteinort.beanrepository.example._01_basicexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedListener;

/**
 * Every bean, that implements ApplicationStartedListener, will be triggered
 *  right after the BeanRepository is built. This should be used to execute code
 *  on startup.
 */
public class StartupListener extends ApplicationStartedListener {

    private final ArgumentPrinter argumentPrinter;

    public StartupListener(final ArgumentPrinter argumentPrinter) {
        this.argumentPrinter = argumentPrinter;
    }

    @Override
    public void onEvent(final ApplicationStartedEvent event) {

        System.out.println("Application started");

        argumentPrinter.printCommandLineArgs();
    }
}
