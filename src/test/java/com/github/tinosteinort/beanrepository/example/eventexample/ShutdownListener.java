package com.github.tinosteinort.beanrepository.example.eventexample;

import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEvent;
import com.github.tinosteinort.beanrepository.application.event.ApplicationShutdownEventListener;

public class ShutdownListener extends ApplicationShutdownEventListener {

    @Override
    public void onEvent(final ApplicationShutdownEvent event) {
        System.out.println("No more Pizza - application shutdown!");
    }
}
