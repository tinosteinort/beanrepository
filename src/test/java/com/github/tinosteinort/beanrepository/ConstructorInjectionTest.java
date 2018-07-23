package com.github.tinosteinort.beanrepository;

import org.junit.Assert;
import org.junit.Test;

public class ConstructorInjectionTest {

    @Test public void singletonServiceWith1Dependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(ServiceWith1Dependency.class, ServiceWith1Dependency::new, Service1.class)
                .build();

        final ServiceWith1Dependency service = repo.getBean(ServiceWith1Dependency.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
    }

    @Test public void singletonServiceWith2Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(ServiceWith2Dependencies.class, ServiceWith2Dependencies::new, Service1.class, Service2.class)
                .build();

        final ServiceWith2Dependencies service = repo.getBean(ServiceWith2Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
    }

    @Test public void singletonServiceWith3Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singleton(ServiceWith3Dependencies.class, ServiceWith3Dependencies::new, Service1.class, Service2.class, Service3.class)
                .build();

        final ServiceWith3Dependencies service = repo.getBean(ServiceWith3Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
    }

    @Test public void singletonServiceWith4Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singleton(Service4.class, Service4::new)
                .singleton(ServiceWith4Dependencies.class, ServiceWith4Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class)
                .build();

        final ServiceWith4Dependencies service = repo.getBean(ServiceWith4Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service4.class), service.getService4());
    }

    @Test public void singletonServiceWith5Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singleton(Service4.class, Service4::new)
                .singleton(Service5.class, Service5::new)
                .singleton(ServiceWith5Dependencies.class, ServiceWith5Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class, Service5.class)
                .build();

        final ServiceWith5Dependencies service = repo.getBean(ServiceWith5Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service5.class), service.getService5());
    }

    @Test public void prototypeServiceWith1Dependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(ServiceWith1Dependency.class, ServiceWith1Dependency::new, Service1.class)
                .build();

        final ServiceWith1Dependency service = repo.getBean(ServiceWith1Dependency.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
    }

    @Test public void prototypeServiceWith2Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(ServiceWith2Dependencies.class, ServiceWith2Dependencies::new, Service1.class, Service2.class)
                .build();

        final ServiceWith2Dependencies service = repo.getBean(ServiceWith2Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
    }

    @Test public void prototypeServiceWith3Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototype(ServiceWith3Dependencies.class, ServiceWith3Dependencies::new, Service1.class, Service2.class, Service3.class)
                .build();

        final ServiceWith3Dependencies service = repo.getBean(ServiceWith3Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
    }

    @Test public void prototypeServiceWith4Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototype(Service4.class, Service4::new)
                .prototype(ServiceWith4Dependencies.class, ServiceWith4Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class)
                .build();

        final ServiceWith4Dependencies service = repo.getBean(ServiceWith4Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
        Assert.assertNotEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service4.class).getClass(), service.getService4().getClass());
    }

    @Test public void prototypeServiceWith5Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototype(Service4.class, Service4::new)
                .prototype(Service5.class, Service5::new)
                .prototype(ServiceWith5Dependencies.class, ServiceWith5Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class, Service5.class)
                .build();

        final ServiceWith5Dependencies service = repo.getBean(ServiceWith5Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
        Assert.assertNotEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service4.class).getClass(), service.getService4().getClass());
        Assert.assertNotEquals(repo.getBean(Service5.class), service.getService5());
        Assert.assertEquals(repo.getBean(Service5.class).getClass(), service.getService5().getClass());
    }

    @Test public void singletonServiceFactoryWith1Dependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singletonFactory(ServiceWith1Dependency.class, ServiceFactoryWith1Dependency::new, Service1.class)
                .build();

        final ServiceWith1Dependency service = repo.getBean(ServiceWith1Dependency.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
    }

    @Test public void singletonServiceFactoryWith2Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singletonFactory(ServiceWith2Dependencies.class, ServiceFactoryWith2Dependencies::new, Service1.class, Service2.class)
                .build();

        final ServiceWith2Dependencies service = repo.getBean(ServiceWith2Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
    }

    @Test public void singletonServiceFactoryWith3Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singletonFactory(ServiceWith3Dependencies.class, ServiceFactoryWith3Dependencies::new, Service1.class, Service2.class, Service3.class)
                .build();

        final ServiceWith3Dependencies service = repo.getBean(ServiceWith3Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
    }

    @Test public void singletonServiceFactoryWith4Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singleton(Service4.class, Service4::new)
                .singletonFactory(ServiceWith4Dependencies.class, ServiceFactoryWith4Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class)
                .build();

        final ServiceWith4Dependencies service = repo.getBean(ServiceWith4Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service4.class), service.getService4());
    }

    @Test public void singletonServiceFactoryWith5Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(Service2.class, Service2::new)
                .singleton(Service3.class, Service3::new)
                .singleton(Service4.class, Service4::new)
                .singleton(Service5.class, Service5::new)
                .singletonFactory(ServiceWith5Dependencies.class, ServiceFactoryWith5Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class, Service5.class)
                .build();

        final ServiceWith5Dependencies service = repo.getBean(ServiceWith5Dependencies.class);

        Assert.assertEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service5.class), service.getService5());
    }

    @Test public void prototypeServiceFactoryWith1Dependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototypeFactory(ServiceWith1Dependency.class, ServiceFactoryWith1Dependency::new, Service1.class)
                .build();

        final ServiceWith1Dependency service = repo.getBean(ServiceWith1Dependency.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
    }

    @Test public void prototypeServiceFactoryWith2Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototypeFactory(ServiceWith2Dependencies.class, ServiceFactoryWith2Dependencies::new, Service1.class, Service2.class)
                .build();

        final ServiceWith2Dependencies service = repo.getBean(ServiceWith2Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
    }

    @Test public void prototypeServiceFactoryWith3Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototypeFactory(ServiceWith3Dependencies.class, ServiceFactoryWith3Dependencies::new, Service1.class, Service2.class, Service3.class)
                .build();

        final ServiceWith3Dependencies service = repo.getBean(ServiceWith3Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
    }

    @Test public void prototypeServiceFactoryWith4Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototype(Service4.class, Service4::new)
                .prototypeFactory(ServiceWith4Dependencies.class, ServiceFactoryWith4Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class)
                .build();

        final ServiceWith4Dependencies service = repo.getBean(ServiceWith4Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
        Assert.assertNotEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service4.class).getClass(), service.getService4().getClass());
    }

    @Test public void prototypeServiceFactoryWith5Dependencies() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(Service1.class, Service1::new)
                .prototype(Service2.class, Service2::new)
                .prototype(Service3.class, Service3::new)
                .prototype(Service4.class, Service4::new)
                .prototype(Service5.class, Service5::new)
                .prototypeFactory(ServiceWith5Dependencies.class, ServiceFactoryWith5Dependencies::new, Service1.class, Service2.class, Service3.class, Service4.class, Service5.class)
                .build();

        final ServiceWith5Dependencies service = repo.getBean(ServiceWith5Dependencies.class);

        Assert.assertNotEquals(repo.getBean(Service1.class), service.getService1());
        Assert.assertEquals(repo.getBean(Service1.class).getClass(), service.getService1().getClass());
        Assert.assertNotEquals(repo.getBean(Service2.class), service.getService2());
        Assert.assertEquals(repo.getBean(Service2.class).getClass(), service.getService2().getClass());
        Assert.assertNotEquals(repo.getBean(Service3.class), service.getService3());
        Assert.assertEquals(repo.getBean(Service3.class).getClass(), service.getService3().getClass());
        Assert.assertNotEquals(repo.getBean(Service4.class), service.getService4());
        Assert.assertEquals(repo.getBean(Service4.class).getClass(), service.getService4().getClass());
        Assert.assertNotEquals(repo.getBean(Service5.class), service.getService5());
        Assert.assertEquals(repo.getBean(Service5.class).getClass(), service.getService5().getClass());
    }

    @Test public void onPostConstructWorksWithSingletonConstructorInjection() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singleton(SingletonServiceWithPostConstruct.class, SingletonServiceWithPostConstruct::new, Service1.class)
                .build();

        SingletonServiceWithPostConstruct service = repo.getBean(SingletonServiceWithPostConstruct.class);
        Assert.assertEquals(1, SingletonServiceWithPostConstruct.postConstructCounter);

        service = repo.getBean(SingletonServiceWithPostConstruct.class);
        Assert.assertEquals(1, SingletonServiceWithPostConstruct.postConstructCounter);
    }

    @Test public void onPostConstructWorksWithPrototypeConstructorInjection() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .prototype(PrototypeServiceWithPostConstruct.class, PrototypeServiceWithPostConstruct::new, Service1.class)
                .build();

        PrototypeServiceWithPostConstruct service = repo.getBean(PrototypeServiceWithPostConstruct.class);
        Assert.assertEquals(1, PrototypeServiceWithPostConstruct.postConstructCounter);

        service = repo.getBean(PrototypeServiceWithPostConstruct.class);
        Assert.assertEquals(2, PrototypeServiceWithPostConstruct.postConstructCounter);
    }

    @Test public void onPostConstructWorksWithSingletonFactoryConstructorInjection() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .singletonFactory(SingletonServiceWithPostConstruct2.class, SingletonServiceWithPostConstruct2Factory::new, Service1.class)
                .build();

        SingletonServiceWithPostConstruct2 service = repo.getBean(SingletonServiceWithPostConstruct2.class);
        Assert.assertEquals(1, SingletonServiceWithPostConstruct2.postConstructCounter);

        service = repo.getBean(SingletonServiceWithPostConstruct2.class);
        Assert.assertEquals(1, SingletonServiceWithPostConstruct2.postConstructCounter);
    }

    @Test public void onPostConstructWorksWithPrototypeFactoryConstructorInjection() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(Service1.class, Service1::new)
                .prototypeFactory(PrototypeServiceWithPostConstruct2.class, PrototypeServiceWithPostConstruct2Factory::new, Service1.class)
                .build();

        PrototypeServiceWithPostConstruct2 service = repo.getBean(PrototypeServiceWithPostConstruct2.class);
        Assert.assertEquals(1, PrototypeServiceWithPostConstruct2.postConstructCounter);

        service = repo.getBean(PrototypeServiceWithPostConstruct2.class);
        Assert.assertEquals(2, PrototypeServiceWithPostConstruct2.postConstructCounter);
    }
}

