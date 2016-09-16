package de.tse.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class InstanceProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void denyNullInstance() {

        new InstanceProvider(null);
    }

    @Test public void instanceNotNull() {

        final Object object = new Object();
        final InstanceProvider provider = new InstanceProvider(object);

        Assert.assertNotNull(provider.getBean(null, false));
    }

    @Test public void instanceSameObject() {

        final Object object = new Object();
        final InstanceProvider provider = new InstanceProvider(object);

        Assert.assertTrue(object == provider.getBean(null, false));
    }
}
