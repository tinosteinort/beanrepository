package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class InstanceTest {

    @Test
    public void instanceHandling() {

        final String[] values = new String[] { "a", "b", "c" } ;

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .instance(values)
                .build();

        assertSame(values, repo.getBean(String[].class));
    }
}
