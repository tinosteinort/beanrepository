package com.github.tinosteinort.beanrepository;

import com.github.tinosteinort.beanrepository.example.services.MailService;
import com.github.tinosteinort.beanrepository.example.services.MyInterface;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl1;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl2;
import com.github.tinosteinort.beanrepository.example.services.MyInterfaceImpl3;
import com.github.tinosteinort.beanrepository.example.services.PrintService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ModularizationTest {

    @Test public void collectBeansIncludingParentModule() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("LogicModule")
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository child1 = new BeanRepository.BeanRepositoryBuilder("DataModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final BeanRepository child2 = new BeanRepository.BeanRepositoryBuilder("DataModule", child1)
                .singleton(MyInterfaceImpl3.class, MyInterfaceImpl3::new)
                .build();

        final Set<MyInterface> beans = child2.getBeansOfType(MyInterface.class);
        Assert.assertEquals(3, beans.size());
    }

    @Test public void collectBeansIncludingParentButNotOtherModules() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("LogicModule")
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder("OneModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder("AnotherModule", parent)
                .singleton(MyInterfaceImpl3.class, MyInterfaceImpl3::new)
                .build();

        final Set<MyInterface> beansOfModule1 = module1.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule1.size());

        final Set<MyInterface> beansOfModule2 = module2.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule2.size());
    }

    @Test public void useSameReferenceToBeanFromParent_noCopy() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("LogicModule")
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder("OneModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder("AnotherModule", parent)
                .singleton(MyInterfaceImpl3.class, MyInterfaceImpl3::new)
                .build();

        final Set<MyInterface> beansOfModule1 = module1.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule1.size());

        final Set<MyInterface> beansOfModule2 = module2.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule2.size());

        Assert.assertEquals(module1.getBean(MyInterfaceImpl1.class), module2.getBean(MyInterfaceImpl1.class));
    }

    @Test public void allowSameBeanInDifferentModules() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("LogicModule")
                .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
                .build();

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder("OneModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder("AnotherModule", parent)
                .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
                .build();

        final Set<MyInterface> beansOfModule1 = module1.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule1.size());

        final Set<MyInterface> beansOfModule2 = module2.getBeansOfType(MyInterface.class);
        Assert.assertEquals(2, beansOfModule2.size());

        Assert.assertNotEquals(module1.getBean(MyInterfaceImpl2.class), module2.getBean(MyInterfaceImpl2.class));
    }

    @Test public void useBeanAsDependencyFromOtherModule() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("BaseModule")
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository child = new BeanRepository.BeanRepositoryBuilder("AdvancedModule", parent)
                .singleton(MailService.class, MailService::new)
                .build();

        final MailService mailService = child.getBean(MailService.class);
        mailService.sendMail("You", "Hi again!");
    }

    @Test(expected = RuntimeException.class)
    public void dependentBeansNotAllowedInDifferentModules() {

        final BeanRepository module1 = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .build();

        final BeanRepository module2 = new BeanRepository.BeanRepositoryBuilder()
                .singleton(MailService.class, MailService::new)
                .build(); // requires PrintService -> not available -> Error
    }

    @Test public void beanAlreadyRegisteredInParentModule() {

        final BeanRepository baseModule = new BeanRepository.BeanRepositoryBuilder("BaseModule")
                .singleton(PrintService.class, PrintService::new)
                .build();

        try {
            final BeanRepository childModule = new BeanRepository.BeanRepositoryBuilder("ChildModule", baseModule)
                    .singleton(PrintService.class, PrintService::new)
                    .build();
        }
        catch (IllegalArgumentException ex) {
            Assert.assertEquals(
                    "Error while register Bean: [com.github.tinosteinort.beanrepository.example.services.PrintService@ChildModule] "
                            + "already exist in Repository [BaseModule]."
                    , ex.getMessage());
        }
    }

    @Test public void dryRunOfChildBeanMustNotExecuteOnPostConstructOfParentBean() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("Parent")
                .singleton(ParentBean1.class, ParentBean1::new)
                .enableLazySingletonBeans(true)
                .build();

        final BeanRepository child = new BeanRepository.BeanRepositoryBuilder("Child", parent)
                .singleton(ChildBean1.class, ChildBean1::new, ParentBean1.class)
                .enableLazySingletonBeans(true)
                .build();

        Assert.assertEquals(0, ParentBean1.onPostConstructCounter);

        child.getBean(ChildBean1.class);
        Assert.assertEquals(1, ParentBean1.onPostConstructCounter);
    }

    @Test public void dryRunOfChildBeanMustNotExecuteOnPostConstructOfParentBeanTwice() {

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("Parent")
                .singleton(ParentBean2.class, ParentBean2::new)
                .enableLazySingletonBeans(true)
                .build();

        Assert.assertEquals(0, ParentBean2.onPostConstructCounter);
        parent.getBean(ParentBean2.class);
        Assert.assertEquals(1, ParentBean2.onPostConstructCounter);

        final BeanRepository child = new BeanRepository.BeanRepositoryBuilder("Child", parent)
                .singleton(ChildBean2.class, ChildBean2::new, ParentBean2.class)
                .build();


        child.getBean(ChildBean2.class);
        Assert.assertEquals(1, ParentBean2.onPostConstructCounter);
    }

    @Test public void dryRunOfChildBeanMustNotExecuteOnPostConstructOfParentPrototypeBean() {

        PrototypeParentBean.onPostConstructCounter = 0;

        final BeanRepository parent = new BeanRepository.BeanRepositoryBuilder("Parent")
                .prototype(PrototypeParentBean.class, PrototypeParentBean::new)
                .build();

        Assert.assertEquals(0, PrototypeParentBean.onPostConstructCounter);
        parent.getBean(PrototypeParentBean.class);
        Assert.assertEquals(1, PrototypeParentBean.onPostConstructCounter);

        final BeanRepository child = new BeanRepository.BeanRepositoryBuilder("Child", parent)
                .singleton(PrototypeChildBean.class, PrototypeChildBean::new, PrototypeParentBean.class)
                .enableLazySingletonBeans(true)
                .build();

        Assert.assertEquals(1, PrototypeParentBean.onPostConstructCounter);
        child.getBean(PrototypeChildBean.class);
        Assert.assertEquals(2, PrototypeParentBean.onPostConstructCounter);
    }
}

class ParentBean1 implements PostConstructible {

    public static int onPostConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        onPostConstructCounter++;
    }
}

class ChildBean1 {

    private final ParentBean1 parentBean;

    public ChildBean1(final ParentBean1 parentBean) {
        this.parentBean = parentBean;
    }
}

class ParentBean2 implements PostConstructible {

    public static int onPostConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        onPostConstructCounter++;
    }
}

class ChildBean2 {

    private final ParentBean2 parentBean;

    public ChildBean2(final ParentBean2 parentBean) {
        this.parentBean = parentBean;
    }
}

class PrototypeParentBean implements PostConstructible {

    public static int onPostConstructCounter = 0;

    @Override public void onPostConstruct(final BeanRepository repository) {
        onPostConstructCounter++;
    }
}

class PrototypeChildBean {

    private final PrototypeParentBean parentBean;

    public PrototypeChildBean(final PrototypeParentBean parentBean) {
        this.parentBean = parentBean;
    }
}
