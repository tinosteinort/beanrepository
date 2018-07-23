package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SingletonFactoryTest {

    @Test public void createInstanceHasToExecutedOnlyOnce() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Bean.class, BeanFactory::new)
                .build();

        assertEquals(1, BeanFactory.createCounter);
        repo.getBean(Bean.class);
        assertEquals(1, BeanFactory.createCounter);
    }

    @Test public void returnNullOnDryRun() {

        final SingletonFactoryProvider factory = new SingletonFactoryProvider(null, (BeanAccessor beans) -> null);

        BeanRepository dummy = new BeanRepository.BeanRepositoryBuilder().build();
        assertNull(factory.getBean(dummy, DryRunAwareMock.DRY_RUN));
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

    @Override public Class<Bean> getBeanType() {
        return Bean.class;
    }
}