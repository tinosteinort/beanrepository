package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExecuteOnPostConstructOfFactoryAndOfBean {

    @Test public void executeOnPostConstructOfSingletonFactoryAndBean() {

        Executed factoryExecuted = new Executed();
        Executed beanExecuted = new Executed();

        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Stone.class, () -> new StoneFactory(factoryExecuted, beanExecuted))
                .build();

        assertTrue(beanExecuted.executed);
        assertTrue(factoryExecuted.executed);
    }

    @Test public void executeOnPostConstructOfPrototypeFactoryAndBean() {

        Executed factoryExecuted = new Executed();
        Executed beanExecuted = new Executed();

        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototypeFactory(Stone.class, () -> new StoneFactory(factoryExecuted, beanExecuted))
                .build();

        assertFalse(beanExecuted.executed);
        assertFalse(factoryExecuted.executed);

        repo.getBean(Stone.class);

        assertTrue(beanExecuted.executed);
        assertTrue(factoryExecuted.executed);
    }
}

class Stone implements PostConstructible {

    private final Executed executed;

    Stone(final Executed executed) {
        this.executed = executed;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        executed.executed = true;
    }
}

class StoneFactory implements Factory<Stone>, PostConstructible {

    private final Executed factoryExecuted;
    private final Executed beanExecuted;

    StoneFactory(Executed factoryExecuted, Executed beanExecuted) {
        this.factoryExecuted = factoryExecuted;
        this.beanExecuted = beanExecuted;
    }

    @Override public Stone createInstance() {
        return new Stone(beanExecuted);
    }

    @Override public Class<Stone> getBeanType() {
        return Stone.class;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        factoryExecuted.executed = true;
    }
}