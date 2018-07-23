package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExecuteOnPostConstructOfFactoryAndOfBean {

    @Test public void executeOnPostConstructOfSingletonFactoryAndBean() {

        HitCounter counter = new HitCounter();

        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Stone.class, () -> new StoneFactory(counter))
                .build();

        assertTrue(counter.hasHit("onPostConstruct of Stone executed"));
        assertTrue(counter.hasHit("onPostConstruct of StoneFactory executed"));
    }

    @Test public void executeOnPostConstructOfPrototypeFactoryAndBean() {

        HitCounter counter = new HitCounter();

        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototypeFactory(Stone.class, () -> new StoneFactory(counter))
                .build();

        assertEquals(0, counter.hits());

        repo.getBean(Stone.class);

        assertTrue(counter.hasHit("onPostConstruct of Stone executed"));
        assertTrue(counter.hasHit("onPostConstruct of StoneFactory executed"));
    }
}

class Stone implements PostConstructible {

    private final HitCounter counter;

    Stone(final HitCounter counter) {
        this.counter = counter;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        counter.hit("onPostConstruct of Stone executed");
    }
}

class StoneFactory implements Factory<Stone>, PostConstructible {

    private final HitCounter counter;

    StoneFactory(HitCounter counter) {
        this.counter = counter;
    }

    @Override public Stone createInstance() {
        return new Stone(counter);
    }

    @Override public Class<Stone> getBeanType() {
        return Stone.class;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        counter.hit("onPostConstruct of StoneFactory executed");
    }
}