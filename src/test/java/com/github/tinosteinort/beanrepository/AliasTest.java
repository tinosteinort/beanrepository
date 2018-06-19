package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AliasTest {

    @Test(expected = RuntimeException.class)
    public void noAliasFound() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .build();

        repository.getBean(SomeServiceInterface.class);
    }

    @Test public void aliasFound() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();

        SomeServiceImpl service = repository.getBean(SomeServiceInterface.class);
        assertNotNull(service);
    }

    @Test public void aliasSingletonIsSame() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();

        SomeServiceImpl service = repository.getBean(SomeServiceImpl.class);
        SomeServiceImpl alias = repository.getBean(SomeServiceInterface.class);

        assertNotNull(service);
        assertNotNull(alias);

        Assert.assertTrue(service == alias);
    }

    @Test public void aliasBeanProviderDeliversSame() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();

        final Provider<SomeServiceImpl> provider = repository.getProvider(SomeServiceImpl.class);
        final Provider<SomeServiceImpl> aliasProvider = repository.getProvider(SomeServiceInterface.class);

        assertNotNull(provider);
        assertNotNull(aliasProvider);

        Assert.assertTrue(provider.get() == aliasProvider.get());
    }

    @Test public void twoAliasesForSameBean() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .alias(SomeSecondServiceInterface.class, SomeServiceImpl.class)
                .build();

        final SomeServiceInterface alias1 = repository.getBean(SomeServiceInterface.class);
        final SomeSecondServiceInterface alias2 = repository.getBean(SomeSecondServiceInterface.class);

        assertNotNull(alias1);
        assertNotNull(alias2);
        Assert.assertEquals(alias1, alias2);
        Assert.assertTrue(alias1 == alias2);
    }

    @Test(expected = Exception.class)
    public void denySameAliasClassMultipleTimes() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceImpl.class, SomeServiceImpl::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();
    }

    @Test public void aliasTriggersPostConstruct() {

        SomeServiceWithPostConstructImpl.initCounter = 0;

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .prototype(SomeServiceWithPostConstructImpl.class, SomeServiceWithPostConstructImpl::new)
                .alias(SomeServiceWithPostConstructInterface.class, SomeServiceWithPostConstructImpl.class)
                .build();

        Assert.assertEquals(0, SomeServiceWithPostConstructImpl.initCounter);

        repository.getBean(SomeServiceWithPostConstructInterface.class);
        Assert.assertEquals(1, SomeServiceWithPostConstructImpl.initCounter);
    }

    @Test public void aliasDoesntTriggerAdditionalPostConstruct() {

        SomeServiceWithPostConstructImpl.initCounter = 0;

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceWithPostConstructImpl.class, SomeServiceWithPostConstructImpl::new)
                .alias(SomeServiceWithPostConstructInterface.class, SomeServiceWithPostConstructImpl.class)
                .build();

        Assert.assertEquals(1, SomeServiceWithPostConstructImpl.initCounter);

        repository.getBean(SomeServiceWithPostConstructImpl.class);
        Assert.assertEquals(1, SomeServiceWithPostConstructImpl.initCounter);

        repository.getBean(SomeServiceWithPostConstructInterface.class);
        Assert.assertEquals(1, SomeServiceWithPostConstructImpl.initCounter);
    }

    @Test public void aliasForFactory() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(SomeServiceImpl.class, SomeServiceImplFactory::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();

        final SomeServiceImpl service = repository.getBean(SomeServiceImpl.class);
        final SomeServiceInterface alias = repository.getBean(SomeServiceInterface.class);

        assertNotNull(service);
        assertNotNull(alias);

        assertEquals(service, alias);
        assertTrue(System.identityHashCode(service) == System.identityHashCode(alias));
    }

    @Test public void aliasAsReference() {

        final BeanRepository repository = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ReferencingAliasService.class, ReferencingAliasService::new, SomeServiceInterface.class)
                .singletonFactory(SomeServiceImpl.class, SomeServiceImplFactory::new)
                .alias(SomeServiceInterface.class, SomeServiceImpl.class)
                .build();

        final ReferencingAliasService service = repository.getBean(ReferencingAliasService.class);

        assertNotNull(service);
        assertNotNull(service.getSomeServiceInterface());
    }
}

class SomeServiceImpl implements SomeServiceInterface, SomeSecondServiceInterface {

}

class SomeServiceImplFactory implements Factory<SomeServiceImpl> {

    @Override public SomeServiceImpl createInstance() {
        return new SomeServiceImpl();
    }
}

interface SomeServiceInterface {

}

interface SomeSecondServiceInterface {

}

class SomeServiceWithPostConstructImpl implements SomeServiceWithPostConstructInterface, PostConstructible {

    static int initCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        initCounter++;
    }
}

interface  SomeServiceWithPostConstructInterface {

}

class ReferencingAliasService {

    private final SomeServiceInterface someServiceInterface;

    public ReferencingAliasService(final SomeServiceInterface someServiceInterface) {
        this.someServiceInterface = someServiceInterface;
    }

    public SomeServiceInterface getSomeServiceInterface() {
        return someServiceInterface;
    }
}