# SimpleWebService

[![Build Status](https://travis-ci.org/PabloNicolasDiaz/SimpleWebService.svg?branch=master)](https://travis-ci.org/PabloNicolasDiaz/SimpleWebService)
[![DepShield Badge](https://depshield.sonatype.org/badges/PabloNicolasDiaz/SimpleWebService/depshield.svg)](https://depshield.github.io)

Simple Web Service Example using ~~Maven~~ Gradle, Java ~~8~~ 10, Spring Boot and JAX-WS

## Overview

This repository contains an example project for building contract-first, SOAP based web services using Maven, Java 8, Spring Boot and Junit 5. 

## Demostrated Concepts

* Gradle (wrapped) as build tool 
* Travis as CI Service
* Java code generation from WSDL using ~~Maven Plugins~~ Gradle ANT Execution
* Enabling JAXB2 plugins in code generation
* Integrating JUnit 5 testing framework with Spring Test
* Unit tests for endpoints classes
* Integration test, to ensure correct WSDL and XSD publishing
* Karate Functional tests, to ensure service functionality
* Tests executions generates Jacoco reports, enabling both Unit and Integration tests coverage 

## Setup and Running

To run integration tests
```

./gradlew check

```

## References

TBD

  
