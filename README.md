# MoneyTransferAPI between from/to accounts
 Spring boot application which provide RESTful APIs to preview accounts and for money transfer

### Prerequisite
- Maven
- JDK 1.8+
### Project Structure
```bash
E-Transfer API
├── src
│   ├── main
│   │   ├── java\com\cashew\eTransfer
│   │   └── resources
│   └── test
│       ├── java\com\cashew\eTransfer
│       └── resources
├── .gitignore
├── pom.xml
└── README.md
```
### Packaging
```
mvn package
```
### Test
```
mvn test

Test Result Reports : \target\surefire-reports

```

### Running
```
java -jar eTransfer-0.0.1.jar
```
### Accounts Data
Reading data from accounts.json file while Spring boot starts and inserting into H2 DB which located in (src\main\resources\accounts.json)

## Feature
This application provides APIs for following 2 features
- Retrieve Accounts info
- Transfer money between from/to accounts
### Basic API Information
| Method | Path             | Usage                  |
| --- |------------------|------------------------|
| GET | /api/v1/accounts | retrieve accounts info |
| POST | /api/v1/transfer | send money transaction |
### Swagger-UI
API Specification is provided in the [swagger-ui page](http://localhost:8080/swagger-ui.html) after application starts.
```
http://localhost:8080/swagger-ui.html
```
### Error Code
| Code | Description |
| --- | --- |
| ERR_SYS | used when internal server error happens |
| ERR_CLIENT_001 | used when error due to client's input or business logic |
| ERR_CLIENT_002 | used when error related to account logic |
### Library used
| Library | Usage |
| --- | --- |
| spring boot | for spring boot application |
| spring data jpa | for ORM and DB operation purpose |
| H2 | In memory DB to insert accounts in json file while startup |
| springfox swagger | generate swagger API specification from code |
| springfox swagger ui | generate swagger ui page for specification |
# public
# transfer-api