class Service1 {

}

class Service2 {

}

class Service3 {

}

class Service4 {

}

class Service5 {

}

class ServiceWith1Dependency {

    private final Service1 service1;

    public ServiceWith1Dependency(final Service1 service1) {
        this.service1 = service1;
    }

    public Service1 getService1() {
        return service1;
    }
}

class ServiceWith2Dependencies {

    private final Service1 service1;
    private final Service2 service2;

    public ServiceWith2Dependencies(final Service1 service1, final Service2 service2) {
        this.service1 = service1;
        this.service2 = service2;
    }

    public Service1 getService1() {
        return service1;
    }
    public Service2 getService2() {
        return service2;
    }
}

class ServiceWith3Dependencies {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;

    public ServiceWith3Dependencies(final Service1 service1, final Service2 service2, final Service3 service3) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
    }

    public Service1 getService1() {
        return service1;
    }
    public Service2 getService2() {
        return service2;
    }
    public Service3 getService3() {
        return service3;
    }
}

class ServiceWith4Dependencies {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;
    private final Service4 service4;

    public ServiceWith4Dependencies(final Service1 service1, final Service2 service2, final Service3 service3, final Service4 service4) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
        this.service4 = service4;
    }

    public Service1 getService1() {
        return service1;
    }
    public Service2 getService2() {
        return service2;
    }
    public Service3 getService3() {
        return service3;
    }
    public Service4 getService4() {
        return service4;
    }
}

