package de.tse.beanrepository.example;

import com.github.tinosteinort.beanrepository.BeanAccessor;
import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.example.services.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * No valid Tests, but it shows the Functionality of the BeanRepository.
 */
public class ExampleTest {

    /**
     * MailService depends on PrintService
     */
    @Test public void singletonDependency() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .singleton(PrintService.class, PrintService::new)
                .build();

        final MailService mailService = repo.getBean(MailService.class);
        mailService.sendMail("You", "Hi!");
    }

    @Test public void instanceHandling() {

        final String[] values = new String[] { "a", "b", "c" } ;

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .instance(values)
                .build();

        for (String value : repo.getBean(String[].class)) {
            System.out.println(value);
        }
    }

    /**
     * For a Singleton the onPostConstruct Method must be called only once.
     */
    @Test public void singletonOnlyOnePostConstruct() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ServiceWithPostConstruct.class, ServiceWithPostConstruct::new)
                .build();

        repo.getBean(ServiceWithPostConstruct.class);
        repo.getBean(ServiceWithPostConstruct.class);
        repo.getBean(ServiceWithPostConstruct.class);
        // see System.out: only one output has to appear for this test
    }

    /**
     * For a Prototype the onPostConstruct Method must be called every time, the Prototype is needed.
     */
    @Test public void prototypeWithMultiplePostConstruct() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(ServiceWithPostConstruct.class, ServiceWithPostConstruct::new)
                .build();

        repo.getBean(ServiceWithPostConstruct.class);
        repo.getBean(ServiceWithPostConstruct.class);
        repo.getBean(ServiceWithPostConstruct.class);
        // see System.out: three outputs has to appear for this test
    }

    /**
     * ServiceA depends on ServiceB and ServiceB depends on ServiceA.
     *  This cyclic Reference should lead to an Error on start up, not
     *  until a Bean is requested by repo.getBean(...)
     */
    @Test(expected = StackOverflowError.class)
    public void cyclicReferenceCheckOnBeanRepositoryInitialisation() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ServiceA.class, ServiceA::new)
                .singleton(ServiceB.class, ServiceB::new)
                .build();

        throw new RuntimeException("Should never reach");
    }

    @Test public void getAllBeansOfType() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final Set<MyInterface> beansOfType = repo.getBeansOfType(MyInterface.class);
        Assert.assertNotNull(beansOfType);
        Assert.assertEquals(2, beansOfType.size());
    }

    /**
     * To collect beans of a specific Type, the onPostConstruct Method has to be used. It is not
     *  possible to collect those beans from the Constructor.
     */
    @Test public void collectBeansOfSpecificType() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(CollectorServiceOnPostConstruct.class, CollectorServiceOnPostConstruct::new)
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final CollectorServiceOnPostConstruct collector = repo.getBean(CollectorServiceOnPostConstruct.class);
        Assert.assertNotNull(collector);
        Assert.assertEquals(2, collector.getImplementations().size());
    }

    @Test(expected = ClassCastException.class)
    public void denyCastToBeanRepository() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(ServiceWithForbiddenCast.class, ServiceWithForbiddenCast::new)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerBeansWithSameClassMultipleTimes() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterface.class, MyInterfaceImpl1::new)
                .singleton(MyInterface.class, MyInterfaceImpl2::new)
                .build();
    }

    @Test public void registerBeansWithDifferentClassMultipleTimes() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        Assert.assertNotNull(repo.getBeansOfType(MyInterface.class));
        Assert.assertEquals(2, repo.getBeansOfType(MyInterface.class).size());
        Assert.assertNotNull(repo.getBean(MyInterfaceImpl1.class));
        Assert.assertNotNull(repo.getBean(MyInterfaceImpl2.class));
        Assert.assertNotEquals(repo.getBean(MyInterfaceImpl1.class), repo.getBean(MyInterfaceImpl2.class));
    }

    @Test public void collectBeansOverModuleBounds() {

        final BeanRepository repo1 = new BeanRepository.BeanRepositoryBuilder("LogicModule")
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository repo2 = new BeanRepository.BeanRepositoryBuilder("DataModule")
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder("CompositeModule")
                .build(repo1, repo2);

        final Set<MyInterface> beans = repo.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beans.size());
    }

    @Test public void dependencyToOtherModule() {

        final BeanRepository repo1 = new BeanRepository.BeanRepositoryBuilder("BaseModule")
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder("AdvancedModule")
                .singleton(MailService.class, MailService::new)
                .build(repo1);

        final MailService mailService = repo.getBean(MailService.class);
        mailService.sendMail("You", "Hi again!");
    }

    @Test(expected = RuntimeException.class)
    public void dependentBeansNotAllowedInDifferentModules() {

        final BeanRepository repo1 = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository repo2 = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .build(); // requires PrintService -> not available -> Error
    }

    @Test(expected = RuntimeException.class)
    public void sameBeanInDifferentModulesNotAllowed() {

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder("Module1")
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder("Module2")
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder("ApplicationBeans")
                .build(module1, module2);
    }

    @Test public void prototypeWithParameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .build();

        final String param = "parameterGeneratedAtRuntime";

        final ServiceWithParameter service = repo.getBean(() -> new ServiceWithParameter(param));
        service.print("123");
    }

    @Test public void prototypeWithBeanDependencyAndParameter() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .build();

        final String param = "parameterGeneratedAtRuntime";

        final ServiceWithBeanDependenciesAndParameter service =
                repo.getBean((BeanAccessor beans) -> new ServiceWithBeanDependenciesAndParameter(beans, param));

        service.print("PrintService is used to print Text");
    }

    @Test public void prototypeWithParameterAndPostConstructible() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final String param = "PostConstructiblePrototype";

        final ServiceWithParameterPostConstructible service =
                repo.getBean(() -> new ServiceWithParameterPostConstructible(param));

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

        Assert.assertEquals(0, ServiceWithPostConstructCounter.getPostConstructCount());
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
}
