BeanRepository - a Bean Obtaining Framework
===========================================

In an other Project I want to work with Injections of Singletons,
 Instances and so on. But because it has to run in a Sandbox (without
 Codesigning) no Reflection was allowed. Spring uses in most cases
 Reflection and Google Guice accessed some not allowed System Properties.

So, this was the PERFECT time to create my own (very) limited Dependency
 Injection/Obtaining Framework :-)

Since then, I work on this Project, even if the origin Projects needs no
 more Features.

See [ExampleTest](src/test/java/com/github/tinosteinort/beanrepository/example/ExampleTest.java)
 for Examples.


# Some Kind of Dependency Injection #

With this Framework, every Bean has to get its own Dependencies from a Repository. This
 is the Reason why 'Dependency Injection' may not the correct Description. But the goal
 is equal: simply manage Dependencies of a Bean, without worrying about the Bean
 Lifecycle.


## Origin Requirements ##

* Simple, self-explanatory and failsafe Configuration in Java Code
* No use of Reflection or Annotation Magic
* Support for Singletons and Instances
* Fail Fast: Detection of cyclic References on start up
* Execute Code after Initialisation of the Bean


## Additional Features ##

* Support for Prototypes (even with Parameters)
* Detect Beans of a specific Type: `Set<T> getBeansOfType(final Class<T> cls)`
* Modularity


## Limitations ##

* No resolving of cyclic References -> leads to StackOverflow on start up (fail fast)
* Only lazy Creation of Beans. Beans are created, when they are needed (in
   dependence of the scope)
* No Request or Session Scope
* Beans has to be obtained in the Constructor or in its `onPostConstruct()` Method
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of the `BeanRepository`
    * Not all Beans may be available at this Time
    * use [PostConstructible](src/main/java/de/tse/beanrepository/PostConstructible.java)
       for initialisation Code
* A Bean can only be accessed by a Class


## Other Frameworks ##

