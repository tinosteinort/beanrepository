package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Test a special case of Issue#5
 * <br/><b>
 *  If in a onPostConstruct method the getBeansOfType method is called, a NPE was thrown
 * </b><br/>
 * https://github.com/tinosteinort/beanrepository/issues/5
 */
public class GetBeansOfTypeTest {

    @Test public void beansOfTypeInPostConstructThrowsNPEwithSingletonFactory() {
        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(Dog.class, DogFactory::new)
                .singleton(AnimalCollector.class, AnimalCollector::new)
                .build();
    }

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