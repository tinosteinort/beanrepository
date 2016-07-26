package de.tse.beanrepository.example;

import de.tse.beanrepository.BeanRepository;
import de.tse.beanrepository.example.services.MailService;
import de.tse.beanrepository.example.services.PrintService;
import de.tse.beanrepository.example.services.ServiceWithPostConstruct;
import org.junit.Test;

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

        final MailService mailService = repo.get(MailService.class);
        mailService.sendMail("You", "Hi!");
    }

    @Test public void instanceHandling() {

        final String[] values = new String[] { "a", "b", "c" } ;

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .instance(values)
                .build();

        for (String value : repo.get(String[].class)) {
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

        repo.get(ServiceWithPostConstruct.class);
        repo.get(ServiceWithPostConstruct.class);
        repo.get(ServiceWithPostConstruct.class);
        // see System.out: only one output has to appear for this test
    }

    /**
     * For a Prototype the onPostConstruct Method must be called every time, the Prototype is needed.
     */
    @Test public void prototypeWithMultiplePostConstruct() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .prototype(ServiceWithPostConstruct.class, ServiceWithPostConstruct::new)
                .build();

        repo.get(ServiceWithPostConstruct.class);
        repo.get(ServiceWithPostConstruct.class);
        repo.get(ServiceWithPostConstruct.class);
        // see System.out: three outputs has to appear for this test
    }
}
