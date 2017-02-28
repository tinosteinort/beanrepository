package com.github.tinosteinort.beanrepository;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Alternative Possibility: BeanRepository implements BeanAccessor. But then, it is possible to
 *  cast BeanAccessor to BeanRepository in the Constructor of a Bean. This Class should
 *  prevent this Casting.
 */
class BeanRepositoryShelter implements BeanAccessor {

    private final BeanRepository repository;

    public BeanRepositoryShelter(final BeanRepository repository) {
        this.repository = repository;
    }
    @Override public <T> T getBean(final Class<T> cls) {
        return repository.getBean(cls);
    }

    @Override public <T> T getPrototypeBean(Supplier<T> creator) {
        return repository.getPrototypeBean(creator);
    }

    @Override public <T> T getPrototypeBean(Function<BeanAccessor, T> creator) {
        return repository.getPrototypeBean(creator);
    }

    @Override public <T, P1> T getPrototypeBean(final ConstructorWith1Parameter<T, P1> creator, final P1 param1) {
        return repository.getPrototypeBean(creator, param1);
    }

    @Override public <T, P1, P2> T getPrototypeBean(final ConstructorWith2Parameters<T, P1, P2> creator,
            final P1 param1, final P2 param2) {
        return repository.getPrototypeBean(creator, param1, param2);
    }

    @Override public <T, P1, P2, P3> T getPrototypeBean(final ConstructorWith3Parameters<T, P1, P2, P3> creator,
            final P1 param1, final P2 param2, final P3 param3) {
        return repository.getPrototypeBean(creator, param1, param2, param3);
    }

    @Override public <T, P1, P2, P3, P4> T getPrototypeBean(
            final ConstructorWith4Parameters<T, P1, P2, P3, P4> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4) {
        return repository.getPrototypeBean(creator, param1, param2, param3, param4);
    }

    @Override public <T, P1, P2, P3, P4, P5> T getPrototypeBean(
            final ConstructorWith5Parameters<T, P1, P2, P3, P4, P5> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4, final P5 param5) {
        return repository.getPrototypeBean(creator, param1, param2, param3, param4, param5);
    }

    public <T, P1> T getPrototypeBean(final ConstructorWithBeansAnd1Parameter<T, P1> creator,
            final P1 param1) {
        return repository.getPrototypeBean(creator, param1);
    }

    public <T, P1, P2> T getPrototypeBean(
            final ConstructorWithBeansAnd2Parameters<T, P1, P2> creator, final P1 param1, final P2 param2) {
        return repository.getPrototypeBean(creator, param1, param2);
    }

    public <T, P1, P2, P3> T getPrototypeBean(
            final ConstructorWithBeansAnd3Parameters<T, P1, P2, P3> creator, final P1 param1, final P2 param2,
            final P3 param3) {
        return repository.getPrototypeBean(creator, param1, param2, param3);
    }

    public <T, P1, P2, P3, P4> T getPrototypeBean(
            final ConstructorWithBeansAnd4Parameters<T, P1, P2, P3, P4> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4) {
        return repository.getPrototypeBean(creator, param1, param2, param3, param4);
    }

    public <T, P1, P2, P3, P4, P5> T getPrototypeBean(
            final ConstructorWithBeansAnd5Parameters<T, P1, P2, P3, P4, P5> creator, final P1 param1, final P2 param2,
            final P3 param3, final P4 param4, final P5 param5) {
        return repository.getPrototypeBean(creator, param1, param2, param3, param4, param5);
    }

    @Override public <T> Provider<T> getProvider(final Class<T> cls) {
        return repository.getProvider(cls);
    }
}
