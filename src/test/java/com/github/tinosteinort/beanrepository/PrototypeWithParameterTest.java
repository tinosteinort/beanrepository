package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class PrototypeWithParameterTest {

    @Test public void prototypeWith1Parameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final Prototype1 proto = repo.getPrototypeBean(Prototype1::new, "1");
        Assert.assertEquals("1", proto.getParam1());
    }

    @Test public void prototypeWith2Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final Prototype2 proto = repo.getPrototypeBean(Prototype2::new, "1", "2");
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
    }

    @Test public void prototypeWith3Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final Prototype3 proto = repo.getPrototypeBean(Prototype3::new, "1", "2", "3");
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
    }

    @Test public void prototypeWith4Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final Prototype4 proto = repo.getPrototypeBean(Prototype4::new, "1", "2", "3", "4");
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
        Assert.assertEquals("4", proto.getParam4());
    }

    @Test public void prototypeWith5Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final Prototype5 proto = repo.getPrototypeBean(Prototype5::new, "1", "2", "3", "4", "5");
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
        Assert.assertEquals("4", proto.getParam4());
        Assert.assertEquals("5", proto.getParam5());
    }

    @Test public void prototypeWithBeansAnd1Parameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final PrototypeWithBeans1 proto = repo.getPrototypeBean(PrototypeWithBeans1::new, "1");
        Assert.assertEquals(repo.accessor(), proto.getBeans());
        Assert.assertEquals("1", proto.getParam1());
    }

    @Test public void prototypeWithBeansAnd2Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final PrototypeWithBeans2 proto = repo.getPrototypeBean(PrototypeWithBeans2::new, "1", "2");
        Assert.assertEquals(repo.accessor(), proto.getBeans());
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
    }

    @Test public void prototypeWithBeansAnd3Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final PrototypeWithBeans3 proto = repo.getPrototypeBean(PrototypeWithBeans3::new, "1", "2", "3");
        Assert.assertEquals(repo.accessor(), proto.getBeans());
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
    }

    @Test public void prototypeWithBeansAnd4Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final PrototypeWithBeans4 proto = repo.getPrototypeBean(PrototypeWithBeans4::new, "1", "2", "3", "4");
        Assert.assertEquals(repo.accessor(), proto.getBeans());
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
        Assert.assertEquals("4", proto.getParam4());
    }

    @Test public void prototypeWithBeansAnd5Parameters() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final PrototypeWithBeans5 proto = repo.getPrototypeBean(PrototypeWithBeans5::new, "1", "2", "3", "4", "5");
        Assert.assertEquals(repo.accessor(), proto.getBeans());
        Assert.assertEquals("1", proto.getParam1());
        Assert.assertEquals("2", proto.getParam2());
        Assert.assertEquals("3", proto.getParam3());
        Assert.assertEquals("4", proto.getParam4());
        Assert.assertEquals("5", proto.getParam5());
    }
}

class Prototype1 {

    private final String param1;

    Prototype1(final String param1) {
        this.param1 = param1;
    }

    public String getParam1() {
        return param1;
    }
}

class Prototype2 {

    private final String param1;
    private final String param2;

    Prototype2(final String param1, final String param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
}

class Prototype3 {

    private final String param1;
    private final String param2;
    private final String param3;

    Prototype3(final String param1, final String param2, final String param3) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
}

class Prototype4 {

    private final String param1;
    private final String param2;
    private final String param3;
    private final String param4;

    Prototype4(final String param1, final String param2, final String param3, final String param4) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
    public String getParam4() {
        return param4;
    }
}

class Prototype5 {

    private final String param1;
    private final String param2;
    private final String param3;
    private final String param4;
    private final String param5;

    Prototype5(final String param1, final String param2, final String param3, final String param4,
            final String param5) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }

    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
    public String getParam4() {
        return param4;
    }
    public String getParam5() {
        return param5;
    }
}


class PrototypeWithBeans1 {

    private final BeanAccessor beans;
    private final String param1;

    PrototypeWithBeans1(final BeanAccessor beans, final String param1) {
        this.beans = beans;
        this.param1 = param1;
    }

    public BeanAccessor getBeans() {
        return beans;
    }
    public String getParam1() {
        return param1;
    }
}

class PrototypeWithBeans2 {

    private final BeanAccessor beans;
    private final String param1;
    private final String param2;

    PrototypeWithBeans2(final BeanAccessor beans, final String param1, final String param2) {
        this.beans = beans;
        this.param1 = param1;
        this.param2 = param2;
    }

    public BeanAccessor getBeans() {
        return beans;
    }
    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
}

class PrototypeWithBeans3 {

    private final BeanAccessor beans;
    private final String param1;
    private final String param2;
    private final String param3;

    PrototypeWithBeans3(final BeanAccessor beans, final String param1, final String param2, final String param3) {
        this.beans = beans;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public BeanAccessor getBeans() {
        return beans;
    }
    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
}

class PrototypeWithBeans4 {

    private final BeanAccessor beans;
    private final String param1;
    private final String param2;
    private final String param3;
    private final String param4;

    PrototypeWithBeans4(final BeanAccessor beans, final String param1, final String param2, final String param3,
                        final String param4) {
        this.beans = beans;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    public BeanAccessor getBeans() {
        return beans;
    }
    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
    public String getParam4() {
        return param4;
    }
}

class PrototypeWithBeans5 {

    private final BeanAccessor beans;
    private final String param1;
    private final String param2;
    private final String param3;
    private final String param4;
    private final String param5;

    PrototypeWithBeans5(final BeanAccessor beans, final String param1, final String param2, final String param3,
                        final String param4, final String param5) {
        this.beans = beans;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }

    public BeanAccessor getBeans() {
        return beans;
    }
    public String getParam1() {
        return param1;
    }
    public String getParam2() {
        return param2;
    }
    public String getParam3() {
        return param3;
    }
    public String getParam4() {
        return param4;
    }
    public String getParam5() {
        return param5;
    }
}