class ServiceWith5Dependencies {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;
    private final Service4 service4;
    private final Service5 service5;

    public ServiceWith5Dependencies(final Service1 service1, final Service2 service2, final Service3 service3, final Service4 service4, final Service5 service5) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
        this.service4 = service4;
        this.service5 = service5;
    }

    public Service1 getService1() {
        return service1;
    }
    public Service2 getService2() {
        return service2;
    }
    public Service3 getService3() {
        return service3;
    }
    public Service4 getService4() {
        return service4;
    }
    public Service5 getService5() {
        return service5;
    }
}

class ServiceFactoryWith1Dependency implements Factory<ServiceWith1Dependency> {

    private final Service1 service1;

    public ServiceFactoryWith1Dependency(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public ServiceWith1Dependency createInstance() {
        return new ServiceWith1Dependency(service1);
    }

    @Override public Class<ServiceWith1Dependency> getBeanType() {
        return ServiceWith1Dependency.class;
    }
}

class ServiceFactoryWith2Dependencies implements Factory<ServiceWith2Dependencies> {

    private final Service1 service1;
    private final Service2 service2;

    public ServiceFactoryWith2Dependencies(final Service1 service1, final Service2 service2) {
        this.service1 = service1;
        this.service2 = service2;
    }

    @Override public ServiceWith2Dependencies createInstance() {
        return new ServiceWith2Dependencies(service1, service2);
    }

