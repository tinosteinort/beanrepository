package com.github.tinosteinort.beanrepository.example;

import com.github.tinosteinort.beanrepository.example.basicexample.SimpleExampleApp;
import com.github.tinosteinort.beanrepository.example.eventexample.EventExampleApp;
import org.junit.Test;

public class RunExampleTest {

    @Test public void runSimpleExampleApp() {

        SimpleExampleApp.main(new String[0]);
    }

    @Test public void runEventExampleApp() {

        EventExampleApp.main(new String[0]);
    }
}
