package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetBeanTest {

    @Test public void getSingletonBean() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterface.class, MyImplementation::new)
                .build();

        assertNotNull(repository.getBean(MyInterface.class));
    }

    @Test public void getSingletonBeanByAlias() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyImplementation.class, MyImplementation::new)
                .alias(MyOtherInterface.class, MyImplementation.class)
                .build();

        final MyImplementation service = repository.getBean(MyImplementation.class);
        final MyOtherInterface alias = repository.getBean(MyOtherInterface.class);

        assertNotNull(service);
        assertNotNull(alias);

        assertEquals(service, alias);
        assertTrue(System.identityHashCode(service) == System.identityHashCode(alias));
    }

    @Test public void getSingletonBeanWithDependencyByAlias() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singleton(ServiceWithPrintService.class, ServiceWithPrintService::new, PrintService.class)
                .alias(MyInterface.class, ServiceWithPrintService.class)
                .build();

        assertNotNull(repository.getBean(MyInterface.class));
    }

    @Test public void getRegisteredPrototypeBeanByAlias() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MyImplementation.class, MyImplementation::new)
                .alias(MyOtherInterface.class, MyImplementation.class)
                .build();

        final MyImplementation service = repository.getBean(MyImplementation.class);
        final MyOtherInterface alias = repository.getBean(MyOtherInterface.class);

        assertNotNull(service);
        assertNotNull(alias);

        assertNotEquals(service, alias);
        assertFalse(service == alias);
    }

    @Test public void getRegisteredPrototypeOneParamBeanByAlias() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .prototype(ServiceWithPrintService.class, ServiceWithPrintService::new, PrintService.class)
                .alias(MyInterface.class, ServiceWithPrintService.class)
                .build();

        final ServiceWithPrintService service = repository.getBean(ServiceWithPrintService.class);
        final MyInterface alias = repository.getBean(MyInterface.class);

        assertNotNull(service);
        assertNotNull(alias);

        assertNotEquals(service, alias);
        assertFalse(service == alias);
    }

    @Test public void getUnregisteredPrototypeBean() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .build();

        final PrintService printService = repository.getBean(PrintService.class);
        final ServiceWithPrintService bean = repository.getPrototypeBean(ServiceWithPrintService::new, printService);
        assertNotNull(bean);
    }
}

interface MyInterface {

}

interface MyOtherInterface {

}

class MyImplementation implements MyInterface, MyOtherInterface {

}

class ServiceWithPrintService implements MyInterface {

    private final PrintService printService;

    ServiceWithPrintService(final PrintService printService) {
        this.printService = printService;
    }
}

class PrintService {

}
