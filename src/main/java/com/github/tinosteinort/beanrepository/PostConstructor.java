package com.github.tinosteinort.beanrepository;

class PostConstructor {

    private final BeanRepository repository;

    public PostConstructor(final BeanRepository repository) {
        this.repository = repository;
    }

    public void postConstruct(final Object bean) {
        if (bean instanceof PostConstructible) {
            ((PostConstructible) bean).onPostConstruct(repository);
        }
    }
}
