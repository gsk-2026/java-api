# Java-API

This is a standalone, top-level Maven container workspace. It serves as a unified physical repository structure designed to house multiple completely independent microservices side-by-side.

---


## Structural Overview & Scalability Architecture

The `java-api` directory operates as an architectural folder hub. New microservices could be added as standalone folders sitting parallel to each other. None of these sub-projects share runtime contexts or parent pom inheritances, ensuring a completely decoupled microservice architecture.

```text
java-api/ (Standalone Workspace Root)
│
├── src/                               # Core source directory for java-api code base
├── .gitignore                         # Local repository file tracking exclusions
├── pom.xml                            # Independent Maven configuration for java-api
├── Jenkinsfile                        # CI/CD orchestration pipeline definition 
├── README.md                          # Main workspace documentation (This File)
│
├── client-resource-access-api/        # [Active Microservice Project Module]
│   ├── .mvn/                          # Maven wrapper binaries and settings
│   ├── src/                           
│   │   ├── main/                      # Application layer
│   │   │   ├── java/                  # Java controller, service, domain packages
│   │   │   └── resources/             # application.properties, banners, YAMLs
│   │   └── test/                      
│   │       ├── java/                  # Unit, Integration, and Load tests
│   │       └── resources/             # Test properties, SQL seeds, Gatling assets
│   ├── .gitignore                     # Service-specific file exclusion rules
│   ├── Dockerfile                     # OCI Container construction instructions 
│   ├── mvnw / mvnw.cmd                # Cross-platform Maven wrapper binaries
│   ├── pom.xml                        # Inherits directly from spring-boot-starter-parent
│   │
│   └── 🛠️ Local Automation Scripts:
│       ├── build_clean_compile.bat    # Windows: Compiles source and processors
│       ├── build_clean_compile.sh     # Unix/Bash: Compiles source and processors
│       ├── build_clean_package.bat    # Windows: Packages code into an executable .jar
│       ├── build_clean_package.sh     # Unix/Bash: Packages code into an executable .jar
│       ├── build_clean_test.bat       # Windows: Triggers local unit tests
│       ├── build_clean_test.sh        # Unix/Bash: Triggers local unit tests
│       ├── build_clean_verify.bat     # Windows: Runs full test and verification loops
│       └── build_clean_verify.sh      # Unix/Bash: Runs full test and verification loops
│
├── new-a-api/                         # [Future New A Standalone Microservice Folder Example]
├── new-b-api/                         # [Future New B Standalone Microservice Folder Example]
```

---


## Required Installation Verification

Following is list of required installation to run the testing for `client-resource-access-api`:

### 1. Java

```bash
java --version                           # sample return: java 26 
```

### 2. mvn

```bash
mvn --version                            # sample return: Apache Maven 3.9.16 
```

### 3. docker

```bash
docker --version                         # required to run ClientResourceAccessApiDockerE2E, ClientResourceAccessApiE2ELoadRunner, and Jenkins CI/CD pipeline
```


### 4. Database (e.g: Oracle DB)

```bash
lsnrctl status                           # The DB could also be a docker container, not a native service.  Required to run ClientResourceAccessApiOracleE2E
```

### 5. Jenjins

```bash
sc query Jenkins                        # Jenkins could also be a docker container, or a browser application.  Required for CI/CD pipeline
```

---


## Comprehensive Quality Testing Suites - Manually triger the testing with samples

All terminal test validations must be executed from inside the target service directory (`./client-resource-access-api`):

### 1. Clone the Codebase
Open your terminal workspace and execute the git clone operation:
```bash
git clone https://github.com/gsk-2026/java-api java-api-local
cd java-api-local/client-resource-access-api
```

### 2. Run all/specific config unit testing
```bash
# Windows (Git Bash/ CMD / PowerShell)
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.config.*Test"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.config.SecurityConfigTest"         # sample
```

### 3. Run all/specific controller unit testing
```bash
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.controller.*Test"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.controller.ClientControllerTest"   # sample
```

### 4. Run all/specific repository unit testing
```bash
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.repository.*Test"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.repository.ClientRepositoryTest"   # sample
```

### 5. Run all/specific service unit testing
```bash
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.service.*Test"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.service.ClientServiceImplTest"   # sample
```

### 6. Run all (controller, repository, service) unit testing
```bash
mvn clean test 
```

### 7. Run all/specific integration testing (IT)
```bash
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.*IT"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.ClientResourceAccessIT"   # sample
```

### 8. Run all/specific End-to-End (E2E) testing
```bash
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.*E2E"
mvn clean test -Dtest="com.dreamtech.clientresourceaccessapi.ClientResourceAccessApiDockerE2E"   # sample
```

### 9. Run performance testing
```bash
mvn clean package    # 1. Bpackage the compiled code into a JAR file
java -Dspring.profiles.active=qatest -jar target/client-resource-access-api-0.0.1-SNAPSHOT.jar    # 2. launches the API package
mvn gatling:test -Dgatling.simulationClass=com.dreamtech.clientresourceaccessapi.ClientResourceAccessApiE2ELoadScenario      # 3. trigger the peformance tetsing
### The performance tets result will be presented in a file - refer the at the end of the logs
```

### 10. Run all (unit, integration, end-to-end, performance) testing
```bash
mvn clean verify -Pqatest
```

### 11. Launch the API package
```bash
mvn clean package    # 1. Package the compiled code into a JAR file
java -Dspring.profiles.active=qatest -jar target/client-resource-access-api-0.0.1-SNAPSHOT.jar    # 2. launches the API package
http://localhost:8181/swagger-ui/index.html         # 3. Notes API Documentation
```

---
   

##  CI/CD Automation Engine - The CI/CD pipeline triggers unit, integration, end-to-end, and performance tests

The root project incorporates a multi-tier **Jenkinsfile** configuration script. Every pull request or branch commit pushed to your remote repository activates a central automation pipeline which:
1. Validates structural workspace integrity.
2. Compiles your underlying dependencies.
3. Automatically spins up mock database isolation environments via Testcontainers.
4. Packages and builds your production OCI Docker container bundles.
5. The CI/CD pipeline executes all unit, integration, E2E, and performance tests.
5. Code changes in the Git features will initiate the unit tests only.
6. Code changes in the Git main will trigger integration, end-to-end, and performance tests too

---