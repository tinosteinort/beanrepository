BeanRepository - Some Kind of Dependency Injection
==================================================

In an other Project I want to work with Injections of Singletons,
 Prototypes and so on. But because it has to run in a Sandbox (without
 Codesigning) no Reflection was allowed. Spring uses in most cases
 Reflection and Google Guice accessed some not allowed System Properties.

So, this was the time to create my own (very) limited Dependency
 Injection (or Obtaining?) Framework :-)

See [ExampleTest.java](src/test/java/de/tse/beanrepository/example/ExampleTest.java)
 for Examples.

# Requirements / Features #

* Simple Configuration in Java Code
* No use of Reflection
* Support for Singletons, Prototypes and Instances
* Early Detection of cyclic References -> on Creation of BeanRepository

# Limitations #

* No resolving of cyclic References -> leads to StackOverflow on start up
* Only lazy Creation of Beans. Beans are created, when they are needed (in
   dependence of the scope)
* Only Constructor Injection possible
    * Every Bean needs a Default Constructor or a Constructor with
       the BeanAccessor as Parameter
    * Every Bean has to store its Dependencies in its own Constructor
       or in its onPostConstruct() Method
    * That a Bean has to get its own Dependencies from a Repository
       may collide with the Definition of 'Dependency Injection', but
       it is close enough for the case: simply get References to other Beans
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of BeanFactory
    * use [PostConstructible](src/main/java/de/tse/beanrepository/PostConstructible.java)
       for initialisation Code
* A Bean is only accessible by the Class in this two ways:
    * `repository.getBean(Class<T> cls)`
    * `repository.getBeansOfType(Class<T> cls)`
* No Modularity
* No Request or Session Scope