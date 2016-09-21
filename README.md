BeanRepository - Some Kind of Dependency Injection
==================================================

In an other Project I want to work with Injections of Singletons,
 Instances and so on. But because it has to run in a Sandbox (without
 Codesigning) no Reflection was allowed. Spring uses in most cases
 Reflection and Google Guice accessed some not allowed System Properties.

So, this was the PERFECT time to create my own (very) limited Dependency
 Injection (or Obtaining?) Framework :-)

Since then, I work on this Project, even if the origin Projects needs no
 more Features.

See [ExampleTest.java](src/test/java/de/tse/beanrepository/example/ExampleTest.java)
 for Examples.


# Origin Requirements #

* Simple Configuration in Java Code
* No use of Reflection or Annotation Magic
* Support for Singletons and Instances
* Fail Fast: Detection of cyclic References on start up


# Additional Features #

* Support for Prototypes
* Detect Beans of a specific Type
* Modularity


# Limitations #

* No resolving of cyclic References -> leads to StackOverflow on start up (fail fast)
* Only lazy Creation of Beans. Beans are created, when they are needed (in
   dependence of the scope)
* No Request or Session Scope
* Only Constructor Injection possible
    * Every Bean needs a Default Constructor or a Constructor with
       the [BeanAccessor](src/main/java/de/tse/beanrepository/BeanAccessor.java) as Parameter
    * Every Bean has to store its Dependencies in its own Constructor
       or in its onPostConstruct() Method
    * That a Bean has to get its own Dependencies from a Repository
       may collide with the Definition of 'Dependency Injection', but
       it is close enough for the case: simply get References to other Beans
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of BeanFactory
    * use [PostConstructible](src/main/java/de/tse/beanrepository/PostConstructible.java)
       for initialisation Code
* A Bean can only be accessed by a Class
