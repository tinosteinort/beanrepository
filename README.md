BeanRepository - Dependency Injection / Service Locator Mix
===========================================================

This Framework is the Implementation of a mix of the Service Locator Pattern and a
 the Dependency Injection Pattern. These Patterns are described by Martin Fowler in
 [this Article](http://martinfowler.com/articles/injection.html). The `BeanRepository`
 does not use Reflection for injecting Beans. Because of that Fact, it can be used in
 the Java Sandbox, where Reflection is not allowed.


## Features ##

* Simple, self-explanatory and failsafe Configuration in Java Code
* No use of Reflection or Annotations
* Constructor Injection
* Support for Singletons, Prototypes (even with Parameters) and Instances
* Provider
* Factories
* Aliases for beans
* Fail Fast on start up
* Execute Code after Initialisation of the Bean
* Detect Beans of a specific Type
* Modularity


## Limitations ##

* Cyclic References not allowed
* Only lazy Creation of Beans. Beans are created, when they are needed (in
   dependence of the scope)
* No Request or Session Scope
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of the `BeanRepository`
* A Bean can only be accessed by a Class


# User Guide #

This Part lists examples, of how the BeanRepository has to be used.

## Maven ##

Include the following Artifact to use the `BeanRepository`:
```xml
<dependency>
    <groupId>com.github.tinosteinort</groupId>
    <artifactId>beanrepository</artifactId>
    <version>1.5.1</version>
</dependency>
```

## Get Dependencies: Dependency Injection Style ##

At the Momemnt, 'real' Depenency Injection only works with up to 5 Dependencies.

Example Services:
```java
    public class PrintService {

        public void print(final String value) {
            System.out.println(value);
        }
    }

    public class MailService {

        private final PrintService printService;

        public MailService(final PrintService printService) {
            this.printService = printService;
        }

        public void sendMail(final String to, final String text) {
            printService.print("Message for " + to + ": " + text);
        }
    }
```

Setup of the `BeanRepository` to get full initialized Beans with Dependency by Constructor
 Injection:
```java
    public static void main(String[] args) {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singleton(MailService.class, MailService::new, PrintService.class)
                .build();

        final MailService = repo.getBean(MailService.class);
    }
```
The Classes of the referenced Beans has to be listed behind the Constructor Reference.

## Get Dependencies: Service Locator Style ##

As an alternative to the Dependency Injection, the Beans can be determined manually. The
 [BeanAccessor](src/main/java/com/github/tinosteinort/beanrepository/BeanAccessor.java) has
 to be used in the Constructor of the Bean. There is no Limitation with the amount of the
 needed Beans.

Services:
```java
    public class PrintService {

        public void print(final String value) {
            System.out.println(value);
        }
    }

    public class MailService {

        private final PrintService printService;

        public MailService(final BeanAccessor beans) { // Using of BeanAccessor
            this.printService = beans.getBean(PrintService.class);
        }

        public void sendMail(final String to, final String text) {
            printService.print("Message for " + to + ": " + text);
        }
    }
```

Setup of the BeanRepository to get full initialized Beans with the `BeanAccessor`:
```java
    public static void main(String[] args) {

        final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
                .singleton(PrintService.class, PrintService::new)
                .singleton(MailService.class, MailService::new) // Type of referenced Bean is not required
                .build();
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
            .singleton(MailService.class, MailService::new, PrintService.class)
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

    final ServiceWithParameter service = repo.getPrototypeBean(ServiceWithParameter:new, param);
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
            repo.getPrototypeBean(ServiceWithBeanDependenciesAndParameter::new, param);

    service.print("Dependency is used to print Text");
```

## Provider ##

A `Provider` delivers a Bean. The Bean is initialised when the `get` Method of the `Provider` is called.
 It is possible to get a `Provider` for each registered Bean.
```java
    Provider<BeanClass> provider = repo.getProvider(BeanClass.class); // does not execute PostConstructible.onPostConstruct
    BeanClass bean = provider.get(); // executes PostConstructible.onPostConstruct
```

## Factory ##

A Factory is a Class which creates an Instance of a Bean. Within the `BeanRepository` a Factory itself is
 not a Bean, but has access to the `BeanAccessor`. Even if the Factory Class implements `PostConstructible`,
 the Method `onPostConstruct` is not executed for the Factory, but for the created Bean, if the Bean Class
 implements `PostConstructible`. It is possible to create Factories for singleton and prototype Beans. A
 Factory has to implement the `Factory` Interface.

```java
    BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .prototypeFactory(PrintService.class, PrintServiceFactory::new) // Factory is called, every Time when PrintService is requested
            .singletonFactory(MailService.class, MailServiceFactory::new)   // Factory is called only once
            .build();
```

## Aliases for Beans ##

It is possible to define an alias for a bean. A bean with an alias can be determined by
 querying for the registered bean id OR by the defined alias class.

```java
    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .singleton(PrintService.class, PrintService::new)
            .alias(WriterService.class, PrintService.class)
            .build();

    final WriterService service = repo.getBean(WriterService.class);
    service.print("Written by alias");
```
In this example the `PrintService` implements the `WriterService` interface.

## Modularisation ##

One `BeanRepository` equates to one Module. It is possible to create a Graph of BeanRepositories. While creating
 a `BeanRepository`, a parent `BeanRepository` can be passed. A Child Repository can get References to Beans from
 the Parent, but not from an other Child of the Parent Repository.

Important: Every `BeanRepository` has to be valid on its own.

So a Tree can be build out of BeanRepositories. If two BeanRepositories has no Correlation, the Beans
 of the Repositories has no Correlation, too.
```java
    final BeanRepository baseRepo = new BeanRepository.BeanRepositoryBuilder("Base")
            .singleton(MyInterfaceImpl1.class, MyInterfaceImpl1::new)
            .build();

    final BeanRepository dataRepo = new BeanRepository.BeanRepositoryBuilder("Data", baseRepo)
            .singleton(MyInterfaceImpl2.class, MyInterfaceImpl2::new)
            .build();

    final BeanRepository otherRepo = new BeanRepository.BeanRepositoryBuilder("Other", baseRepo)
            .singleton(MyInterfaceImpl3.class, MyInterfaceImpl3::new)
            .build(logicRepo, dataRepo);


    final Set<MyInterface> beans1 = dataRepo.getBeansOfType(MyInterface.class); // contains MyInterfaceImpl1 and MyInterfaceImpl2
    final Set<MyInterface> beans2 = otherRepo.getBeansOfType(MyInterface.class); // contains MyInterfaceImpl1 and MyInterfaceImpl3
```

### External Bean Definition ###

An other Way of Mudularisation is to define `BeanDefinition` in different Modules of the Application, collect
 them and summarise into one BeanRepository.

```java
    final BeanDefinition<PrintService> definition1 =
            BeanDefinition.create(Scope.SINGLETON, PrintService.class, PrintService::new);

    final BeanDefinition<ServiceWithConstructorDependency> definition2 =
            BeanDefinition.create(Scope.SINGLETON, MailService.class, MailService::new, PrintService.class);

    final BeanRepository repo = new BeanRepository.BeanRepositoryBuilder()
            .definition(definition1)
            .definition(definition2)
            .build();
```

# Version History #

## v1.5.1 ##
Fixes:
* [Issue#3](https://github.com/tinosteinort/beanrepository/issues/3):
  StackOverflowError when using PostConstructible in Combination with ConstructorInjection

## v1.5.0 ##
Fixes:
* [Issue#2](https://github.com/tinosteinort/beanrepository/issues/2):
  getProvidersForSingletons() does not consider Factories

Enhancements:
* [Issue#1](https://github.com/tinosteinort/beanrepository/issues/1):
  Method should be private, not for external use
* Support simple Access to Prototype Beans with Parameter (with and without Dependencies
  to other Beans)

## v1.4.0 ##
Fixes:
* Modularisation does not work as expected. Concept refactored.

Enhancements:
* Constructor Injection

## v1.3.1 ##
Fixes:
* `Factory.createInstance()` is not called while dryRun. It is executed once for singleton Beans,
    and every time, when a prototype Bean is requested

## v1.3.0 ##
Enhancements:
* A `Factory` can be used to create Beans

## v1.2.0 ##
Enhancements:
* Add `Provider` Interface, and Methods to get Providers for registered Beans

## v1.1 ##
Fixes:
* `onPostConstruct` was called multiple Times on referenced Beans

Enhancements:
* Add other `getBean(...)` Methods to `BeanAccessor`. This allows to generate `prototype` Beans in
   a Constructor

## v1.0 ##
Initial Version
