# Tool Shop API Automation Suite

This is an independent, self-contained API testing microservice framework designed to validate the endpoints from the ExpandTesting practice platform.  The purpose of this testing suite is to ensure the functional health, schema reliability, and security contracts of the production REST APIs hosted on the **[TOOL SHOP](https://v4.practicesoftwaretesting.com/#/)**, (source code available on **[GitHub URL](https://github.com/testsmith-io/practice-software-testing)**, with full API documentation in the **[Toolshop REST API technical description](https://api-v4.practicesoftwaretesting.com/api/documentation)**).

For simplicity and clarity, the test automation framework implements only the **User** and **Invoice** endpoints, utilizing a **"Has-A"** relationship (composition) between them.

In the architectural structure below, the **src/main** directory acts as a client wrapper for the Tool Shop API.


---


## Structural Overview & Scalability Architecture

```text
java-api/ (Standalone Workspace Root)
│
├── .gitignore                          # Local repository file tracking exclusions
├── pom.xml                             # Independent Maven configuration for java-api
├── README.md                           # Main workspace documentation 
│
├── tool-shop-api/                      # [Active Microservice Project Module] 
│   ├── src/                           
│   │   ├── main/                       # Application wrapper layer 
│   │   │   ├── java/                   # Java packages
│   │   │   └── resources/              # application.properties for various environments
│   │   └── test/                      
│   │       ├── java/                   # Integration, End-to-End tests
│   │       └── resources/              # Test properties
│   ├── .gitignore                      # Service-specific file exclusion rules
│   ├── pom.xml                         # Microservice pom
│   ├── README.md                       # Microservice documentation 
│   
```

---

## Required Installation Verification

Below is the list of minimum installation requirements to run tests for the `tool-shop-api`:

### 1. Java

```bash
java --version                           # sample return: java 26 
```

### 2. mvn

```bash
mvn --version                            # sample return: Apache Maven 3.9.16 
```

---


## Steps to Manually Execute Quality Testing Suites

The examples below show how to manually run the full quality testing workflow:

### 1. Clone the Codebase
```bash
git clone https://github.com/gsk-2026/java-api java-api-local
#or get the tool-shop-api only from java-api
#git fetch origin
#git checkout origin/main -- tool-shop-api 
cd java-api-local/tool-shop-api
```

### 2. Run all client testing
```bash
# Windows (Git Bash/ CMD / PowerShell)
mvn clean test -Dtest="com.dreamtech.toolshopapi.client.*Test"        # all client testing 
```

### 3. Run all/specific user client testing
```bash
# Windows (Git Bash/ CMD / PowerShell) 
mvn clean test -Dtest=UserClientTest                                  # all user client testing
mvn clean test -Dtest=UserClientTest#testUserRegistrationSuccess201   # specific user client testing
```

### 4. Run all/specific invoice client testing
```bash
# Windows (Git Bash/ CMD / PowerShell) 
mvn clean test -Dtest=InvoiceClientTest                             # all invoice client testing
mvn clean test -Dtest=InvoiceClientTest#testInvoicePostSuccess201   # specific invoice client testing
```

### 5. Run all/specific end-to-end testing
```bash
# Windows (Git Bash/ CMD / PowerShell) 
mvn clean test -Dtest=E2EToolShopTest                               # all end-to-end testing
mvn clean test -Dtest=E2EToolShopTest#testE2EUserClientHappyPath    # specific end-to-end testing
```

---


##  Automation Engine

1. The test automation suite is executed via GitHub Actions workflows: **tool-shop-api-test.yml**.

2. Automated runs are scheduled Monday through Friday at 03:00 UTC (Standard).

3. The target testing environment rotates by day of the week:
   - Monday: `DIT`
   - Tuesday: `SIT`
   - Wednesday: `UAT`
   - Thursday: `PLT`
   - Friday: `PRD`


---