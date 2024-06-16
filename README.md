## BACKEND PLATFORM SERVICES.

### Purpose of repository:

#### This repository is indented to serve as main repository for developing the backend services.

```
Greetings, Folks!

At time of starting the development, this has no clear purpose but mostly to enchance skills and mind.
However, we do try to develop it in the utmost scalable way.

If you are interested, please feel free to contribute !
```

### Architecture

```
Module based monolithic architecture.
```

### Technologies:

```
* Java Spring as backbone of the backend server
* PostgresSQL as datasource.
* Authentication and authorization - semi stateless based authentication with auto locking mechanism upon meeting
  specific criteria during the authentication process.
* AOP.
* Docker.
* MapStruct.
```

### How to start?

```
You can set up everything yourself(database, java etc...) or download docker and execute "docker compose up" in
terminal in the same directory.
```

## Changelog:

````agsl
Whats next ? Yet 1.9.0 to come, and we finally have to do some testing and validation :)
````

* 1.8.0 - Improvements in core and auth modules, mainly focused on unlocking templates.

* 1.7.0 - Some major improvements, please refer to each modules README for more information.

* 1.6.0 - Some major changes like new modules introduced.

* 1.5.0 Containerization - simply run "docker compose up" with following args:

```
- POSTGRES_USER=$POSTGRES_USER
- POSTGRES_PASSWORD=$POSTGRES_PASSWORD
- POSTGRES_DB=POSTGRES_DB
```

* 1.4.0 Fix namings, improve lazy load entities fetching and implement proper snapshots versioning.


* 1.3.0 Added local cache and secondary DataSource implementation.


* 1.0.0 Initial authorization + users session invalidation implemented.