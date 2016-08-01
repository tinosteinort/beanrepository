BeanRepository - Some Kind of Dependency Injection
==================================================

In an other Project I want to work with Injections of Singletons,
 Prototypes and so on. But because it has to run in a Sandbox (without
 Codesigning) no Reflection was allowed. Spring uses in most cases
 Reflection and Google Guice accessed some not allowed System Properties.

So, this was the time to create my own (very) limited Dependency
 Injection Framework :-)

See [ExampleTest.java](src/test/java/de/tse/beanrepository/example/ExampleTest.java)
 for Examples.

# Requirements / Features #

* Simple Configuration in Java Code
* No use of Reflection
* Singleton Support
* Prototype Support
* Instance Support
* Early Detection of cyclic References -> on Creation of BeanRepository

# Limitations #

* No resolving of cyclic References -> leads to StackOverflow on start up
* Only eager Creation of Beans
* Only Constructor Injection possible
    * Every Bean needs a Default Constructor or a Constructor with
       the BeanAccessor as Parameter
    * Every Bean has to store its Dependencies in its own Constructor
       or in its onPostConstruct() Method
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of BeanFactory
    * use [PostConstructible](src/main/java/de/tse/beanrepository/PostConstructible.java)
       for initialisation Code
* A Bean is only accessible by the Class: `repository.get(Class<T> cls)`
* Creation of Singleton Beans is not threadsafe