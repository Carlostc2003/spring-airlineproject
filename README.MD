# Airline Backend Project

![header-airline-backend-project](docs/img/header-img.png)

REST API developed with **Spring Boot** for the comprehensive management of airline operations.  
You can test it here: https://spring-airlineproject.onrender.com/

## Technologies

- Java 25
- Apache Maven 3.9.15
- MongoDB Cloud Atlas
- Spring Boot 4.0.6
- Spring Data MongoDB
- Spring Security
- Spring Web MVC
- Jakarta Validation & Jakarta Bean Validation
- Lombok
- SpringDoc OpenAPI 2.8.9 Swagger (https://spring-airlineproject.onrender.com/swagger-ui/index.html)
- JUnit 5 Jupiter
- Mockito 5.14.2
- BCryptPasswordEncoder, Pbkdf2PasswordEncoder, DelegatingPasswordEncoder & CORS config
- Java Serialization

## Core Features

- Create, Update, List & Delete aircrafts
- Retrieve each aircraft
- Create, Update, List & Delete bookings
- Retrieve bookings for each user
- Check in a user using
- Cancel a booking (does not delete the record, only changes its status)
- Create, Update, List & Delete flights
- Obtain flights by indicating departure airport, arrival airport, departure date and seat class.
- Get the list of departure airport information
- Create, Update, List & Delete users
- Allows you to log in with email and password, or with Google login.
- Allows transferring miles to another user
- Allows the user to change their password

## Installation and Execution

### 1. Clone the repository

```bash
git clone https://github.com/Carlostc2003/spring-airlineproject.git

cd spring-airlineproject/backend
```  

### 2. Configure environment variables

```properties
server.port=${PORT:8080}
app.cors.allowed-origins=${APP_CORS_ALLOWED_ORIGINS:http://localhost:4200,http://127.0.0.1:4200}
spring.mongodb.uri=${SPRING_MONGODB_URI}
```
- `PORT` Backend port, by default 8080, configure it according to your preferences, make sure the indicated port is free.
- `APP_CORS_ALLOWED_ORIGINS` The allowed domain for CORS is the frontend domain, or leave it as default if it is local.
- `SPRING_MONGODB_URI`URI for database access in MongoDB Cloud Atlas, example:
```
mongodb+srv:// [user] : [password] @ [server_db] / [name_db] ?appName= [name_server_db] &retryWrites=true&w=majority
```

### 3. Project construction
- Windows
```powershell
.\mvnw clean package -DskipTests
```
- Linux
```bash
./mvnw clean package -DskipTests
```

### 4. Execution

Once the project is compiled, a `.jar` file will be generated inside `target/`.

Run the application with:

```bash
java -jar target/backend-1.1.2.jar
```

## API Rest Documentation
Once the backend is running, go to the following link where you can find all the API documentation and program functionality.

`http:// [domain] /swagger-ui/index.html`

The domain will be localhost:8080 if you haven't made any changes. If you want to get the information, go here.
https://spring-airlineproject.onrender.com/swagger-ui/index.html

## MongoDB Structure

- aircrafts

```json
{
  "_id": "EC-123",
  "model": "Airbus A380-800",
  "features": {
    "starlink": true,
    "externalCameras": true,
    "cabinScreens": true
  },
  "seats": {
    "economy": 280,
    "xxl": 130,
    "business": 60,
    "total": 470
  }
}
```

- bookings

```json
{
  "userId": "6a1211d168f347741eb488d8",
  "flightId": "6a1208c4b91b597b0df9d2e4",
  "seatClass": "ECONOMY",
  "seatNumber": "14C",
  "details": {
    "bookingDate": {
      "$date": "2026-05-23T21:30:00.000Z"
    },
    "price": 450.5,
    "status": "CONFIRMED"
  }
}
```

- flights

```json
{
  "flightId": "IL-832",
  "departure": {
    "airport": "SIN",
    "city_airport": "Singapore",
    "date": {
      "$date": "2026-06-15T11:00:00.000Z"
    },
    "terminal": "T1",
    "boardingGate": "B22"
  },
  "arrival": {
    "airport": "HND",
    "city_airport": "Tokyo",
    "date": {
      "$date": "2026-06-16T07:30:00.000Z"
    },
    "terminal": "T3",
    "boardingGate": "C354"
  },
  "duration": 1230,
  "aircraftId": "EC-XAB",
  "prices": {
    "economy": 450.5,
    "xxl": 600,
    "business": 1200
  },
  "seats": {
    "economy": 150,
    "xxl": 20,
    "business": 10
  }
}
```

- users

```json
{
  "email": "carlos.tormo@github.com",
  "name": "Carlos",
  "surname": "Tormo",
  "passport": {
    "id": "ABC123456",
    "expirationDate": {
      "$date": "2031-05-15T00:00:00.000Z"
    },
    "issueCountry": "Spain"
  },
  "phone": {
    "prefix": "+34",
    "number": "612345678"
  },
  "birthDate": {
    "$date": "1998-03-22T00:00:00.000Z"
  },
  "sex": "male",
  "miles": 0,
  "password": "$2a$12$eImiTxAk4kvM26AIE8vGh.9qWSTrR9NiE7VpYmD6vKMNnO5R452bO",
  "authProvider": "LOCAL"
}
```

## Feature History

- `29/05/2026` It allows you to obtain the list of airports where planes take off using the API https://spring-airlineproject.onrender.com/api/flights/departures. Example:
````json
[
  {
    "airport_code": "HKG",
    "city_airport": "Hong Kong"
  },
  {
    "airport_code": "ICN",
    "city_airport": "Seoul"
  }
]
````

- `30/05/2026` Now you can access the actuator api, allowing you to use health, mappings, metrics and info.
  - `health` https://spring-airlineproject.onrender.com/actuator/health
  - `mappings` https://spring-airlineproject.onrender.com/actuator/mappings
  - `metrics` https://spring-airlineproject.onrender.com/actuator/metrics
  - `info` https://spring-airlineproject.onrender.com/actuator/info