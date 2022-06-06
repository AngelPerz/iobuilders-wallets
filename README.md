# iobuilders-wallets
Wallets microservice of the simulated small bank for ioBuilders 

compiled and tested with:
``` 
graalvm-ce-java17-21.3.1 
```

## Requirements
This microservice requires [**iobuilder-users**](https://github.com/AngelPerz/iobuilders-users) 
to be running and the dockerized Postgres database declared in the same project:
``` 
/iobuilders-users/docker-compose/docker-compose.yml
```

## Installation
To compile the application use the Maven wrapper launching the following command in the project's root directory:
```
./mvnw clean install
```


## Usage
Start the application and send REST requests to the API, by default in `http://localhost:8081/`

For API documentation and local testing the Swagger Page is available in 
[http://localhost:8081/swagger-ui/#/](http://localhost:8081/swagger-ui/#/)