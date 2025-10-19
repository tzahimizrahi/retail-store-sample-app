# Retail Store Application Service Mapping

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              EXTERNAL USER                               │
└────────────────────────────────┬────────────────────────────────────────┘
                                 │ HTTP
                                 ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          UI SERVICE (Java)                               │
│                         Port: 8080                                       │
│                    Namespace: ui                                         │
│                                                                           │
│  • Store Frontend (Thymeleaf)                                            │
│  • API Gateway                                                           │
│  • Kiota-generated clients                                               │
└──────┬──────────────┬──────────────┬──────────────┬─────────────────────┘
       │              │              │              │
       │ REST         │ REST         │ REST         │ REST
       ▼              ▼              ▼              ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│  CATALOG    │ │    CART     │ │  CHECKOUT   │ │   ORDERS    │
│  (Go)       │ │   (Java)    │ │  (Node.js)  │ │   (Java)    │
│  Port: 8080 │ │  Port: 8080 │ │  Port: 8080 │ │  Port: 8080 │
│             │ │             │ │             │ │             │
│ Namespace:  │ │ Namespace:  │ │ Namespace:  │ │ Namespace:  │
│  catalog    │ │   carts     │ │  checkout   │ │   orders    │
└──────┬──────┘ └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
       │               │               │               │
       │ SQL           │ NoSQL         │ Cache         │ SQL + MQ
       ▼               ▼               ▼               ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│   MySQL     │ │  DynamoDB   │ │    Redis    │ │ PostgreSQL  │
│             │ │   Local     │ │             │ │             │
│ Port: 3306  │ │  Port: 8000 │ │  Port: 6379 │ │  Port: 5432 │
│             │ │             │ │             │ │             │
│ StatefulSet │ │  Deployment │ │  Deployment │ │ StatefulSet │
└─────────────┘ └─────────────┘ └─────────────┘ └──────┬──────┘
                                                        │
                                                        │ AMQP
                                                        ▼
                                                 ┌─────────────┐
                                                 │  RabbitMQ   │
                                                 │ (Optional)  │
                                                 │             │
                                                 │ Order Events│
                                                 └─────────────┘
```

## Service Details

### **UI Service** (ui namespace)
- **Language**: Java 21 / Spring Boot
- **Purpose**: Store frontend and API gateway
- **Dependencies**: Calls all backend services
- **Pod**: `ui-65476579bb-2mrcr`

### **Catalog Service** (catalog namespace)
- **Language**: Go 1.23
- **Purpose**: Product catalog API
- **Database**: MySQL (catalog-mysql-0)
- **Features**: Product listing, search, filtering
- **Pod**: `catalog-5fdcc8c65-n2rfv`

### **Cart Service** (carts namespace)
- **Language**: Java 21 / Spring Boot
- **Purpose**: Shopping cart management
- **Database**: DynamoDB Local (carts-dynamodb-995f7768c-mt4wv)
- **Features**: Add/remove items, cart persistence
- **Pod**: `carts-68d496fff8-bg2ck`

### **Checkout Service** (checkout namespace)
- **Language**: Node.js / NestJS
- **Purpose**: Checkout orchestration
- **Cache**: Redis (checkout-redis-69cb79ff4d-cfq95)
- **Features**: Coordinates cart → order flow
- **Pod**: `checkout-5b885fb57c-chsnq`

### **Orders Service** (orders namespace)
- **Language**: Java 21 / Spring Boot
- **Purpose**: Order management and tracking
- **Database**: PostgreSQL (orders-postgresql-0)
- **Messaging**: RabbitMQ (optional, for order events)
- **Pod**: `orders-74f89d6dbd-8v5bp`

## Communication Flow

1. **User → UI**: Browser HTTP requests
2. **UI → Services**: REST API calls (synchronous)
3. **Services → Databases**: Direct connections
4. **Orders → RabbitMQ**: Async order events (optional)

## Network Architecture

- **Cluster**: eks-mcp-workshop (us-west-2)
- **Nodes**: 3 worker nodes
- **Pod Network**: 10.42.0.0/16 CIDR
- **Service Discovery**: Kubernetes DNS
- **Load Balancing**: Kubernetes Services

## Observability

- **Metrics**: Prometheus endpoints on all services
- **Tracing**: OpenTelemetry OTLP (optional)
- **Health Checks**: Kubernetes liveness/readiness probes
