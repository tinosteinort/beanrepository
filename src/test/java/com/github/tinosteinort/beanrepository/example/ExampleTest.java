package com.github.tinosteinort.beanrepository.example;

import com.github.tinosteinort.beanrepository.BeanDefinition;
import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.Scope;
import com.github.tinosteinort.beanrepository.example.services.FactoryWithDependency;
import com.github.tinosteinort.beanrepository.example.services.FactoryWithoutDependency;
import com.github.tinosteinort.beanrepository.example.services.MailService;
import com.github.tinosteinort.beanrepository.example.services.MyInterface;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl1;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl2;
import com.github.tinosteinort.beanrepository.example.services.PrintService;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithBeanDependenciesAndParameter;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithConstructorDependency;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithConstructorDependencyFactory;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithParameter;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithParameterPostConstructible;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithPostConstructCounter;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithoutPostConstruct;
import com.github.tinosteinort.beanrepository.example.services.SomeService;
import com.github.tinosteinort.beanrepository.example.services.SomeServiceFactory;
import com.github.tinosteinort.beanrepository.example.services.WriterService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertSame;

/**
 * No valid Tests, but it shows the Functionality of the BeanRepository.
 */
public class ExampleTest {

    @Test public void instanceHandling() {

        final String[] values = new String[] { "a", "b", "c" } ;

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .instance(values)
                .build();

        assertSame(values, repo.getBean(String[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerBeansWithSameClassMultipleTimes() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterface.class, MyInterfaceImpl1::new)
                .singleton(MyInterface.class, MyInterfaceImpl2::new)
                .build();
    }

    @Test public void modularisation() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("ParentModule")
                .singleton(PrintService.class, PrintService::new)
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder("OneModule", parent)
                .singleton(MailService.class, MailService::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder("AnotherModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final MailService mailService = module1.getBean(MailService.class);
        mailService.sendMail("You", "Hi!");

        final Set<MyInterface> beansOfModule2 = module2.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule2.size());
    }

    @Test public void prototypeWithParameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final String param = "parameterGeneratedAtRuntime";

        final ServiceWithParameter service = repo.getPrototypeBean(ServiceWithParameter::new, param);
        service.print("123");
    }

    @Test public void prototypeWithBeanDependencyAndParameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .build();

        final String param = "parameterGeneratedAtRuntime";

        final ServiceWithBeanDependenciesAndParameter service =
                repo.getPrototypeBean(ServiceWithBeanDependenciesAndParameter::new, param);

        service.print("PrintService is used to print Text");
    }

    @Test public void prototypeWithParameterAndPostConstructible() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final String param = "PostConstructiblePrototype";

        final ServiceWithParameterPostConstructible service =
                repo.getPrototypeBean(ServiceWithParameterPostConstructible::new, param);

        service.doSomething();
        Assert.assertEquals(2, service.getManyBeans().size()); // proofs that onPostConstruct(...) is executed for Prototype Beans with Parameter
    }

    /**
     * onPostConstructible() has to be executed only once
     */
    @Test public void singletonWithPrototypePostConstructibleDependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                // onPostConstruct() of this Bean was called twice before -> wrong
                .prototype(ServiceWithPostConstructCounter.class, ServiceWithPostConstructCounter::new)
                .singleton(ServiceWithoutPostConstruct.class, ServiceWithoutPostConstruct::new)
                .build();

        Assert.assertEquals(1, ServiceWithPostConstructCounter.getPostConstructCount());
        final ServiceWithoutPostConstruct service = repo.getBean(ServiceWithoutPostConstruct.class);
        Assert.assertEquals(1, ServiceWithPostConstructCounter.getPostConstructCount());
    }

    @Test public void singletonBeanWithPrototypeBeanInConstructor() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .prototype(PrintService.class, PrintService::new) // Should be initialised only once
                .build();

        final MailService mailService = repo.getBean(MailService.class);
        mailService.sendMail("You", "Hi!");
    }

    @Test public void factoryWithCustomCreatorMethod() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(SomeServiceFactory.class, SomeServiceFactory::new)
                .build();

        final SomeServiceFactory factory = repo.getBean(SomeServiceFactory.class);
        final SomeService service  = factory.create(1);

        service.print();
    }

    @Test public void singletonFactoryWithoutDependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singletonFactory(ServiceWithParameter.class, FactoryWithoutDependency::new)
                .build();

        final ServiceWithParameter service  = repo.getBean(ServiceWithParameter.class);

        service.print("Abc");
    }

    @Test public void singletonFactoryWithDependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singletonFactory(MailService.class, FactoryWithDependency::new)
                .build();

        final MailService service = repo.getBean(MailService.class);

        service.sendMail("You", "Hi!");
    }

    @Test public void constructorInjectionWithOneParam() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singleton(ServiceWithConstructorDependency.class, ServiceWithConstructorDependency::new, PrintService.class)
                .build();

        final ServiceWithConstructorDependency service = repo.getBean(ServiceWithConstructorDependency.class);

        service.doSomething();
    }

    @Test public void constructorInjectionForSingletonFactoryWithOneParam() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singletonFactory(ServiceWithConstructorDependency.class, ServiceWithConstructorDependencyFactory::new, PrintService.class)
                .build();

        final ServiceWithConstructorDependency service = repo.getBean(ServiceWithConstructorDependency.class);

        service.doSomething();
    }

    @Test public void externalBeanDefinition() {

        final BeanDefinition<PrintService> definition1 =
                BeanDefinition.create(Scope.SINGLETON, PrintService.class, PrintService::new);

        final BeanDefinition<ServiceWithConstructorDependency> definition2 =
                BeanDefinition.create(Scope.SINGLETON, ServiceWithConstructorDependency.class, ServiceWithConstructorDependency::new, PrintService.class);

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .definition(definition1)
                .definition(definition2)
                .build();

        final ServiceWithConstructorDependency printService = repo.getBean(ServiceWithConstructorDependency.class);
        printService.doSomething();
    }

    @Test public void beanWithAlias() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .alias(WriterService.class, PrintService.class)
                .build();

        final WriterService service = repo.getBean(WriterService.class);
        service.print("Written by alias");
    }
}
