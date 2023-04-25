Emergency Roadside Assistance Service
===========================
This project is created as an assignment on below requirement

Implement a service that helps Geico customers with a disabled vehicle, to get immediate roadside assistance, by connecting
them directly with emergency assistance service trucks that are available and operating at nearby locations.

Functionality

- Update location of a service provider

- Return the 5 nearest service trucks ordered by ascending distance

- Reserve a service provider for a customer

- Release a service provider from a customer
This is demo
This is a sample project for a Roadside Assistance Service. It is built using Java and Spring Boot.

Requirements
------------

To run this project, you need to have the following installed:
-   Java 11
-   Any IDEA like intellij if you want to run and debug and review the code otherwise it is not required

Getting Started
---------------
To build and run the project with Gradle, follow these steps:

1. Go to the project root directory using the command line.

2.  Run the following command to build the project:

    bashCopy code

    `./gradlew build`

    This will compile the code, run the tests, and generate a JAR file in the `build/libs` directory.

3.  Run the following command to start the application:

    bashCopy code

    `java -jar build/libs/geico-assist-1.0-SNAPSHOT.jar`

For Docker Lovers 
-------------
Docker file is added, just in case if someone wants to use it. However, docker build is not tested, as I did not have the docker installed on my personal laptop Would recommend to run and build it with above-mentioned approach

How to Test the Application
-------------
Functional tests and unit tests are added.

To run application in action, running following tests are recommended. Below is the reference path for e2e/functional test. 
Tests are pretty verbose, in addition to that there are enough  comments and present on test class to understand the flow and assertions
- Here is path from source root
com/geico/claim/roadsideassistance/e2e/RoadsideAssistanceServiceSmokeTest.java

ADR
-------
Some of the decisions and consideration made are documented in adr, which you can find here
src/adr.md
License
-------
Open Source,  Publicly  GNU General Public License (GPL) 