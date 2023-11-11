# Restaurant Choice Service

Restaurant Choice Service provides you the capability of choosing your next dinning spot !

This application consists of a spring boot based backend, and a simple old-fashioned HTML frontend which serves the 
capability of providing user inputs.

Application mainly communicate on HTTP based REST and WebSockets. 

## Local Set Up Guide
< Assumption that you have pre-installed following tools in your local machine>
- Maven 3
- Java 17
- Git
- Intelij IDEA

#### Cloning the codebase
``git clone git@github.com:maduhshan/restaurant-choice-service.git``

By default code will checkout at master branch. For further development activities 
please use your own feature branches. Feel free to raise a pull request for new features and add-ons.

#### Import the code to Intelij IDEA

Open Intelij and import the code base using the root folder or the pom.xml
``File > Open``

### Source Code
This code is developed by adhering to layered, Below are the important packages to be look out for.

#### src/main/java :
- ``gov.sg.tech.controller`` - Represents the presentation layer which consists controllers to server REST
and WebSocket communication.
  
- ``gov.sg.tech.service`` - Consists the business logic of the application and works and mediator to return
client understandable objects to controllers

- ``gov.sg.tech.dao.repository`` - Classes in this layer gives the capability of data storage.
We are using spring-data supported JPARepositories and their in built capabilities for most of the
  storage logics.

- ``gov.sg.tech.controller.transformer`` - Transforms the objects to client understandable domain models and vice versa 
  (Transform to entity types to data persistence layer understandable objects)

- ``gov.sg.tech.entity`` - Contains ORM classes, which maps to database entities

- ``gov.sg.tech.domain`` - Contains domain models that are being used to communicate with clients 
  (These models would be the contract between client and server)
  
### Data storage

We are using Liquibase schema migration tool It provides a platform-independent way to manage and version database schema changes.

``/resources/db/changelog/`` Contains the change log files to be added. 
It's important to make sure the proper sequence is followed when adding new script files.

``db/changelog/db.changelog-master.xml`` The idea is to break down database changes into logical units and manage them separately.
When ever you add a new database change file, it needs to be added in to this file like below,
``    <include file="classpath:db/changelog/002-2023-11-10-create-session_user-table.sql" relativeToChangelogFile="false"/>
``
## Build

``mvn clean install``

## Run 
Application can be run using the Application.java main class.