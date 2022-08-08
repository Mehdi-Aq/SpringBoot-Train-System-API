# SpringBoot-Train-System-API
A Spring Boot RESTful API for the train system database

## Project structure

```
|   |
|   +---SQL scripts
|   |       Data insertion.sql
|   |       Tables and Views creation.sql
|   |
|   +---src
|   |   +---main
|   |   |   +---java
|   |   |   |   \---com
|   |   |   |       \---example
|   |   |   |           \---demo
|   |   |   |               |   WSprojectApplication.java
|   |   |   |               |
|   |   |   |               +---controller
|   |   |   |               |       RailwayController.java
|   |   |   |               |
|   |   |   |               +---exception
|   |   |   |               |       AccountNotUpdatedException.java
|   |   |   |               |       DatabaseErrorException.java
|   |   |   |               |       EmailAlreadyRegisteredException.java
|   |   |   |               |       EmptyResultSetException.java
|   |   |   |               |       NoAvailableSeatsException.java
|   |   |   |               |       PassengerNotFoundException.java
|   |   |   |               |       UnsuccessfulPaymentException.java
|   |   |   |               |       UserNotFoundException.java
|   |   |   |               |       WrongPasswordException.java
|   |   |   |               |
|   |   |   |               +---model
|   |   |   |               |       LoginInfo.java
|   |   |   |               |       Passenger.java
|   |   |   |               |       Receipt.java
|   |   |   |               |       ReceiptDAO.java
|   |   |   |               |       Ticket.java
|   |   |   |               |       Trip.java
|   |   |   |               |       User.java
|   |   |   |               |
|   |   |   |               +---repository
|   |   |   |               |   |   RailwayRepository.java
|   |   |   |               |   |
|   |   |   |               |   \---dbmodel
|   |   |   |               \---service
|   |   |   |                       Generator.java
|   |   |   |                       RailwayService.java
|   |   |   |
|   |   |   \---resources
|   |   |       |   application.properties
|   |   |       |
|   |   |       +---static
|   |   |       \---templates
|   |   \---test
|   |       \---java
|   |           \---com
|   |               \---example
|   |                   \---demo
|   |                           WSprojectApplicationTests.java
|   |
|   \---target
|       +---classes
|       |   |   application.properties
|       |   |
|       |   \---com
|       |       \---example
|       |           \---demo
|       |               |   WSprojectApplication.class
|       |               |
|       |               +---controller
|       |               |       RailwayController.class
|       |               |
|       |               +---exception
|       |               |       AccountNotUpdatedException.class
|       |               |       DatabaseErrorException.class
|       |               |       EmailAlreadyRegisteredException.class
|       |               |       EmptyResultSetException.class
|       |               |       NoAvailableSeatsException.class
|       |               |       PassengerNotFoundException.class
|       |               |       UnsuccessfulPaymentException.class
|       |               |       UserNotFoundException.class
|       |               |       WrongPasswordException.class
|       |               |
|       |               +---model
|       |               |       LoginInfo.class
|       |               |       Passenger.class
|       |               |       Receipt.class
|       |               |       ReceiptDAO.class
|       |               |       Ticket.class
|       |               |       Trip.class
|       |               |       User.class
|       |               |
|       |               +---repository
|       |               |       RailwayRepository.class
|       |               |
|       |               \---service
|       |                       Generator.class
|       |                       RailwayService.class
|       |
|       +---generated-sources
|       |   \---annotations
|       +---generated-test-sources
|       |   \---test-annotations
|       \---test-classes
|           \---com
|               \---example
|                   \---demo
|                           WSprojectApplicationTests.class
|
\---SQL scripts
        Data insertion.sql
        Tables and Views creation.sql
        ```

## Endpoints test

[Postman collection](/RailWayAPI.postman_collection.json)
        
