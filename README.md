# Bajaj Health Service

A Spring Boot application that solves the Bajaj Health coding challenge by implementing a service that:
1. Generates a webhook on startup
2. Solves SQL problems based on registration number
3. Submits solutions using JWT tokens

## 🚀 Quick Start

### Download & Run
**Direct JAR Download**: [bajaj-health-service-1.0.0.jar](https://drive.google.com/file/d/1lELmy-WMDkmKsgUD5FMOk_EqS0RkYyey/view?usp=sharing)

```bash
# Download the JAR file from the link above
# Then run:
java -jar bajaj-health-service-1.0.0.jar
```

## Requirements Met

✅ **RestTemplate and WebClient**: Uses both as required  
✅ **No Controller/Endpoint**: Flow is triggered on application startup  
✅ **JWT Authorization**: Uses JWT token in Authorization header for second API call  
✅ **Spring Boot**: Built with Spring Boot framework  

## Project Structure

```
bajajQstn/
├── src/
│   ├── main/
│   │   ├── java/com/bajaj/
│   │   │   ├── BajajHealthApplication.java    # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java            # RestTemplate and WebClient configuration
│   │   │   ├── dto/
│   │   │   │   ├── WebhookRequest.java       # Webhook generation request DTO
│   │   │   │   ├── WebhookResponse.java      # Webhook generation response DTO
│   │   │   │   └── SolutionRequest.java      # Solution submission request DTO
│   │   │   └── service/
│   │   │       └── BajajHealthService.java   # Main business logic service
│   │   └── resources/
│   │       └── application.properties        # Application configuration
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

## Download Options

### 📦 Pre-built JAR File
**Direct Download**: [bajaj-health-service-1.0.0.jar](https://drive.google.com/file/d/1lELmy-WMDkmKsgUD5FMOk_EqS0RkYyey/view?usp=sharing)

### 🔨 Build from Source
```bash
# Clone the repository
git clone https://github.com/yourusername/bajajQstn.git
cd bajajQstn

# Build the application
mvn clean package

# The JAR will be created at: target/bajaj-health-service-1.0.0.jar
```

## How It Works

### 1. Application Startup
- The service automatically starts the flow when the application is ready
- No manual endpoint triggering required

### 2. Webhook Generation
- Sends POST request to `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- Uses RestTemplate for this request
- Request body contains name, registration number, and email

### 3. SQL Problem Solving
- Determines which question to solve based on last two digits of registration number
- **Odd digits**: Solves Question 1 (DEPARTMENT, EMPLOYEE, PAYMENTS tables)
- **Even digits**: Solves Question 2 (same schema)
- SQL solution finds highest salary NOT credited on 1st day of month

### 4. Solution Submission
- Submits the SQL solution to the webhook URL
- Uses WebClient for this request
- Includes JWT token in Authorization header

## SQL Solution Details

The SQL query solves the problem of finding the highest salary that was NOT credited on the 1st day of any month:

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1
```

## Setup and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher (only if building from source)

### Option 1: Run Pre-built JAR (Recommended)
```bash
# Download the JAR from: https://drive.google.com/file/d/1lELmy-WMDkmKsgUD5FMOk_EqS0RkYyey/view?usp=sharing
java -jar bajaj-health-service-1.0.0.jar
```

### Option 2: Build and Run from Source
```bash
cd bajajQstn
mvn clean package
java -jar target/bajaj-health-service-1.0.0.jar
```

### Option 3: Run with Maven
```bash
mvn spring-boot:run
```

## Configuration

The application can be configured via `application.properties`:
- Server port: 8080
- Connection timeouts: 10 seconds
- Logging levels: DEBUG for application, INFO for Spring

## API Endpoints Used

1. **Webhook Generation**: `POST https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
2. **Solution Submission**: `POST https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter WebFlux
- JJWT (JSON Web Token library)
- Spring Boot Starter Test (for testing)

## Submission Details

### 📋 For Bajaj Finserv Submission Form
- **GitHub Repository**: `https://github.com/yourusername/bajajQstn.git`
- **Public JAR Download Link**: `https://drive.google.com/file/d/1lELmy-WMDkmKsgUD5FMOk_EqS0RkYyey/view?usp=sharing`

## Notes

- The service uses the registration number "REG12347" (odd ending, so solves Question 1)
- Both RestTemplate and WebClient are used as per requirements
- The flow is completely automated and runs on application startup
- JWT token is properly included in the Authorization header for the second API call

## Testing

### Test Suite Overview

The project includes a comprehensive test suite covering:

- **Unit Tests**: Individual component testing
- **Integration Tests**: Spring context and service integration
- **DTO Tests**: Data transfer object validation
- **Configuration Tests**: Bean creation verification

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BajajHealthServiceSimpleTest

# Run tests with detailed output
mvn test -Dtest=*Test -Dsurefire.useFile=false
```

### Test Coverage

The test suite covers:

✅ **DTO Classes**: All getters, setters, constructors, and toString methods  
✅ **Service Logic**: SQL problem solving, registration number parsing  
✅ **Configuration**: RestTemplate and WebClient bean creation  
✅ **Business Rules**: Odd/even registration number logic  
✅ **SQL Content**: Query structure and required fields  

### Test Structure

```
src/test/java/com/bajaj/
├── BajajHealthApplicationTests.java          # Application context tests
├── dto/                                      # DTO unit tests
│   ├── WebhookRequestTest.java
│   ├── WebhookResponseTest.java
│   └── SolutionRequestTest.java
├── config/                                   # Configuration tests
│   └── WebConfigTest.java
├── service/                                  # Service tests
│   ├── BajajHealthServiceTest.java          # Complex mocking tests
│   └── BajajHealthServiceSimpleTest.java    # Simple logic tests
├── integration/                              # Integration tests
│   └── BajajHealthServiceIntegrationTest.java
└── util/                                     # Test utilities
    └── TestDataBuilder.java
```

### Test Configuration

- **Test Profile**: Uses `application-test.properties`
- **Mocking**: Mockito for external dependencies
- **Assertions**: JUnit 5 assertions
- **Test Data**: Centralized test data builder

## Troubleshooting

### Test Issues

- **Java 24 Compatibility**: Some Mockito features may have compatibility issues
- **Mocking Problems**: Use simple tests for core logic validation
- **Context Loading**: Ensure test profile is properly configured

### General Issues

- Check logs for detailed execution flow
- Ensure internet connectivity for external API calls
- Verify Java 17+ is installed and available
- Check Maven is properly configured

## 🎯 Submission Ready

This project is ready for submission to the Bajaj Finserv Health assessment. The JAR file is publicly accessible and the application meets all requirements specified in the problem statement.
