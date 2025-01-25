# category-manager-backend
Backend - Category Management

## Overview

This is the backend for the Category Management System, built with Spring Boot. It provides a REST API for managing categories, including endpoints for CRUD operations and validations for parent-child relationships.

### Features

-Create, update, delete, and fetch categories.
-Validate parent-child relationships to prevent cycles or invalid hierarchies.
-Pagination and filtering for category lists.
-Handles errors with meaningful messages.
### Tech Stack
Framework: Spring Boot
Database: PostgreSQL
Build Tool: Maven

### Installation
Clone this repository along with the client side application 

A docker-compose will be availlble to use on the root directory of the project.
simply run a docker compose up -d --build (make sure to have docker installed)
The Web app will be available on  the port 4200 , whilst the Api is going to be available in json format (import directly to postman and test to your heart's content)