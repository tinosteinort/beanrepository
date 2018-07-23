package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HitCounterTest {

    @Test public void zeroHitsAfterCreation() {
        HitCounter hitCounter = new HitCounter();
        assertEquals(0, hitCounter.hits());
    }

    @Test public void countHits() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit();
        assertEquals(1, hitCounter.hits());

        hitCounter.hit();
        assertEquals(2, hitCounter.hits());
    }

    @Test public void resetHits() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit();
        hitCounter.reset();
        assertEquals(0, hitCounter.hits());
    }

    @Test public void namedHit() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit("Interesting!");
        assertEquals(1, hitCounter.hits());
        assertTrue(hitCounter.hasHit("Interesting!"));
    }

    @Test(expected = NullPointerException.class)
    public void namedHitMustNotBeNull() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit(null);
    }

    @Test public void hitExists() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit("123");
        assertTrue(hitCounter.hasHit("123"));
    }

    @Test public void hitNotExists() {
        HitCounter hitCounter = new HitCounter();

        hitCounter.hit("123");
        assertFalse(hitCounter.hasHit("456"));
    }
}
