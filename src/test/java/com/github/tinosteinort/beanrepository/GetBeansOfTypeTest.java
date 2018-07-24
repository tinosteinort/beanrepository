package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Important note: the method 'getBeansOfType' cannot be called in a constructor.
 *  To prevent this, the 'BeanAccessor' as no 'getBeansOfType' method. If a bean
 *  wants to collect other beans of a specific type, use the 'getBeansOfType'
 *  method in the 'onPostConstruct' method
 */
public class GetBeansOfTypeTest {

    @Test public void returnFoundValues() {
        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Dog.class, Dog::new)
                .singleton(Cat.class, Cat::new)
                .build();

        final Set<Animal> animals = repo.getBeansOfType(Animal.class);
        assertEquals(2, animals.size());
    }

    @Test public void returnsEmptyListIfNoBeanWasFound() {
        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Human.class, Human::new)
                .build();

        final Set<Animal> animals = repo.getBeansOfType(Animal.class);
        assertNotNull(animals);
        assertEquals(0, animals.size());
    }

    /**
     * Test a special case of Issue#5
     * <br/><b>
     *  If in a onPostConstruct method the getBeansOfType method is called, a NPE was thrown
     * </b><br/>
     * https://github.com/tinosteinort/beanrepository/issues/5
     */
    @Test public void beansOfTypeInPostConstructThrowsNPEwithSingletonFactory() {
        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Dog.class, DogFactory::new)
                .singleton(AnimalCollector.class, AnimalCollector::new)
                .build();
    }

    /**
     * Same test as above, only for prototype bean
     */
    @Test public void beansOfTypeInPostConstructThrowsNPEwithPrototypeFactory() {
        new BeanRepository.BeanRepositoryBuilder()
                .prototypeFactory(Dog.class, DogFactory::new)
                .singleton(AnimalCollector.class, AnimalCollector::new)
                .build();
    }
}

interface Animal {

}

class Dog implements Animal {

}

class Cat implements Animal {

}

class Human {

}

class DogFactory implements Factory<Dog> {

    @Override public Dog createInstance() {
        return new Dog();
    }

    @Override public Class<Dog> getBeanType() {
        return Dog.class;
    }
}

class AnimalCollector implements PostConstructible {

    private final List<Animal> animals = new ArrayList<>();

    @Override public void onPostConstruct(BeanRepository repository) {
        // this call on the repository raises the NPE (before the fix)
        final Set<Animal> animalsInRepository = repository.getBeansOfType(Animal.class);

        animals.addAll(animalsInRepository);
    }
}