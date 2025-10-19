# Development Guidelines

## Code Quality Standards

### Licensing and Copyright
**Pattern Frequency: 4/5 files**
- All source files MUST include the MIT-0 license header at the top
- Standard format:
```
/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: MIT-0
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
```

### Package and Import Organization
**Pattern Frequency: 5/5 files**

#### Java
- Package names follow reverse domain notation: `com.amazon.sample.<service>.<layer>`
- Imports organized in groups: Java standard library, third-party, internal
- Use fully qualified imports, avoid wildcards
- Example:
```java
package com.amazon.sample.carts.services;

import com.amazon.sample.carts.repositories.CartEntity;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
```

#### Go
- Package names are lowercase, single-word
- Imports grouped: standard library, third-party, internal
- Example:
```go
package repository

import (
    "context"
    "fmt"
    
    "github.com/aws-containers/retail-store-sample-app/catalog/config"
    "gorm.io/gorm"
)
```

#### JavaScript/TypeScript
- Use ES6 module syntax
- Organize imports: external libraries first, then internal modules
- Use const for immutable references

### Naming Conventions

#### Java
**Pattern Frequency: 3/3 Java files**
- Classes: PascalCase (e.g., `DynamoDBCartService`, `ItemsRequestBuilder`)
- Methods: camelCase (e.g., `getProducts`, `deleteItem`)
- Constants: UPPER_SNAKE_CASE (e.g., `EXCHANGE_NAME`, `ORDERS_ORDERS_QUEUE`)
- Private fields: camelCase with descriptive names
- Interface implementations: Concrete class name describes implementation (e.g., `DynamoDBCartService` implements `CartService`)

#### Go
**Pattern Frequency: 1/1 Go file**
- Exported types/functions: PascalCase (e.g., `NewRepository`, `GetProducts`)
- Unexported: camelCase (e.g., `createMySQLDatabase`)
- Interfaces: Descriptive noun ending (e.g., `CatalogRepository`)
- Struct fields: PascalCase for exported, camelCase for unexported

#### JavaScript
**Pattern Frequency: 1/1 JS file**
- Objects: PascalCase for constructors/classes (e.g., `ChatUI`)
- Functions/methods: camelCase (e.g., `sendMessage`, `scrollToBottom`)
- Constants: camelCase for object properties, UPPER_SNAKE_CASE for true constants

## Architectural Patterns

### Dependency Injection (Java/Spring)
**Pattern Frequency: 3/3 Java files**
- Use constructor injection for required dependencies
- Annotate configuration classes with `@Configuration`
- Use `@Bean` for explicit bean definitions
- Leverage `@ConditionalOnProperty` for conditional configuration
- Example:
```java
@Configuration
@ConditionalOnProperty(prefix = "messaging", name = "provider", havingValue = "rabbitmq")
public class RabbitMQMessagingConfig {
    @Autowired
    private RabbitMQProperties properties;
    
    @Bean
    public MessagingProvider messagingProvider(RabbitTemplate template) {
        return new RabbitMQMessagingProvider(template);
    }
}
```

### Repository Pattern
**Pattern Frequency: 2/5 files**
- Define repository interfaces with clear method contracts
- Implement concrete repositories for different backends
- Use context propagation for tracing (Go: `ctx context.Context`, Java: implicit)
- Example interface:
```go
type CatalogRepository interface {
    GetProducts(tags []string, order string, pageNum, pageSize int, ctx context.Context) ([]model.Product, error)
    GetProduct(id string, ctx context.Context) (*model.Product, error)
}
```

### Builder Pattern (Java)
**Pattern Frequency: 2/3 Java files**
- Use fluent builder APIs for complex object construction
- Chain method calls for readability
- Example:
```java
this.table.createTable(builder ->
    builder
        .globalSecondaryIndices(builder3 -> builder3.indexName("idx_global_customerId"))
        .provisionedThroughput(b -> b.readCapacityUnits(1L).writeCapacityUnits(1L))
);
```

