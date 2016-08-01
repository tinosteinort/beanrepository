package de.tse.beanrepository;

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
}
