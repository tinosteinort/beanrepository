package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class InstanceProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void denyNullInstance() {

        new InstanceProvider(null, null);
    }

    @Test public void instanceNotNull() {

        final Object object = new Object();
        final InstanceProvider provider = new InstanceProvider(null, object);

        Assert.assertNotNull(provider.getBean(null, DryRunAwareMock.NO_DRY_RUN));
    }

    @Test public void instanceSameObject() {

        final Object object = new Object();
        final InstanceProvider provider = new InstanceProvider(null, object);

        Assert.assertSame(object, provider.getBean(null, DryRunAwareMock.NO_DRY_RUN));
    }
}
