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
* Support for Singletons, Prototypes (also with Parameters) and Instances
* Provider
* Factories
* Aliases for beans
* Fail Fast on start up
* Execute Code after Initialisation of the Bean
* Configurable if singletons are lazy initialised or not
* Detect Beans of a specific Type
* Modularity


## Limitations ##

* Cyclic References not allowed
* No Request or Session Scope
* No initialisation Code allowed in Constructor
    * Constructor may be called multiple times while Creation of the `BeanRepository`
* A Bean can only be accessed by a Class


## Maven ##

Include the following Artifact to use the `BeanRepository`:
```xml
<dependency>
    <groupId>com.github.tinosteinort</groupId>
    <artifactId>beanrepository</artifactId>
    <version>1.7.0</version>
</dependency>
```

## Examples

See [SimpleExampleApp](src\test\java\com\github\tinosteinort\beanrepository\example\basicexample) for ar very basic example
See [EventExampleApp](src\test\java\com\github\tinosteinort\beanrepository\example\eventexample) for an example with listeners


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
