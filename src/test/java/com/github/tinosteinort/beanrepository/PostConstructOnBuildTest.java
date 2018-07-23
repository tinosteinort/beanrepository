package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for Issue
 * <br/><b>
 *  onPostConstruct of singleton beans is executed if the bean is requested, not when the BeanRepository is build
 * </b><br/>
 * https://github.com/tinosteinort/beanrepository/issues/4
 */
public class PostConstructOnBuildTest {

    @Test public void executePostConstructOnBuild() {

        Executed executed = new Executed();

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(PostConstructibleBean.class, () -> new PostConstructibleBean(executed))
                .build();

        assertTrue(executed.executed);
    }

    @Test public void executePostConstructOnBuildForFactoryBean() {

        Executed executed = new Executed();

        new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(PostBean.class, () -> new SomeBeanFactory(executed))
                .build();

        assertTrue(executed.executed);
    }
}

class PostConstructibleBean implements PostConstructible {

    private final Executed executed;

    PostConstructibleBean(final Executed executed) {
        this.executed = executed;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        executed.executed = true;
    }
}

class SomeBeanFactory implements Factory<PostBean> {

    private final Executed executed;

    SomeBeanFactory(final Executed executed) {
        this.executed = executed;
    }

    @Override public PostBean createInstance() {
        return new PostBean(executed);
    }

    @Override public Class<PostBean> getBeanType() {
        return PostBean.class;
    }
}

class PostBean implements PostConstructible {

    private final Executed executed;

    PostBean(final Executed executed) {
        this.executed = executed;
    }

    @Override public void onPostConstruct(BeanRepository repository) {
        executed.executed = true;
    }
}