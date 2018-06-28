package com.github.tinosteinort.beanrepository.example.basicexample;

import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.BeanRepositoryApplication;
import com.github.tinosteinort.beanrepository.BeanRepositoryConfigurator;
import com.github.tinosteinort.beanrepository.application.env.ArgsProvider;

public class SimpleExampleApp implements BeanRepositoryConfigurator {

    public static void main(String[] args) {

        // set args manually without configuring IDE run configs
        args = new String[] {"argument 1", "argument 2", "value"};

        // start the application with dependency injection support
        //  1. parameter: Java system String[] args
        //  2. parameter: Some class that implements BeanRepositoryConfigurator
        BeanRepositoryApplication.run(args, new SimpleExampleApp());
    }

    @Override
    public void configure(final BeanRepository.BeanRepositoryBuilder builder) {

        // Configure all required beans with dependencies
        builder
                .singleton(EntryPoint.class, EntryPoint::new, ArgumentPrinter.class)
                .singleton(ArgumentPrinter.class, ArgumentPrinter::new, ArgsProvider.class);
    }
}
