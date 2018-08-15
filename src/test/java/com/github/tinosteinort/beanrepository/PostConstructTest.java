package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostConstructTest {

    @Test public void callOnPostConstructForPrototypeWithParameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Football.class, Football::new)
                .singleton(Handball.class, Handball::new)
                .build();

        final GameCollector service =
                repo.getPrototypeBean(GameCollector::new, "John");

        assertEquals(2, service.getGames().size());
    }

    /**
     * onPostConstructible() has to be executed only once
     */
    @Test public void singletonWithPrototypePostConstructibleDependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(HitCounter.class, HitCounter::new)
                .prototype(Pizza.class, Pizza::new, HitCounter.class)
                .singleton(PizzaBaker.class, PizzaBaker::new, Pizza.class)
                .enableLazySingletonBeans(false)
                .build();

        final HitCounter counter = repo.getBean(HitCounter.class);

        // When the repository is built, the PizzaBaker bean is initialised and an
        //  prototype Pizza instance is passed to the PizzaBaker
        assertEquals(1, counter.hits());
        assertTrue(counter.hasHit("Pizza created"));

        // When the PizzaBaker is needed, no new prototype Pizza instance must be
        //  initialised and no onPostConstruct of that prototype must be called
        repo.getBean(PizzaBaker.class);

        assertEquals(1, counter.hits());
        assertTrue(counter.hasHit("Pizza created"));
    }
}

interface Game {

}

class Football implements Game {

}

class Handball implements Game {

}

class GameCollector implements PostConstructible {

    private final String username;
    private final Set<Game> games = new HashSet<>();

    GameCollector(String username) {
        this.username = username;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        games.addAll(repository.getBeansOfType(Game.class));
    }

    public Set<Game> getGames() {
        return games;
    }
}

class Pizza implements PostConstructible {

    private final HitCounter counter;

    Pizza(HitCounter counter) {
        this.counter = counter;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        counter.hit("Pizza created");
    }
}

class PizzaBaker {

    private final Pizza pizza;

    PizzaBaker(final Pizza pizza) {
        this.pizza = pizza;
    }
}