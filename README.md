## HOLIDAY HUB - BACKEND PLATFORM SERVICES.

### Project short summary:

#### Model behind "Holiday Hub" is intended to provide SINGLE and easy to use/integrate touristic oriented services for both persons and companies seeking advertisement of their services.

### Purpose of repository:

#### This repository is indented to serve as main repository for developing the backend services.

### Architecture

* Module based monolithic architecture.

### Technologies:

* Java Spring as backbone of the backend server
* PostgresSQL as initial solution for database.
* Authentication and authorization - semi stateless based authentication with auto locking mechanism upon meeting specific criteria during the authentication process.
* AOP.
* Docker.
* MapStruct.

### How to start?

- You can set up everything yourself(database, java etc...) or download docker and execute "docker compose up" in
  terminal in the same directory.

## Changelog:

* 1.7.0 - Some major improvements, please refer to each modules README for more information.

* 1.6.0 - Some major changes like new modules introduced.

* 1.5.0 Containerization - simply run "docker compose up" with following args:
    - POSTGRES_USER=$POSTGRES_USER
    - POSTGRES_PASSWORD=$POSTGRES_PASSWORD
    - POSTGRES_DB=POSTGRES_DB


* 1.4.0 Fix namings, improve lazy load entities fetching and implement proper snapshots versioning.


* 1.3.0 Added local cache and secondary DataSource implementation.


* 1.0.0 Initial authorization + users session invalidation implemented.