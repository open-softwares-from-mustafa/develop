Getting Started
Reference Documentation
For further reference, please consider the following sections:

Official Apache Maven documentation
Spring Boot Maven Plugin Reference Guide
Create an OCI image
Spring Boot DevTools
Brokerage API Service
version 0.0.1

Tech Stack
Spring Boot 3.3.4+

JDK17+

Maven

h2 Database

Running Application
Run mvn clean install to build the project.
Run mvn spring-boot:run to start the application.
The API will be accessible at http://localhost:8080.
Testing Endpoints
Authorization Type: Basic Auth

Username: admin
Password: admin@2024
In Postman:

Go to the "Authorization" tab.
Select "Basic Auth" from the dropdown.
Enter the admin username and admin@2024 as the password.
Sample Requests
GET Method http://localhost:8080/login

POST Method http://localhost:8080/orders/create Json Body : { "orderId": 1, "customerId": 1, "assetName": "TRY", "orderSide": "BUY", "size": 10, "price": 150.0 }

POST Method http://localhost:8080/customers/create Json Body : { "name": "Mustafa Simsak", "email": "mustafa@test.com" }

GET Method http://localhost:8080/orders/list?customerId=1&start=2024-09-28T00:00:00&end=2024-09-30T00:00:00

GET Method http://localhost:8080/orders/match/1

DELETE Method http://localhost:8080/orders/1

GET Method http://localhost:8080/customers/1/assets

POST Method http://localhost:8080/customers/1/withdraw?amount=500&iban=TR5555555555555555555555

POST Method http://localhost:8080/customers/1/deposit?amount=20000
