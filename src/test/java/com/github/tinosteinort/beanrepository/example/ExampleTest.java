package com.github.tinosteinort.beanrepository.example;

import com.github.tinosteinort.beanrepository.BeanDefinition;
import com.github.tinosteinort.beanrepository.BeanRepository;
import com.github.tinosteinort.beanrepository.Scope;
import com.github.tinosteinort.beanrepository.example.services.MailService;
import com.github.tinosteinort.beanrepository.example.services.MyInterface;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl1;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl2;
import com.github.tinosteinort.beanrepository.example.services.PrintService;
import com.github.tinosteinort.beanrepository.example.services.ServiceWithConstructorDependency;
import org.junit.Test;

/**
 * No valid Tests, but it shows the Functionality of the BeanRepository.
 */
public class ExampleTest {

    @Test(expected = IllegalArgumentException.class)
    public void registerBeansWithSameClassMultipleTimes() {

        new BeanRepository.BeanRepositoryBuilder()
                .singleton(MyInterface.class, MyInterfaceImpl1::new)
                .singleton(MyInterface.class, MyInterfaceImpl2::new)
                .build();
    }

    @Test public void singletonBeanWithPrototypeBeanInConstructor() {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .prototype(PrintService.class, PrintService::new) // Should be initialised only once
                .build();

        final MailService mailService = repo.getBean(MailService.class);
        mailService.sendMail("You", "Hi!");
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
}
