package com.github.tinosteinort.beanrepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnableLazySingletonBeansTest {

    @Before public void setup() {

        WorkerService.postConstructCounter = 0;
    }

    @Test public void noLazyBeansAsDefault() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(WorkerService.class, WorkerService::new)
                .build();

        assertEquals(1, WorkerService.postConstructCounter);
    }

    @Test public void noLazyBeansSetExplicit() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(WorkerService.class, WorkerService::new)
                .enableLazySingletonBeans(false)
                .build();

        assertEquals(1, WorkerService.postConstructCounter);
    }

    @Test public void lazySingletonBeanInitialisation() {

        BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(WorkerService.class, WorkerService::new)
                .enableLazySingletonBeans(true)
                .build();

        assertEquals(0, WorkerService.postConstructCounter);
        repo.getBean(WorkerService.class);
        assertEquals(1, WorkerService.postConstructCounter);
    }
}

class WorkerService implements PostConstructible {

    public static int postConstructCounter;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}