### Request/Response Pattern
**Pattern Frequency: 1/5 files**
- Separate request building from execution (Kiota pattern)
- Provide overloaded methods with and without configuration
- Example:
```java
public Item post(@jakarta.annotation.Nonnull final Item body) {
    return post(body, null);
}

public Item post(@jakarta.annotation.Nonnull final Item body,
                 @jakarta.annotation.Nullable final java.util.function.Consumer<PostRequestConfiguration> requestConfiguration) {
    // Implementation
}
```

## Common Annotations and Decorators

### Java Annotations
**Pattern Frequency: 3/3 Java files**

#### Lombok
- `@Slf4j`: Automatic logger field injection
- Use for reducing boilerplate in service classes

#### Jakarta/Spring
- `@jakarta.annotation.Generated`: Mark generated code (Kiota clients)
- `@jakarta.annotation.Nonnull` / `@jakarta.annotation.Nullable`: Null safety
- `@PostConstruct`: Initialization after dependency injection
- `@Bean`: Spring bean definition
- `@Configuration`: Configuration class marker
- `@ConditionalOnProperty`: Conditional bean creation
- `@Autowired`: Dependency injection (prefer constructor injection)

#### Validation
- Use `Objects.requireNonNull()` for parameter validation
- Example:
```java
public WithItemItemRequestBuilder byItemId(@jakarta.annotation.Nonnull final String itemId) {
    Objects.requireNonNull(itemId);
    // ...
}
```

## Error Handling

### Java
**Pattern Frequency: 2/3 Java files**
- Use Optional for potentially missing values
- Provide fallback behavior with `ifPresentOrElse`
- Example:
```java
@Override
public void deleteItem(String customerId, String itemId) {
    item(customerId, itemId).ifPresentOrElse(
        this.table::deleteItem,
        () -> log.warn("Item missing for delete {} -- {}", customerId, itemId)
    );
}
```

### Go
**Pattern Frequency: 1/1 Go file**
- Always return and check errors explicitly
- Use `fmt.Errorf` with `%w` for error wrapping
- Panic only for unrecoverable initialization errors
- Example:
```go
if err != nil {
    return nil, fmt.Errorf("failed to fetch tags: %w", err)
}
```

### JavaScript
**Pattern Frequency: 1/1 JS file**
- Use try-catch for async operations
- Log errors to console
- Provide user-friendly error messages
- Example:
```javascript
try {
    await this.processResponse(message, loadingDiv);
} catch (error) {
    console.error("Error:", error);
    ChatUI.appendMessage("bot", "Sorry, there was an error processing your message.");
}
```

## Logging Practices

### Java (SLF4J)
**Pattern Frequency: 2/3 Java files**
- Use `@Slf4j` annotation for logger injection
- Log levels: `info` for important events, `warn` for recoverable issues
- Include context in log messages
- Example:
```java
log.info("Using RabbitMQ messaging");
log.warn("Item missing for delete {} -- {}", customerId, itemId);
```

### Go
**Pattern Frequency: 1/1 Go file**
- Use `fmt.Printf` and `fmt.Println` for standard output
- Include descriptive context
- Example:
```go
fmt.Printf("Using mysql database %s\n", config.Endpoint)
```

## Database and Persistence Patterns

### GORM (Go)
**Pattern Frequency: 1/1 Go file**
- Use method chaining for query building
- Always pass context with `WithContext(ctx)`
- Use `Preload` for eager loading relationships
- Apply pagination with `Offset` and `Limit`
- Example:
```go
query := db.DB.WithContext(ctx).
    Preload("Tags").
    Where("id = ?", id).
    First(&product)
```

