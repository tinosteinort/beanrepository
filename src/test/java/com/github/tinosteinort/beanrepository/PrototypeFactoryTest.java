package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class PrototypeFactoryTest {

    @Test public void createPrototypeInstanceHasToExecutedOnlyOnce() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototypeFactory(PrototypeBean.class, PrototypeBeanFactory::new)
                .build();

        // createInstance() must not executed while dryRun

        Assert.assertEquals(0, PrototypeBeanFactory.createCounter);
        PrototypeBean bean = repo.getBean(PrototypeBean.class);
        Assert.assertEquals(1, PrototypeBeanFactory.createCounter);
    }
}

class PrototypeBean {

}

class PrototypeBeanFactory implements Factory<PrototypeBean> {

    public static int createCounter = 0;

    @Override public PrototypeBean createInstance() {
        createCounter++;
        return new PrototypeBean();
    }
}
