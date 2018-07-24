package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * This test checks if a cast from BeanAccessor to BeanRepository is forbidden.
 *  You cannot collect beans of a specific type in the constructor, because not
 *  all beans are collected yet. It depends on the order of registration of the
 *  beans. So, to get beans of a specific type, use the 'onPostConstruct' method.
 *  In this method the order of the registration of the beans does not matter.
 */
public class CastBeanAccessorTest {

    @Test(expected = ClassCastException.class)
    public void denyCastToBeanRepository() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(NumberCollector.class, NumberCollector::new)
                .singleton(NumberOne.class, NumberOne::new)
                .singleton(NumberTwo.class, NumberTwo::new)
                .build();
    }
}

class NumberCollector {

    private final Set<Number> beans = new HashSet<>();

    public NumberCollector(final BeanAccessor accessor) {
        // Try to cast to BeanRepository to enable method 'getBeansOfType'. This is not allowed.
        final BeanRepository repository = (BeanRepository) accessor;
        beans.addAll(repository.getBeansOfType(Number.class));

        // Use 'getBeansOfType' in 'onPostConstruct' instead, to collect beans of a specific type
    }
}

interface Number {

}

class NumberOne implements Number {

}
class NumberTwo implements Number {

}