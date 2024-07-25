# Yoga-app

It's a booking app for Savasana, a yoga studio where you can book sessions with differents teachers

## Techonlogies

Front-end:  
>Angular version 14, 
>Jest version 28, 
>Cypress version 10

The Back-end:  
>Java version 17, 
>Spring Boot version 2, 
>Maven version 3, 
>JUnit 5

## Getting started

### Clone the project
Clone the project in the directory of your choice :
>git clone https://github.com/guillaumebeysson/yoga-app

### Front-end
- Open your IDE and go to: `/front`
- Run `npm install` to install the dependencies
- Run `npm run start` to start the front-end
- Your app run on this url: http://localhost:4200/
- Login and password for the default account are:
    - login: yoga@studio.com  
    - password: test!1234

### Back-end
- Open your IDE and go to: `/back`
- In the file `application.properties` (located at : `/back/src/main/resources`):
  set the properties `spring.datasource.username` and `spring.datasource.password` with the same username and password you defined MySQL user.
- Run `mvn spring-boot:run` to start the back-end
- Default port used is 8080

### MySQL
- Create a new database `yoga`
- Use this new database
- From the directory `/ressources/sql` execute `script.sql` to create tables
- The previously script create a new admin user with the credentials:
    - login: yoga@studio.com  
    - password: test!1234

## Tests

### Front end tests

#### Jest (unit and integration tests)

- To start tests and generate coverage report run:
>npm run test --coverage

- Location coverage report is:
>front/coverage/jest/lcov-report/index.html

#### Cypress (end to end tests)

- To start Cypress run:
>npm run e2e

- To generate coverage report use:
>npm run e2e:coverage

- Location coverage report is:
>front/coverage/lcov-report/index.html

### Back end tests

#### JUnit (unit and integration tests)

- To start tests and generate coverage report run:
>mvn package

- Location coverage report is:
>back/target/site/jacoco/index.html