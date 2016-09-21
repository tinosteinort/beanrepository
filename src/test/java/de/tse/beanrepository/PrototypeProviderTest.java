package de.tse.beanrepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class PrototypeProviderTest {

    @Test public void differentInstancesEveryCall() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MyPrototypeService.class, MyPrototypeService::new)
                .build();

        final Function<BeanAccessor, MyPrototypeService> creator = repository -> new MyPrototypeService();

        final PrototypeProvider provider = new PrototypeProvider(null, creator);


        MyPrototypeService bean1 = provider.getBean(repo, false);
        Assert.assertNotNull(bean1);

        MyPrototypeService bean2 = provider.getBean(repo, false);
        Assert.assertNotNull(bean2);

        Assert.assertFalse(bean1 == bean2);
    }

    @Test public void executePostConstructOnEveryCall() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MyPrototypeServiceWithPostConstruct.class, MyPrototypeServiceWithPostConstruct::new)
                .build();

        final Function<BeanAccessor, MyPrototypeServiceWithPostConstruct> creator =
                repository -> new MyPrototypeServiceWithPostConstruct();

        final PrototypeProvider provider = new PrototypeProvider(null, creator);

        MyPrototypeServiceWithPostConstruct bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
        bean = provider.getBean(repo, false);
        Assert.assertEquals(1, bean.postConstructCounter);
    }

    @Test public void dontExecutePostConstructOnDryRun() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(MyPrototypeServiceWithPostConstruct.class, MyPrototypeServiceWithPostConstruct::new)
                .build();

        final Function<BeanAccessor, MyPrototypeServiceWithPostConstruct> creator =
                repository -> new MyPrototypeServiceWithPostConstruct();

        final PrototypeProvider provider = new PrototypeProvider(null, creator);

        MyPrototypeServiceWithPostConstruct bean = provider.getBean(repo, true);
        Assert.assertEquals(0, bean.postConstructCounter);
        bean = provider.getBean(repo, true);
        Assert.assertEquals(0, bean.postConstructCounter);
    }
}

class MyPrototypeService {

}

class MyPrototypeServiceWithPostConstruct implements PostConstructible {

    int postConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}
