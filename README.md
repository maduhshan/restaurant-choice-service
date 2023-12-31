# Restaurant Choice Service

Restaurant Choice Service provides you the capability of choosing your next dinning spot !

This application consists of a spring boot based backend, and a simple old-fashioned HTML frontend which serves the 
capability of providing user inputs.

The application mainly communicate on HTTP based REST and WebSockets for interactive communication. 

### Design on high level
Application exposes two communication modes.

- REST

  - ```/sessions``` - Create a Session with a session owner(Session owner needs to registered first). Once a session is created session id needs to shared to other users so that they can join using the session id
  - ```/sessions/{id}``` - Get a session from it's id
  - ```/users``` - Register an user

REST endpoints are used for on demand operations like register users and create sessions. Where, rest of the clients do not need to be notified up on an operation completes in real-time

- WEBSOCKET

  - ``/sessions/{sessionId}/join`` - Mapping path to join a session
    This topic is used for new users who want to join an ongoing session using sessionID

    ```/topic/sessions/manage/{sessionId}``` - Destination which publishes joined session response
    to notify other users that new user has joined
  - ``/sessions/{sessionId}/manage`` - Mapping path to end/manage session. Session Owner/ First User has the
    capability to close the session and populate the random restaurant.

    ``/topic/sessions/manage/{sessionId}`` - Destination to publish end session response to notify other users that session has ended and final selected restaurant.

  - ```/sessions/{sessionId}/restaurantChoice``` - Mapping path to submit a restaurant choice. All joined
    users can submit their personal preference by adding their choice of restaurant.

    ``/topic/sessions/manage/{sessionId}`` - Destination to publish the submitted restaurant choice response to notify other
    active users other's restaurant Choices.

WebSockets have been used for interactive full duplex communication between users on sessions, since a user needs to see who joined and what others submitted as their choice of restaurant in real time
WebSockets are ideal for real-time bidirectional communication between a client and a server. It enables both the client and server to send messages to each other at any time. Which makes it the right solution
for this use case where a user needs to see what other users submitted as their restaurant choice.


**API spec url:**  http://<host:port>/restaurant-choice-service/swagger-ui/index.html

**Generated Swagger Doc:**  https://github.com/maduhshan/restaurant-choice-service/blob/master/api-docs/restaurant-choice-service-rest-api.yaml 


## Local Set Up Guide
< Assumption that you have pre-installed following tools in your local machine>
- Maven 3
- Java 17
- Git
- Intelij IDEA

#### Cloning the codebase
``git clone git@github.com:maduhshan/restaurant-choice-service.git``

By default, code will checkout at master branch. For further development activities 
please use your own feature branches. Feel free to raise a pull request for new features and add-ons.

#### Import the code to Intelij IDEA

Open IntelliJ and import the code base using the root folder or the pom.xml
``File > Open``

### Source Code
This code is developed by adhering to layered architecture, Below are the important packages to look out for.

#### src/main/java :
- ``gov.sg.tech.controller`` - Represents the presentation layer which consists controllers to server REST
and WebSocket communication.
  
- ``gov.sg.tech.service`` - Consists the business logic of the application and works and mediator to return
client understandable objects to controllers

- ``gov.sg.tech.dao`` - Contains a wrapper around all JPARepository classes. It also contains transformers which facilitates transforming DTOs, POJOs in to Entity Objects.
When adhering strictly on clean code architecture DAOs should accept POJOs as incoming parameters so that it does not know about the objects used in Controller layer.
  I have a slight deviation here as I thought given the size of the objects it would be un-necessary of an another layer of transformation from 
  entity -> pojo -> Response DTO
  
- ``gov.sg.tech.dao.repository`` - Classes in this layer gives the capability of data storage.
We are using spring-data supported JPARepositories and their in built capabilities for most of the
  storage logics.

- ``gov.sg.tech.controller.transformer`` - Transforms the objects to client understandable domain models and vice versa 
  (Transform to entity types to data persistence layer understandable objects)

- ``gov.sg.tech.entity`` - Contains ORM classes, which maps to database entities

- ``gov.sg.tech.domain`` - Contains domain models that are being used to communicate with clients 
  (These models would be the contract between client and server). There's a slight deviation from clean code architecture
  given the small nature of the use case. Hence, there are some domain models that are being shared from 
  controller layer to dao layer.

#### src/main/resources/static :
Location for web content

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

Application can also be bundled to a docker image. Navigate to source root folder (cd ../restaurant-choice-service/).
Then execute the command below to build the docker image.

``docker build -t restaurant-choice-service .``

Once docker build is success, Application can be started in a docker container

``docker run -p 8080:8080 restaurant-choice-service``

Psst! Don't forget to start docker service before docker commands. 

Once, the application is up Home page can be accessed at http://host:port/restaurant-choice-service .
On the local set up application is running at http://localhost:8080/restaurant-choice-service.

#### Note that db credentials are hardcoded for simplicity and demo purpose. In the ideal world all credentials needs to be managed using a secret manager such as AWS secret store, HashiCorp Vault. 


### User Journey on WEB UI

Navigate to Home Page: http://host:port/restaurant-choice-service

#### As Session Owner:

1. Provide your Name and Session Name that needs to be created
2. Once, session is created you are logged in to a session where you can submit a restaurant choice
3. Share the session id with other users

#### As a general user:
1. Provide the session id and username and join to the session
2. Submit the restaurant choice

**NOTE:** Only session owner can end the session.

## TODO

- Integration Tests
- API Automation suite for regression
- Async API Docs for web socket