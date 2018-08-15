package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.example.services.MailService;
import com.github.tinosteinort.beanrepository.example.services.PrintService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class ProviderTest {

    @Before public void setup() {
        BeanWithPostConstructSingleton.postConstructCounter = 0;
    }
    @Test public void postConstructNotCalledBeforeGet_singleton() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(BeanWithPostConstructSingleton.class, BeanWithPostConstructSingleton::new)
                .enableLazySingletonBeans(true)
                .build();

        final Provider<BeanWithPostConstructSingleton> provider = repo.getProvider(BeanWithPostConstructSingleton.class);
        Assert.assertEquals(0, BeanWithPostConstructSingleton.postConstructCounter);

        provider.get();
        Assert.assertEquals(1, BeanWithPostConstructSingleton.postConstructCounter);
    }

    @Test public void postConstructNotCalledBeforeGet_prototype() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(BeanWithPostConstructPrototype.class, BeanWithPostConstructPrototype::new)
                .enableLazySingletonBeans(true)
                .build();

        repo.getBean(BeanWithPostConstructPrototype.class);
        Assert.assertEquals(1, BeanWithPostConstructPrototype.postConstructCounter);

        final Provider<BeanWithPostConstructPrototype> provider = repo.getProvider(BeanWithPostConstructPrototype.class);
        Assert.assertEquals(1, BeanWithPostConstructPrototype.postConstructCounter);

        provider.get();
        Assert.assertEquals(2, BeanWithPostConstructPrototype.postConstructCounter);
    }

    @Test public void getProviderForSingletons() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .singleton(PrintService.class, PrintService::new)
                .singletonFactory(SomeService.class, SomeServiceFactory::new)
                .prototype(BeanWithPostConstructPrototype.class, BeanWithPostConstructPrototype::new)
                .instance("123")
                .build();

        Assert.assertEquals(3, repo.getProvidersForSingletons().size());
    }

    @Test public void getProviderForPrototypes() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MailService.class, MailService::new)
                .prototype(PrintService.class, PrintService::new)
                .prototypeFactory(SomeService.class, SomeServiceFactory::new)
                .singleton(BeanWithPostConstructSingleton.class, BeanWithPostConstructSingleton::new)
                .instance("123")
                .build();

        Assert.assertEquals(3, repo.getProvidersForPrototypes().size());
    }

    @Test public void getProviderForInstances() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MailService.class, MailService::new)
                .prototype(PrintService.class, PrintService::new)
                .singleton(BeanWithPostConstructSingleton.class, BeanWithPostConstructSingleton::new)
                .instance("123")
                .build();

        final Set<Provider<?>> instanceProviders = repo.getProvidersForInstances();
        Assert.assertEquals(1, instanceProviders.size());
        Assert.assertEquals("123", instanceProviders.iterator().next().get());
    }

    @Test public void postConstructOnDryRun() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(SomeBean.class, SomeBean::new)
                .prototype(BeanWithPostConstruct.class, BeanWithPostConstruct::new)
                .build();

        Assert.assertFalse(BeanWithPostConstruct.postConstructExecuted);

        // Getting the Provider should not set postConstructExecuted to true
        final Provider<BeanWithPostConstruct> provider = repo.getProvider(BeanWithPostConstruct.class);
        Assert.assertFalse(BeanWithPostConstruct.postConstructExecuted);

        // Getting the Bean from the Provider should set postConstructExecuted to true
        provider.get();
        Assert.assertTrue(BeanWithPostConstruct.postConstructExecuted);
    }
}

class BeanWithPostConstructSingleton implements PostConstructible {

    public static int postConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}

class BeanWithPostConstructPrototype implements PostConstructible {

    public static int postConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}

class BeanWithPostConstruct implements PostConstructible {

    public static boolean postConstructExecuted = false;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructExecuted = true;
    }
}

class SomeBean {

    private final BeanWithPostConstruct beanWithPostConstruct;

    public SomeBean(final BeanAccessor beans) {
        this.beanWithPostConstruct = beans.getProvider(BeanWithPostConstruct.class).get();
    }
}

class SomeService {

}

class SomeServiceFactory implements Factory<SomeService> {

    @Override public SomeService createInstance() {
        return new SomeService();
    }

    @Override public Class<SomeService> getBeanType() {
        return SomeService.class;
    }
}