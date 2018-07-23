package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultipleConfiguratorTest {

    @Test public void multipleConfigurators() {

        HitCounter counter = new HitCounter();

        BeanRepositoryConfigurator configurator1 = builder -> builder.singleton(Apple.class, Apple::new);
        BeanRepositoryConfigurator configurator2 = builder -> builder.singleton(Pear.class, Pear::new);
        BeanRepositoryConfigurator configurator3 = builder -> builder.singleton(Basket.class, () -> new Basket(counter));

        BeanRepositoryApplication.run(new String[0], configurator1, configurator2, configurator3);

        assertEquals(1, counter.hits());
    }

    @Test(expected = NullPointerException.class)
    public void argsRequired() {

        BeanRepositoryApplication.run(null, (BeanRepositoryConfigurator) builder -> {

        });
    }

    @Test(expected = NullPointerException.class)
    public void configuratorsRequiredNotNull() {

        BeanRepositoryApplication.run(new String[0], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configuratorsRequiredNotEmpty() {

        BeanRepositoryApplication.run(new String[0], new BeanRepositoryConfigurator[0]);
    }
}

class Apple {

}

class Pear {

}

class Basket implements PostConstructible {

    private final HitCounter counter;

    Basket(HitCounter counter) {
        this.counter = counter;
    }

    @Override
    public void onPostConstruct(BeanRepository repository) {
        counter.hit();
        assertNotNull(repository.getBean(Apple.class));
        assertNotNull(repository.getBean(Pear.class));
    }
}