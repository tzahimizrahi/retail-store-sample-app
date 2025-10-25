# Project Structure

## Directory Organization

### `/src` - Application Components
Core microservices implementing the retail store functionality:

- **`ui/`** - Store user interface (Java/Spring Boot)
  - Web frontend with Thymeleaf templates
  - API gateway to backend services
  - Kiota-generated API clients for service communication
  - Static assets and JavaScript for interactive features

- **`catalog/`** - Product catalog service (Go)
  - Product listing and search API
  - MySQL/MariaDB persistence
  - RESTful API with OpenAPI specification

- **`cart/`** - Shopping cart service (Java/Spring Boot)
  - User cart management API
  - DynamoDB or MongoDB backend support
  - Session-based cart operations

- **`orders/`** - Order management service (Java/Spring Boot)
  - Order creation and tracking
  - RabbitMQ messaging integration
  - Database persistence for order history

- **`checkout/`** - Checkout orchestration (Node.js/NestJS)
  - Checkout process coordination
  - Integration with cart and order services
  - Redis for session management

- **`app/`** - Full application deployment
  - Docker Compose configurations
  - Helmfile for Kubernetes deployments
  - Tiltfile for local development

- **`e2e/`** - End-to-end testing (Cypress)
  - Integration test suites
  - UI and API testing scenarios

- **`load-generator/`** - Load testing tool
  - Artillery-based load generation
  - Exercises all infrastructure components

### `/terraform` - Infrastructure as Code
AWS deployment configurations:

- **`eks/default/`** - Full EKS deployment with AWS services (RDS, DynamoDB)
- **`eks/minimal/`** - Minimal EKS with in-cluster dependencies
- **`ecs/default/`** - Amazon ECS deployment
- **`apprunner/`** - AWS App Runner deployment
- **`lib/`** - Reusable Terraform modules

### `/docs` - Documentation
- Architecture diagrams and images
- Feature documentation
- Screenshots and visual assets

### `/samples` - Sample Data
- Product catalog JSON data
- Product images for demo store

### `/scripts` - Build and Deployment Scripts
- Docker Compose distribution scripts
- Kubernetes manifest generation
- E2E testing automation
- Security and code scanning utilities

### `/.github` - CI/CD Workflows
- GitHub Actions workflows
- Automated testing and publishing
- Release management

### `/oss` - Open Source Compliance
- License attribution generation
- ORT (OSS Review Toolkit) configurations

## Component Architecture

### Microservices Pattern
The application follows a deliberately over-engineered microservices architecture with:
- **Service Independence**: Each component has separate codebase and deployment
- **Polyglot Implementation**: Multiple languages (Java, Go, Node.js)
- **API-First Design**: OpenAPI specifications for all services
- **Backend Flexibility**: Multiple persistence options per service

### Communication Patterns
- **Synchronous**: REST APIs between services
- **Asynchronous**: RabbitMQ messaging for order events
- **Client Generation**: Kiota for type-safe API clients

### Deployment Structure
Each service includes:
- `Dockerfile` - Container image definition
- `chart/` - Helm chart for Kubernetes deployment
- `docker-compose.yml` - Local development setup
- `openapi.yml` - API specification
- `scripts/` - Build and utility scripts

## Build System
- **Nx Monorepo**: Workspace management with Nx
- **Language-Specific Tools**: Maven (Java), Go modules, npm/yarn (Node.js)
- **Container Builds**: Multi-architecture Docker images
- **Helm Charts**: Kubernetes deployment packages
