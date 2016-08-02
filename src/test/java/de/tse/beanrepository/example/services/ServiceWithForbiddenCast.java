package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanAccessor;
import de.tse.beanrepository.BeanRepository;

import java.util.HashSet;
import java.util.Set;

public class ServiceWithForbiddenCast {

    private final Set<MyInterface> beans = new HashSet<>();

    public ServiceWithForbiddenCast(final BeanAccessor accessor) {
        // Try to cast to BeanRepository to enable Method 'getBeansOfType'. This must not be allowed
        final BeanRepository repository = (BeanRepository) accessor;
        beans.addAll(repository.getBeansOfType(MyInterface.class));

        // Instead use onPostConsturct to collect Beans of a specific Type
    }
}
