# Spring Boot Project Implement Endpoint Request & Validate OTP

## Requirements
- Build APIs for OTP Flow. 
- Usually OTP flow has 3 main steps: Request, Resend, and Validate. 
- But in this code project, we only create 2 steps: Request & Validate OTP. 
- OTP Flow rules:
  - OTP statuses: created, validated, expired. 
  - OTP code expiration time is 2 mins. 
- Data should be stored in a database. Either: postgres, mysql, mongo, etc. 
- Write unit tests.

## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`
- MySQL
```
spring.datasource.url=jdbc:mysql://localhost:3306/demo_db
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=create

otp.expiration.minutes=2
otp.validation.success.message=OTP validated successfully.
```

- Redis
```
spring.data.redis.host=localhost
spring.data.redis.port=6380
spring.data.redis.password=
spring.cache.redis.time-to-live=2
```

## Run following SQL insert statements
Open `data.sql`
```
CREATE DATABASE IF NOT EXISTS demo_db;
USE demo_db;

INSERT INTO otp_statuses(status) VALUES('CREATED');
INSERT INTO otp_statuses(status) VALUES('VALIDATED');
INSERT INTO otp_statuses(status) VALUES('EXPIRED');
```

## Run http requests
Open `manual.http`
