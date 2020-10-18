# my-retail Product Detail Service
This service is for serving product information to my-retail internal systems.
## Technology Stack:
Java 11, Spring Boot, Spock, Cassandra, Groovy, Maven, Jmeter, Docker, Swagger2

## Getting Started
Follow the instructions to run the application on local machine with cassandra on docker.

### Install docker:

```
For mac : https://docs.docker.com/docker-for-mac/install/
For Windows: https://docs.docker.com/docker-for-windows/install/
```

### Install maven:
```
https://maven.apache.org/install.html
```

### Install Openjdk11:
```
https://openjdk.java.net/install/
```

### Clone repository 

```
git clone https://github.com/svaasan/my-retail-product-api.git
```

### Run Cassandra Docker Instance 

```
cd my-retail-product-api
cd docker
docker-compose build
docker-compose up
```


### Build and Run:
Switch to application(repo) root directory
```
mvn clean install
mvn spring-boot:run 
```

### Sample Requests
>For more api documentation please refer swagger
>http://localhost:8080/swagger-ui.html#/

#### Product Price Update
```
curl --location --request PUT 'http://localhost:8080/v1/products/13860420' \
--header 'Content-Type: application/json' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "id": 13860420,
    "current_price": {
        "value": 14.49,
        "currency_code": "USD"
    }
}'
```

#### Get Product Details
```
curl --location --request GET 'http://localhost:8080/v1/products/13860420' \
--header 'Content-Type: application/json'
```

### Further Reading:
* [SpringBoot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) - Application Framework
* [Maven](https://maven.apache.org/developers/index.html) - Dependency & Build
* [Groovy](http://groovy-lang.org/documentation.html) - Unit Testing
* [Cassandra](http://cassandra.apache.org/doc/latest/architecture/index.html) - Datastore
* [Spock](http://spockframework.org/spock/docs/1.1/index.html) - Testing

#### Author: Shrinivasan Venkataramani
        
   ***email: svaasan@gmail.com***