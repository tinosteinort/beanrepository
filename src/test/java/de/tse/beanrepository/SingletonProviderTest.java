package de.tse.beanrepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class SingletonProviderTest {

    @Test public void onlyOneInstance() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MySingletonService.class, MySingletonService::new)
                .build();

        final Function<BeanAccessor, MySingletonService> creator = repoository -> new MySingletonService();

        final SingletonProvider provider = new SingletonProvider(null, creator);

        MySingletonService bean1 = provider.getBean(repo, false);
        Assert.assertNotNull(bean1);

        MySingletonService bean2 = provider.getBean(repo, false);
        Assert.assertNotNull(bean2);

        Assert.assertTrue(bean1 == bean2);
    }

    @Test public void onlyOnePostConstruct() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MySingletonServiceWithPostConstruct.class, MySingletonServiceWithPostConstruct::new)
                .build();

        final Function<BeanAccessor, MySingletonServiceWithPostConstruct> creator =
                repository -> new MySingletonServiceWithPostConstruct();

        final SingletonProvider provider = new SingletonProvider(null, creator);


        MySingletonServiceWithPostConstruct bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
        bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
    }

    @Test public void noPostConstructOnDryRun() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MySingletonServiceWithPostConstruct.class, MySingletonServiceWithPostConstruct::new)
                .build();

        final Function<BeanAccessor, MySingletonServiceWithPostConstruct> creator =
                repository -> new MySingletonServiceWithPostConstruct();

        final SingletonProvider provider = new SingletonProvider(null, creator);


        MySingletonServiceWithPostConstruct bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
        bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
    }
}

class MySingletonService {

}

class MySingletonServiceWithPostConstruct implements PostConstructible {

    int postConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}
