package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PrototypeFactoryTest {

    @Test public void createPrototypeInstanceHasToExecutedOnlyOnce() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototypeFactory(PrototypeBean.class, PrototypeBeanFactory::new)
                .build();

        // createInstance() must not executed while dryRun

        assertEquals(0, PrototypeBeanFactory.createCounter);
        PrototypeBean bean = repo.getBean(PrototypeBean.class);
        assertEquals(1, PrototypeBeanFactory.createCounter);
    }

    @Test public void returnNullOnDryRun() {

        final PrototypeFactoryProvider factory = new PrototypeFactoryProvider(null, (BeanAccessor beans) -> null);

        BeanRepository dummy = new BeanRepository.BeanRepositoryBuilder().build();
        assertNull(factory.getBean(dummy, DryRunAwareMock.DRY_RUN));
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

    @Override public Class<PrototypeBean> getBeanType() {
        return PrototypeBean.class;
    }
}
