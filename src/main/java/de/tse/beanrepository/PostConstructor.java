package de.tse.beanrepository;

class PostConstructor {

    private final BeanRepository repository;

    public PostConstructor(final BeanRepository repository) {
        this.repository = repository;
    }

    public void postConstruct(final Object bean, final boolean dryRun) {
        if (dryRun) {
            return;
        }

        if (bean instanceof PostConstructible) {
            ((PostConstructible) bean).onPostConstruct(repository);
        }
    }
}
