##### Sample Project for Qantas


### Description: 

Provide APIs(REST) to manage user creation,updation and deletion use case which can be consumed by either mobile app or web channel.

###Solution
 
The services involved in this solution are developed using micro-services architecture. Eurea server is used for service discovery. 

### Services

The project contains a backend database service, spring controller service that recieves the web request and eureka server that is used to deployed the services.

 ## eureka-server: 
 This contains the code to run eureka server.
 
 ## db-service:  
 This contains the code that runs in-memory H2 database that stores the information about users and their details. The DAO layer is implemented using Spring Data Rest and rest repositories such that the JPA layer is directly exposed as rest urls. This service is deployed and exposed from eureka server. 
 
 ## rest-service:  
 This contains the code that exposes the actual web apis that can potentially be consumed by various clients. This is also deployed on eureka server. This service uses RestTemplate to communicate to the db-service to fetch/update data.
 
 #### Security:  
 Spring security is used to authorise and authenticate users that interact with the system. The project uses basic in memory authentication client to show case the security layer and its implementation by the rest-service. Two users "testuser" and "admin" are created with roles ROLE_USER and ROLE_ADMIN.  THe user with ROLE_ADMIN has access to all the users in the system and also the deletion operation.  The normal users can create, update the user but cannot delete. 
 
 
 The solution can be extended to use an external identity management system for user authentication and authorization using Oauth2.0.
 
 #### Tests:  
 Developed unit tests cases for Controller and the serivce layer which has certain business logic. Especially, that deals with existing users and authorization with roles.  
 
 #### Api Documentation:  
 Swagger is used to expose the apis to the clients.  The swagger ui is deployed along with the rest-service and it is availabe at the endpoint hosturl/swagger-ui.html. All the apis exposed by the rest-service can be tested from the swagger-ui itself. However, i have also used postman and Restlet client (browser plugin) for testing. 
 
 #### Docker: 
 All the modules will be provided as docker images which can be deployed on to any VM containing docker. 
		In actual live scenario, the services are built using the build pipeline which emits the docker container as output.
 