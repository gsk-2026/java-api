# Practice SoftwareTesting API Automation Suite

This is an independent, self-contained API testing microservice framework designed to validate the endpoints from the ExpandTesting practice platform.  The test suite ensures the functional reliability, , schema consistency, and security contracts of the production REST APIs hosted by **[Practice Software Testing](https://testsmith-io.github.io/practice-software-testing/#/)** (source code available on **[GitHub URL](https://github.com/testsmith-io/practice-software-testing)**, with full API swagger documentation in the **[Toolshop API Swagger description](https://api-v4.practicesoftwaretesting.com/api/documentation)**).

To maintain focus and simplicity, the automation suite framework targets only the **User** and **Invoice** endpoints, structured around an "Is-A" relationship between them.

In the architectural structure below, the focus is strictly on QA test automation. As a result, the **src/main** directory is left empty, while all testing logic and strategy reside entirely within the **src/test** directory. 


---


## Structural Overview & Scalability Architecture

```text
java-api/ (Standalone Workspace Root)
│
├── .gitignore                          # Local repository file tracking exclusions
├── pom.xml                             # Independent Maven configuration for java-api
├── README.md                           # Main workspace documentation 
│
├── practice-softwaretesting-api/       # [Active Microservice Project Module] 
│   ├── src/                           
│   │   ├── main/                       # Empty directory 
│   │   └── test/                       # QA test framework                 
│   │       ├── java/ 
│   │       │    ├── config/
│   │       │    ├── utils/ 
│   │       │    ├── model/ 
│   │       │    └── tests/             # Integration, End-to-End tests 
│   │       │        ├── base/
│   │       │        ├── user/
│   │       │        ├── invoice/
│   │       │        └── e2e/ 
│   │       └── resources/              # Test properties
│   ├── .gitignore                      # Service-specific file exclusion rules
│   ├── pom.xml                         # Microservice pom
│   ├── README.md                       # Microservice documentation 
│   
```

---

## Required Installation Verification

Below is the list of minimum installation requirements to run tests for the `practice-softwaretesting-api`:

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
#or get the practice-softwaretesting-api only from java-api
#git fetch origin
#git checkout origin/main -- practice-softwaretesting-api 
cd java-api-local/practice-softwaretesting-api
```

### 2. Run all client testing
```bash
# Windows (Git Bash/ CMD / PowerShell)
mvn clean test                                                                  # all client testing 
```

### 3. Run all/specific user client testing
```bash
# Windows (Git Bash/ CMD / PowerShell) 
mvn clean test -Dtest=User*Test                                                 # all user client testing
mvn clean test -Dtest=UserRegistrationTest#testUserRegistrationSuccess201       # specific user client testing
```

### 4. Run all/specific invoice client testing
```bash
# Windows (Git Bash/ CMD / PowerShell) 
mvn clean test -Dtest=Invoice*Test                                              # all invoice client testing
mvn clean test -Dtest=InvoicePostTest#testInvoicePostSuccess200                 # specific invoice client testing
```

---


##  Automation Engine

1. The test automation suite is executed via GitHub Actions workflows: **practice-softwaretesting-api-test.yml**.

2. Automated runs are scheduled Monday through Friday at 04:00 UTC (Standard).

3. The target testing environment rotates by day of the week:
    - Monday: `DIT`
    - Tuesday: `SIT`
    - Wednesday: `UAT`
    - Thursday: `PLT`
    - Friday: `PRD`


---