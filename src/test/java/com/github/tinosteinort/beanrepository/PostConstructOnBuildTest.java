package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for Issue
 * <br/><b>
 *  onPostConstruct of singleton beans is executed if the bean is requested, not when the BeanRepository is build
 * </b><br/>
 * https://github.com/tinosteinort/beanrepository/issues/4
 */
public class PostConstructOnBuildTest {

    @Test public void executePostConstructOnBuild() {

        HitCounter counter = new HitCounter();

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(Pen.class, () -> new Pen(counter))
                .build();

        assertTrue(counter.hasHit("onPostConstruct of pen executed"));
    }

    @Test public void executePostConstructOnBuildForFactoryBean() {

        HitCounter counter = new HitCounter();

        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Card.class, () -> new CardFactory(counter))
                .build();

        assertTrue(counter.hasHit("onPostConstruct of card, created by factory, executed"));
    }
}

class Pen implements PostConstructible {

    private final HitCounter counter;

    Pen(final HitCounter counter) {
        this.counter = counter;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        counter.hit("onPostConstruct of pen executed");
    }
}

class CardFactory implements Factory<Card> {

    private final HitCounter counter;

    CardFactory(final HitCounter counter) {
        this.counter = counter;
    }

    @Override public Card createInstance() {
        return new Card(counter);
    }

    @Override public Class<Card> getBeanType() {
        return Card.class;
    }
}

class Card implements PostConstructible {

    private final HitCounter counter;

    Card(final HitCounter counter) {
        this.counter = counter;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        counter.hit("onPostConstruct of card, created by factory, executed");
    }
}