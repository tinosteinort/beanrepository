package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MultipleConfiguratorTest {

    @Test public void multipleConfigurators() {

        Executed executed = new Executed();

        BeanRepositoryConfigurator configurator1 = new BeanRepositoryConfigurator() {
            @Override
            public void configure(BeanRepository.BeanRepositoryBuilder builder) {
                builder.singleton(Bean1.class, Bean1::new);
            }
        };
        BeanRepositoryConfigurator configurator2 = new BeanRepositoryConfigurator() {
            @Override
            public void configure(BeanRepository.BeanRepositoryBuilder builder) {
                builder.singleton(Bean2.class, Bean2::new);
            }
        };
        BeanRepositoryConfigurator configurator3 = new BeanRepositoryConfigurator() {
            @Override
            public void configure(BeanRepository.BeanRepositoryBuilder builder) {
                builder.singleton(TestBean.class, () -> new TestBean(executed));
            }
        };

        BeanRepositoryApplication.run(new String[0], configurator1, configurator2, configurator3);

        assertTrue(executed.executed);
    }

    @Test(expected = NullPointerException.class)
    public void argsRequired() {

        BeanRepositoryApplication.run(null, new BeanRepositoryConfigurator() {
            @Override
            public void configure(BeanRepository.BeanRepositoryBuilder builder) {

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void configuratorsRequiredNotNull() {

        BeanRepositoryApplication.run(new String[0], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configuratorsRequiredNotEmpty() {

        BeanRepositoryApplication.run(new String[0], new BeanRepositoryConfigurator[0]);
    }
}

class Bean1 {

}

class Bean2 {

}

class TestBean implements PostConstructible {

    private final Executed executed;

    TestBean(Executed executed) {
        this.executed = executed;
    }

    @Override
    public void onPostConstruct(BeanRepository repository) {
        executed.executed = true;
        assertNotNull(repository.getBean(Bean1.class));
        assertNotNull(repository.getBean(Bean2.class));
    }
}