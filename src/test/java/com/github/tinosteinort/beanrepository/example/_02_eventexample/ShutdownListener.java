package com.github.tinosteinort.beanrepository.example._02_eventexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownListener;

public class ShutdownListener extends ApplicationShutdownListener {

    @Override
    public void onEvent(final ApplicationShutdownEvent event) {
        System.out.println("No more Pizza - application shutdown!");
    }
}