### DynamoDB Enhanced Client (Java)
**Pattern Frequency: 1/3 Java files**
- Use `TableSchema.fromClass()` for entity mapping
- Leverage Global Secondary Indexes for queries
- Use `Key.builder()` for key construction
- Example:
```java
DynamoDbIndex<DynamoItemEntity> index = this.table.index("idx_global_customerId");
QueryConditional q = QueryConditional.keyEqualTo(
    Key.builder().partitionValue(customerId).build()
);
```

### Spring AMQP (RabbitMQ)
**Pattern Frequency: 1/3 Java files**
- Define exchanges, queues, and bindings as Spring beans
- Use Jackson converters for JSON serialization
- Configure separate converters for producer and consumer
- Example:
```java
@Bean
FanoutExchange exchange() {
    return new FanoutExchange(EXCHANGE_NAME);
}

@Bean
Binding binding(Queue queue, FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
}
```

## Frontend Patterns (JavaScript)

### Module Pattern
**Pattern Frequency: 1/1 JS file**
- Use object literal pattern for namespacing
- Initialize in `DOMContentLoaded` event
- Example:
```javascript
const ChatUI = {
    elements: null,
    init(contextPath) {
        this.elements = { /* ... */ };
        this.bindEvents();
    }
};

document.addEventListener("DOMContentLoaded", () => {
    ChatUI.init(retailContextPath);
});
```

### Event Handling
- Bind events in dedicated method
- Use arrow functions to preserve context
- Example:
```javascript
bindEvents() {
    this.elements.sendButton.addEventListener("click", () => this.handleSendMessage());
}
```

### Async/Await with Streams
- Use async/await for cleaner asynchronous code
- Handle streaming responses with ReadableStream API
- Example:
```javascript
async processResponse(message, loadingDiv) {
    const response = await this.fetchBotResponse(message);
    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    
    while (true) {
        const { value, done } = await reader.read();
        if (done) break;
        // Process chunk
    }
}
```

## Configuration Management

### Spring Boot Properties
**Pattern Frequency: 2/3 Java files**
- Use `@EnableConfigurationProperties` for type-safe configuration
- Define property classes with prefix
- Use `@ConditionalOnProperty` for feature toggles

### Environment-Based Configuration (Go)
**Pattern Frequency: 1/1 Go file**
- Support multiple backend types via configuration
- Provide sensible defaults (e.g., in-memory database for development)
- Example:
```go
if config.Type == "mysql" {
    db, err = createMySQLDatabase(config)
} else {
    db, err = gorm.Open(sqlite.Open("file::memory:?cache=shared"), &gorm.Config{})
}
```

## Testing and Initialization

### Database Migration and Seeding
**Pattern Frequency: 2/5 files**
- Run migrations on application startup
- Seed initial data programmatically
- Check for existing data before inserting
- Example (Go):
```go
db.AutoMigrate(&model.Product{})
products, err := LoadProductData()
// Insert products if not exists
```

### Lifecycle Hooks
**Pattern Frequency: 2/3 Java files**
- Use `@PostConstruct` for initialization logic
- Implement `ApplicationListener<ApplicationReadyEvent>` for post-startup tasks
- Example:
```java
@PostConstruct
public void init() {
    if (createTable) {
        this.table.createTable(/* ... */);
    }
}
```

## Code Style Enforcement

### Java
- Use Checkstyle with project-specific configuration
- Configuration location: `../misc/style/java/checkstyle.xml`
- Exclude generated code from checks (e.g., Kiota clients)

### JavaScript
- Use Prettier for code formatting
- Configuration in `.prettierrc`

## API Design Principles

### RESTful Conventions
**Pattern Frequency: 2/5 files**
- Use standard HTTP methods: GET (retrieve), POST (create), PATCH (update), DELETE (remove)
- Return appropriate types: collections for list operations, single objects for item operations
- Use void return for operations without response body

### OpenAPI Specification
- All services define OpenAPI specifications (`openapi.yml`)
- Generate type-safe clients from specifications (Kiota for Java)
- Maintain API contracts across services
