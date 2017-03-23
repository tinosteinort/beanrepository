package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Special Test Case for Issue#3
 */
public class DryRunTest {

    @Test public void dryRunWithReferenceCollector() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(CollectorA.class, CollectorA::new)
                .singleton(CollectorB.class, CollectorB::new)
                .singleton(ServiceBeta.class, ServiceBeta::new)
                .singleton(ServiceAlpha.class, ServiceAlpha::new, ServiceBeta.class)
                .build();

        repo.getBean(ServiceAlpha.class);
    }
}

class ServiceAlpha {

    private final ServiceBeta serviceBeta;

    ServiceAlpha(final ServiceBeta serviceBeta) {
        this.serviceBeta = serviceBeta;
    }
}

class ServiceBeta implements PostConstructible {

    private final List<Collector> collectors = new ArrayList<>();

    @Override public void onPostConstruct(final BeanRepository repository) {
        collectors.addAll(repository.getBeansOfType(Collector.class));
    }
}

interface Collector {

}

class CollectorA implements Collector {

}

class CollectorB implements Collector {

}