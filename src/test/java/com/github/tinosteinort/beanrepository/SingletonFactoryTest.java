package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class SingletonFactoryTest {

    @Test public void createInstanceHasToExecutedOnlyOnce() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Bean.class, BeanFactory::new)
                .build();

        // createInstance() must not executed while dryRun

        Assert.assertEquals(0, BeanFactory.createCounter);
        Bean bean = repo.getBean(Bean.class);
        Assert.assertEquals(1, BeanFactory.createCounter);
    }
}

class Bean {

}

class BeanFactory implements Factory<Bean> {

    public static int createCounter = 0;

    @Override public Bean createInstance() {
        createCounter++;
        return new Bean();
    }
}