    @Override public Class<ServiceWith2Dependencies> getBeanType() {
        return ServiceWith2Dependencies.class;
    }
}

class ServiceFactoryWith3Dependencies implements Factory<ServiceWith3Dependencies> {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;

    public ServiceFactoryWith3Dependencies(final Service1 service1, final Service2 service2, final Service3 service3) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
    }

    @Override public ServiceWith3Dependencies createInstance() {
        return new ServiceWith3Dependencies(service1, service2, service3);
    }

    @Override public Class<ServiceWith3Dependencies> getBeanType() {
        return ServiceWith3Dependencies.class;
    }
}

class ServiceFactoryWith4Dependencies implements Factory<ServiceWith4Dependencies> {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;
    private final Service4 service4;

    public ServiceFactoryWith4Dependencies(final Service1 service1, final Service2 service2, final Service3 service3, final Service4 service4) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
        this.service4 = service4;
    }

    @Override public ServiceWith4Dependencies createInstance() {
        return new ServiceWith4Dependencies(service1, service2, service3, service4);
    }

    @Override public Class<ServiceWith4Dependencies> getBeanType() {
        return ServiceWith4Dependencies.class;
    }
}

class ServiceFactoryWith5Dependencies implements Factory<ServiceWith5Dependencies> {

    private final Service1 service1;
    private final Service2 service2;
    private final Service3 service3;
    private final Service4 service4;
    private final Service5 service5;

    public ServiceFactoryWith5Dependencies(final Service1 service1, final Service2 service2, final Service3 service3, final Service4 service4, final Service5 service5) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
        this.service4 = service4;
        this.service5 = service5;
    }

    @Override public ServiceWith5Dependencies createInstance() {
        return new ServiceWith5Dependencies(service1, service2, service3, service4, service5);
    }

    @Override public Class<ServiceWith5Dependencies> getBeanType() {
        return ServiceWith5Dependencies.class;
    }
}

class SingletonServiceWithPostConstruct implements PostConstructible {

    private final Service1 service1;

    public static int postConstructCounter;

    public SingletonServiceWithPostConstruct(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}


class PrototypeServiceWithPostConstruct implements PostConstructible {

    private final Service1 service1;

    public static int postConstructCounter;

    public PrototypeServiceWithPostConstruct(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}

class SingletonServiceWithPostConstruct2 implements PostConstructible {

    private final Service1 service1;

    public static int postConstructCounter;

    public SingletonServiceWithPostConstruct2(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}

class PrototypeServiceWithPostConstruct2 implements PostConstructible {

    private final Service1 service1;

    public static int postConstructCounter;

    public PrototypeServiceWithPostConstruct2(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public void onPostConstruct(final BeanRepository repository) {
        postConstructCounter++;
    }
}

class SingletonServiceWithPostConstruct2Factory implements Factory<SingletonServiceWithPostConstruct2> {

    private final Service1 service1;

    public SingletonServiceWithPostConstruct2Factory(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public SingletonServiceWithPostConstruct2 createInstance() {
        return new SingletonServiceWithPostConstruct2(service1);
    }

    @Override public Class<SingletonServiceWithPostConstruct2> getBeanType() {
        return SingletonServiceWithPostConstruct2.class;
    }
}

class PrototypeServiceWithPostConstruct2Factory implements Factory<PrototypeServiceWithPostConstruct2> {

    private final Service1 service1;

    public PrototypeServiceWithPostConstruct2Factory(final Service1 service1) {
        this.service1 = service1;
    }

    @Override public PrototypeServiceWithPostConstruct2 createInstance() {
        return new PrototypeServiceWithPostConstruct2(service1);
    }

    @Override public Class<PrototypeServiceWithPostConstruct2> getBeanType() {
        return PrototypeServiceWithPostConstruct2.class;
    }
}

