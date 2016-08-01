package de.tse.beanrepository;

public interface BeanAccessor {

    <T> T getBean(Class<T> cls);
}
