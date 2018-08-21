BeanRepository - Dependency Injection / Service Locator
=======================================================

This framework is the implementation of a mix of the Service Locator Pattern and a
 the Dependency Injection Pattern. These patterns are described by Martin Fowler in
 [this article](http://martinfowler.com/articles/injection.html). The `BeanRepository`
 does not use reflection for injecting beans. Because of that fact, it can be used in
 the Java sandbox, where reflection is not allowed.


## Features ##

* simple, self-explanatory and failsafe configuration in Java code
* no use of reflection or annotations
* constructor injection
* support for singletons, prototypes and instances
* provider
* factories
* aliases for beans
* fail fast on start up
* execute code after initialisation of the bean (post construct)
* configurable if singletons are lazy initialised or not
* detect beans of a specific type
* modularity possible


## Limitations ##

* cyclic references not supported directly. But if needed, see
   [CyclicReferenceExampleApp](/src/test/java/com/github/tinosteinort/beanrepository/example/_03_cyclicreferenceexample)
   for a solution
* no request or session scope
* no initialisation code allowed in constructor
    * constructor may be called multiple times while working with the `BeanRepository`
* a bean can only be accessed by a class


## Maven ##

Include the following artifact to use the `BeanRepository`:
```xml
<dependency>
    <groupId>com.github.tinosteinort</groupId>
    <artifactId>beanrepository</artifactId>
    <version>1.7.0</version>
</dependency>
```

## Examples

See [SimpleExampleApp](/src/test/java/com/github/tinosteinort/beanrepository/example/_01_basicexample) for ar very
 basic example.

See [EventExampleApp](/src/test/java/com/github/tinosteinort/beanrepository/example/_02_eventexample) for an example
 with events and listeners.

See [CyclicReferenceExampleApp](/src/test/java/com/github/tinosteinort/beanrepository/example/_03_cyclicreferenceexample)
 for an example with cyclic references.




# Version History #

## v1.7.0 ##
Enhancements:
* Introduce `BeanRepositoryApplication`
* Add examples

Fixes:
* [Issue#4](https://github.com/tinosteinort/beanrepository/issues/4):
  onPostConstruct of singleton beans is executed if the bean is requested, not when the BeanRepository is build
* [Issue#5](https://github.com/tinosteinort/beanrepository/issues/5):
  onPostConstruct of a Factory Bean is not triggered

## v1.6.0 ##
Enhancements:
* Aliases for beans

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
