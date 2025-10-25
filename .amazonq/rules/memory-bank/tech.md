# Technology Stack

## Programming Languages

### Java (UI, Cart, Orders)
- **Version**: Java 21
- **Framework**: Spring Boot 3.5.5
- **Build Tool**: Maven 3.x
- **Key Libraries**:
  - Spring WebFlux (reactive web)
  - Spring Cloud Gateway
  - Spring AI (Bedrock, OpenAI integration)
  - Lombok (code generation)
  - MapStruct (object mapping)
  - Kiota (API client generation)

### Go (Catalog)
- **Version**: Go 1.23.0+ (toolchain 1.24.5)
- **Framework**: Gin (web framework)
- **Key Libraries**:
  - GORM (ORM)
  - Testcontainers (integration testing)
  - go-envconfig (configuration)

### Node.js/TypeScript (Checkout)
- **Framework**: NestJS 11.x
- **Runtime**: Node.js with TypeScript 5.8+
- **Key Libraries**:
  - NestJS Terminus (health checks)
  - Redis client
  - class-validator/class-transformer

## Observability & Monitoring

### Metrics
- **Prometheus**: Metrics collection and exposition
- **Micrometer**: Java metrics instrumentation
- **prom-client**: Node.js Prometheus client

### Tracing
- **OpenTelemetry**: Distributed tracing standard
- **OTLP Exporters**: HTTP trace export
- **AWS X-Ray**: ID generation and resource detection
- **Instrumentation**:
  - Spring Boot auto-instrumentation
  - Gin/Go instrumentation
  - NestJS OTEL integration

## Data Persistence

### Relational Databases
- **MySQL/MariaDB**: Catalog service primary database
- **SQLite**: Testing and development

### NoSQL Databases
- **DynamoDB**: Cart service (AWS deployment)
- **MongoDB**: Cart service (alternative backend)
- **Redis**: Checkout session management

### Messaging
- **RabbitMQ**: Order event messaging

## Container & Orchestration

### Container Runtime
- **Docker**: Container image building and local execution
- **Multi-arch Support**: x86-64 and ARM64 images

### Orchestration
- **Kubernetes**: Primary orchestration platform
- **Helm**: Package management (charts for each service)
- **Istio**: Service mesh support
- **Docker Compose**: Local development

## Cloud Platforms

### AWS Services
- **Amazon EKS**: Managed Kubernetes
- **Amazon ECS**: Container orchestration
- **AWS App Runner**: Fully managed container service
- **Amazon RDS**: Managed databases
- **Amazon DynamoDB**: NoSQL database
- **AWS Bedrock**: AI/ML services

## Development Tools

### Build & Dependency Management
- **Nx**: Monorepo build system (v20.8.2)
- **Maven**: Java dependency management
- **Go Modules**: Go dependency management
- **Yarn**: Node.js package management

### Code Quality
- **Checkstyle**: Java code style enforcement
- **ESLint**: JavaScript/TypeScript linting
- **Prettier**: Code formatting

### Testing
- **JUnit**: Java unit testing
- **Testcontainers**: Integration testing with containers
- **Cypress**: E2E testing
- **Jest**: JavaScript testing
- **Artillery**: Load testing

## Infrastructure as Code
- **Terraform**: AWS infrastructure provisioning
- **Helmfile**: Kubernetes application deployment

## API & Documentation
- **OpenAPI 3.0**: API specification standard
- **Swagger**: API documentation
- **Kiota**: Type-safe API client generation

## Development Commands

### Java Services (UI, Cart, Orders)
```bash
./mvnw clean install          # Build
./mvnw spring-boot:run        # Run locally
./mvnw test                   # Run tests
```

### Go Service (Catalog)
```bash
go build                      # Build
go run main.go                # Run locally
go test ./...                 # Run tests
```

### Node.js Service (Checkout)
```bash
yarn install                  # Install dependencies
yarn build                    # Build
yarn serve:dev                # Run in development mode
yarn test                     # Run tests
```

### Docker
```bash
docker build -t <service> .   # Build image
docker run -p 8080:8080 <service>  # Run container
```

### Nx Workspace
```bash
nx run <project>:<target>     # Run specific target
npm run compose:up            # Start all services
npm run compose:down          # Stop all services
```
