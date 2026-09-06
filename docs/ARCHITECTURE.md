# Clock Store — Backend Architecture

## Overview

Clock Store uses a layered Spring Boot architecture.

```text
┌─────────────────────┐
│   Angular Client    │
│   Angular Admin     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     Controller      │
│      REST API       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│        DTO          │
│ Request / Response  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│       Service       │
│   Business Logic    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     Repository      │
│     Data Access     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     PostgreSQL      │
│      Database       │
└─────────────────────┘
```

---

# Project Structure

```text
src/main/java/com/clockstore/Clock_Store/

├── controller/
│   └── CategoryController.java
│
├── service/
│   └── CategoryService.java
│
├── repository/
│   └── CategoryRepository.java
│
├── entity/
│   └── Category.java
│
├── dto/
│   ├── Request/
│   │   └── CategoryRequest.java
│   │
│   └── Response/
│       ├── CategoryResponse.java
│       └── ErrorResponse.java
│
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── BadRequestException.java
│   └── GlobalExceptionHandler.java
│
└── ClockStoreApplication.java
```

---

# Architecture Layers

## 1. Controller

The Controller layer handles HTTP communication.

Responsibilities:

* Receive HTTP requests.
* Validate request data.
* Read path variables.
* Read query parameters.
* Call the appropriate service.
* Return HTTP responses.

Controllers must not contain business logic.

Example:

```text
POST /api/categories
        ↓
CategoryController
        ↓
CategoryService
```

---

# 2. DTO

DTOs define the data exchanged between the frontend and backend.

## Request DTO

Used for incoming requests.

Example:

```json
{
  "parentId": null,
  "categoryName": "Watches",
  "slug": "watches",
  "description": "All watches",
  "status": true,
  "sortOrder": 1
}
```

## Response DTO

Used for outgoing responses.

Example:

```json
{
  "id": 1,
  "parentId": null,
  "categoryName": "Watches",
  "slug": "watches",
  "description": "All watches",
  "status": true,
  "sortOrder": 1
}
```

Entities must not be exposed directly through the REST API.

---

# 3. Service

The Service layer contains business logic.

Responsibilities:

* Business validation.
* Entity creation.
* Entity updates.
* Entity deletion.
* Repository communication.
* Entity → DTO mapping.
* DTO → Entity mapping.
* Business rules.

Example:

```text
CategoryController
        ↓
CategoryService
        ↓
CategoryRepository
```

---

# 4. Repository

The Repository layer handles database access.

Repositories use Spring Data JPA.

Example:

```java
public interface CategoryRepository
        extends JpaRepository<Category, Long> {
}
```

Database filtering should preferably be performed by the repository/database instead of loading all records into Java and filtering them manually.

Example:

```java
List<Category> findByStatus(boolean status);
```

instead of:

```java
categoryRepository.findAll()
        .stream()
        .filter(Category::isStatus)
        .toList();
```

---

# 5. Entity

Entities represent database tables.

Examples:

```text
Category       → categories
Brand          → brands
Product        → products
ProductImage   → product_images
ProductVariant → product_variants
Inventory      → inventory
```

Entities use Jakarta Persistence / JPA annotations.

---

# 6. Exception Handling

Application exceptions are handled centrally through:

```text
GlobalExceptionHandler
```

Supported application errors:

```text
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
```

Example:

```json
{
  "status": 404,
  "message": "Category not found with id: 15",
  "timestamp": "2026-09-01T20:30:00"
}
```

---

# API Architecture

The API uses REST principles.

Base URL:

```text
/api
```

Resources use plural nouns:

```text
/api/categories
/api/brands
/api/products
/api/product-images
/api/product-options
/api/product-variants
/api/inventory
/api/product-pricing
```

---

# HTTP Methods

| Method | Purpose            |
| ------ | ------------------ |
| GET    | Retrieve resources |
| POST   | Create resources   |
| PUT    | Update resources   |
| DELETE | Delete resources   |

---

# API Flow

Example: Create Category

```text
Angular Admin
      │
      │ POST /api/categories
      ▼
CategoryController
      │
      │ CategoryRequest
      ▼
CategoryService
      │
      │ Business validation
      ▼
CategoryRepository
      │
      ▼
PostgreSQL
      │
      ▼
Saved Category
      │
      ▼
CategoryResponse
      │
      ▼
Angular Admin
```

---

# Environment Configuration

Sensitive configuration must not be hardcoded in Java source code.

Environment variables are used for configuration.

Example:

```text
POSTGRES_HOST
POSTGRES_PORT
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
```

The `.env` file must not be committed to Git.

---

# Database

Database:

```text
PostgreSQL
```

ORM:

```text
Hibernate / Spring Data JPA
```

Primary key type:

```text
Long
```

Database primary key:

```text
BIGINT
```

---

# Development Principles

1. Controllers contain no business logic.
2. Services contain business logic.
3. Repositories handle database access.
4. DTOs are used for API communication.
5. Entities are not exposed directly.
6. Exceptions are handled globally.
7. Database filtering should happen at repository level.
8. Sensitive credentials must never be committed.
9. Entity IDs use `Long`.
10. API endpoints must be documented in `API_ENDPOINTS.md`.
11. Database structure must be documented in `DATABASE.md`.
12. New modules must follow the same layered architecture.
13. Business rules belong in the Service layer.
14. Database-specific operations belong in the Repository layer.
15. HTTP concerns belong in the Controller layer.

---

# Module Architecture

Each business module should follow the same structure.

Example:

```text
Category
├── CategoryController
├── CategoryService
├── CategoryRepository
├── Category
├── CategoryRequest
└── CategoryResponse
```

Future modules:

```text
Brand
├── BrandController
├── BrandService
├── BrandRepository
├── Brand
├── BrandRequest
└── BrandResponse

Product
├── ProductController
├── ProductService
├── ProductRepository
├── Product
├── ProductRequest
└── ProductResponse
```

This keeps the backend consistent and maintainable as the system grows.