After some more research: there are plenty of Dependency Injection Frameworks:
* [JayWire](https://github.com/vanillasource/jaywire)
* [net.nanojl:injector](https://nanojl.net/injector/)
* [Feather](https://github.com/zsoltherpai/feather)
* [Silk](https://github.com/jbee/silk)
* and many more...

But there is no Reason for a Developer, to don't write an own Dependency Framework. Just for Fun :-)


# User Guide #

This Part lists examples, of how the BeanRepository has to be used.

## Maven ##

Include the following Artifact to use the `BeanRepository`:
```xml
<dependency>
    <groupId>com.github.tinosteinort</groupId>
    <artifactId>beanrepository</artifactId>
    <version>1.0</version>
</dependency>
```

## Get needed Beans in Constructor ##

Every Bean that has a Dependency, needs the
 [BeanAccessor](src/main/java/com/github/tinosteinort/beanrepository/BeanAccessor.java)
 as Parameter, so that the Bean can obtains its own Dependencies.
```java
    public class PrintService { // no Dependencies -> no BeanAccessor needed

        public void print(final String value) {
            System.out.println(value);
        }
    }

    public class MailService {

        private final PrintService printService; // Dependency! -> BeanAccessor needed

        public MailService(final BeanAccessor beans) {
            this.printService = beans.getBean(PrintService.class);
        }

        public void sendMail(final String to, final String text) {
            printService.print("Message for " + to + ": " + text);
        }
    }
```

## Initialisation in onPostConstruct() to get needed Beans ##

In the Constructor it is not allowed (and not possible) to to call `getBeansOfType(Class<T> cls)`, because at this
 Time, not every Bean may be registered. To get all Beans of a Type, use the `onPostConstruct()` Method out of the
 [PostConstructible](src/main/java/de/tse/beanrepository/PostConstructible.java) Interface. This Method is called
 once for each Singleton Bean, and every Time if a Prototype Bean is requested. For Instance Beans this Method is not
 called.
```java
    public interface MyInterface {

        void doSomething(String value);
    }

    public class MyInterfaceImpl1 implements MyInterface {

        @Override public void doSomething(String value) {

        }
    }

    public class MyInterfaceImpl2 implements MyInterface {

        @Override public void doSomething(String value) {

        }
    }

    public class CollectorServiceOnPostConstruct implements PostConstructible {

        private final Set<MyInterface> implementations = new HashSet<>();

        @Override public void onPostConstruct(final BeanRepository repository) {
            implementations.addAll(repository.getBeansOfType(MyInterface.class));
        }
    }
```

## Bootstrap BeanRepository ##

To create a `BeanRepository`, the `BeanRepositoryBuilder` has to be used. The Scope of the Bean is defined
 by the Method, called on the `BeanRepositoryBuilder`.
```java
    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .singleton(MailService.class, MailService::new)
            .singleton(PrintService.class, PrintService::new)
            .build();

    final MailService mailService = repo.getBean(MailService.class);
    mailService.sendMail("Donnie", "Hi!");
```

## Singleton Scope ##

To see how to create a Singleton with the `BeanRepository`, see the Example above. It is guaranteed, that
 only one (and the same) Instance of the Class is provided by the `BeanRepository`.

## Instance Scope ##

It is possible to register Instances in the `BeanRepository`. The Bean can be accessed by its Type.
```java
    final String[] values = new String[] { "a", "b", "c" } ;

    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .instance(values)
            .build();

    for (String value : repo.getBean(String[].class)) {
        System.out.println(value);
    }
```

## Prototype Scope ##

A Prototype Bean is created every Time it is requested. In this case `service1` and `service2` are
 different Beans.
```java
    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .prototype(PrintService.class, PrintService::new)
            .build();

    final PrintService service1 = repo.getBean(PrintService.class);
    final PrintService service2 = repo.getBean(PrintService.class);
```

## Prototype Beans with Parameter ##

It is possible that some Prototype Beans needs Parameter which are available only at Runtime:
```java
    public class ServiceWithParameter {

        private final String id;

        public ServiceWithParameter(final String id) {
            this.id = id;
        }

        public void print(final String value) {
            System.out.println(id + ": " + value);
        }
    }
```
A Prototype Bean with Parameter can not registered in the `BeanRepository`, but it is accessible
 by the `BeanRepository`:
```java
    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .build();

    final String param = "parameterGeneratedAtRuntime";

    final ServiceWithParameter service = repo.getBean(() -> new ServiceWithParameter(param));
    service.print("123");
```
It is also possible, to get other Beans from within a Prototype Bean with Parameter. Example:
```java
    public class ServiceWithBeanDependenciesAndParameter {

        private final PrintService printService;
        private final String id;

        public ServiceWithBeanDependenciesAndParameter(final BeanAccessor beans, final String id) {
            this.printService = beans.getBean(PrintService.class);
            this.id = id;
        }

        public void print(final String value) {
            printService.print(id + ": " + value);
        }
    }
```
Accessing a Prototype Bean with Dependencies and Parameter:
```java
    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .singleton(PrintService.class, PrintService::new)
            .build();

    // e.g. a calculated Value, not available at Bootstrap of BeanRepository
    final String param = "parameterGeneratedAtRuntime";

    final ServiceWithBeanDependenciesAndParameter service =
            repo.getBean((BeanAccessor beans) -> new ServiceWithBeanDependenciesAndParameter(beans, param));

    service.print("Dependency is used to print Text");
```

# Modularisation #

It is possible to create multiple BeanRepositories which can be wired together. One `BeanRepository` equates
 to one Module. Every `BeanRepository` can get a name for better Recognition in Case of an Error.

Important: Every `BeanRepository` has to be valid on its own.

So a Tree can be build out of BeanRepositories. If two BeanRepositories has no Correlation, the Beans
 of the Repositories has no Correlation, too.
```java
    final BeanRepository logicRepo = new BeanRepository.BeanRepositoryBuilder("LogicModule")
            .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
            .build();

    final BeanRepository dataRepo = new BeanRepository.BeanRepositoryBuilder("DataModule")
            .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
            .build();

    // #MARKER#

    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder("CompositeModule")
            .build(logicRepo, dataRepo);

    // At this Point all Modules are wired together

    final Set<MyInterface> beans = repo.getBeansOfType(MyInterface.class);
```

If working with Modules, dont call `getBean()` before all Modules are wired together
 (e.g. at `#MARKER#`). This may lead to missing Beans, because `onPostConstruct()` is executed,
 before all Beans are available. This may lead to an unexpected State.
