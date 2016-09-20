package de.tse.beanrepository.example;

import de.tse.beanrepository.BeanRepository;
import de.tse.beanrepository.example.services.*;
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
}
