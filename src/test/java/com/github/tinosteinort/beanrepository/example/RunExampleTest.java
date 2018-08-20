package com.github.tinosteinort.beanrepository.example;

import com.github.tinosteinort.beanrepository.example._01_basicexample.SimpleExampleApp;
import com.github.tinosteinort.beanrepository.example._03_cyclicreferenceexample.CyclicReferenceExampleApp;
import com.github.tinosteinort.beanrepository.example._02_eventexample.EventExampleApp;
import org.junit.Test;

public class RunExampleTest {

    @Test public void runSimpleExampleApp() {

        SimpleExampleApp.main(new String[0]);
    }

    @Test public void runEventExampleApp() {

        EventExampleApp.main(new String[0]);
    }

    @Test public void runCyclicReferenceExampleApp() {

        CyclicReferenceExampleApp.main(new String[0]);
    }
}
