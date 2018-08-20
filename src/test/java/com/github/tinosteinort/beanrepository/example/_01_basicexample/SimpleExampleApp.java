package com.github.tinosteinort.beanrepository.example._01_basicexample;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryApplication;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.env.ArgsProvider;

/**
 * This is a simple sample application which demonstrates
 *    * how to start an application with the BeanRepository
 *    * how to access the command line arguments
 *    * how to listen to application start up
 */
public class SimpleExampleApp implements BeanRepositoryConfigurator {

    public static void main(String[] args) {

        // set args manually without configuring IDE run configs
        // -> not needed for real applications
        args = new String[] {"argument 1", "argument 2", "value"};

        // start the application with dependency injection support
        //  1. parameter: commandline args[]
        //  2. parameter: Some class that implements BeanRepositoryConfigurator
        BeanRepositoryApplication.run(args, new SimpleExampleApp());
    }

    @Override
    public void configure(final BeanRepository.BeanRepositoryBuilder builder) {

        // configure all required beans with dependencies in this method
        builder
                .singleton(StartupListener.class, StartupListener::new, ArgumentPrinter.class)
                // the ArgsProvider is a bean which is provided by the BeanRepository
                .singleton(ArgumentPrinter.class, ArgumentPrinter::new, ArgsProvider.class);
    }
}
