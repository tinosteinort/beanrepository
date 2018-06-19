package com.github.tinosteinort.beanrepository;

/**
 * This Class provides a Method to execute Code after a Bean is initialised.
 */
@FunctionalInterface
public interface PostConstructible {

    /**
     * This Method is called when a Bean was successfully created. At the execution Time of this Method, all
     *  Beans are available in the {@link BeanRepository}. This Method is called once, for every constructed
     *  {@code singleton} and {@code prototype} Bean. For Beans of the Scope {@code instance} this Method is
     *  never executed, because a constructed Instance is already passed to the {@link BeanRepository}. If a
     *  {@code Factory} and the bean created by the {@code Factory} implements {@code PostConstructible}, the
     *  Factories {@code onPostConstruct} method is executed first.
     *
     * @param repository    A complete initialised {@link BeanRepository}.
     */
    void onPostConstruct(BeanRepository repository);
}
