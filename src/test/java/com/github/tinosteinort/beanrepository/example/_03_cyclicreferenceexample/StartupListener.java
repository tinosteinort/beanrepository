package com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationStartedListener;

public class StartupListener extends ApplicationStartedListener {

    private final Driver driver;

    public StartupListener(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void onEvent(final ApplicationStartedEvent event) {

        driver.startEngine();
    }
}
