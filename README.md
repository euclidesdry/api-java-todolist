# Todo List App

 A Todo List app made with [JAVA 22](https://jdk.java.net/22/), [Apache Maven](https://maven.apache.org) and [Spring Boot](https://start.spring.io)

## Functional Requirements

- [x] Should be possible to Register a new User
- [x] Should be possible to Login with the User
- [ ] Should be possible to Validate a User
- [x] Should be possible to Add a Task
- [x] Should be possible to Update a Task entirely or partially
- [x] Should be possible to List all Tasks

## Business Requirements

- [x] Should not be possible to create a Task with an title with more than 50 characters
- [x] Should not be possible to create a Task with an date in the past from today
- [x] Should not be possible to create a Task with Start Date in the future than End Date
- [x] Should not be possible to create a Task with Start Date equal to End Date
- [x] A Task can only be possible to be manipulated by an authenticated User

## Non-Functional Requirements

- [x] User must be authenticated with Basic Auth
