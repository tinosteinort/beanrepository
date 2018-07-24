package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.example.services.ServiceA;
import com.github.tinosteinort.beanrepository.example.services.ServiceB;
import org.junit.Test;

public class DetectCyclicReferenceOnBuild {

    /**
     * ServiceA depends on ServiceB and ServiceB depends on ServiceA.
     *  This cyclic Reference should lead to an Error on start up, not
     *  until a Bean is requested by repo.getBean(...)
     */
    @Test(expected = StackOverflowError.class)
    public void cyclicReferenceCheckOnBeanRepositoryInitialisation() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(ServiceA.class, ServiceA::new)
                .singleton(ServiceB.class, ServiceB::new)
                .build();
    }
}
