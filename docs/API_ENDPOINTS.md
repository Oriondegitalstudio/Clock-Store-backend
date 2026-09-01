# Clock Store API — Endpoints

## Base URL

```text
http://localhost:8080/api
```

run the project using this command ``` mvnw.cmd spring-boot:run ```
---

# 1. Categories

## Get All Categories

**GET**

```text
/categories
```

Returns all categories.

### Response — `200 OK`

```json
[
  {
    "id": 1,
    "parentId": null,
    "categoryName": "Watches",
    "slug": "watches",
    "description": "All watches",
    "image": null,
    "status": true,
    "sortOrder": 1
  }
]
```

---

## Get Category By ID

**GET**

```text
/categories/{id}
```

### Path Parameters

| Parameter | Type | Required | Description         |
| --------- | ---- | -------- | ------------------- |
| `id`      | Long | Yes      | Category identifier |

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Category found        |
| `404`  | Category not found    |
| `500`  | Internal server error |

---

## Create Category

**POST**

```text
categories/create
```

### Request Body

```json
{
  "parentId": null,
  "name": "Watches",
  "slug": "watches",
  "description": "All watches",
  "image": null,
  "status": true,
  "sortOrder": 1
}
```

### Status Codes

| Status | Description               |
| ------ | ------------------------- |
| `201`  | Category created          |
| `400`  | Invalid request           |
| `404`  | Parent category not found |
| `409`  | Slug already exists       |
| `500`  | Internal server error     |

---

## Update Category

**PUT**

```text
/categories/update/{id}
```

### Path Parameters

| Parameter | Type | Required | Description         |
| --------- | ---- | -------- | ------------------- |
| `id`      | Long | Yes      | Category identifier |

### Request Body

```json
{
  "parentId": null,
  "categoryName": "Luxury Watches",
  "slug": "luxury-watches",
  "description": "Luxury watches",
  "image": null,
  "status": true,
  "sortOrder": 1
}
```

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Category updated      |
| `400`  | Invalid request       |
| `404`  | Category not found    |
| `409`  | Slug already exists   |
| `500`  | Internal server error |

---

## Delete Category

**DELETE**

```text
/categories/{id}
```

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `204`  | Category deleted      |
| `404`  | Category not found    |
| `500`  | Internal server error |

---

## Get Active Categories

**GET**

```text
/categories/active
```

Returns only active categories.

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Categories retrieved  |
| `500`  | Internal server error |

---

## Get Categories By Parent

**GET**

```text
/categories/parent/{parentId}
```

Returns all child categories belonging to a parent category.

### Path Parameters

| Parameter  | Type | Required | Description                |
| ---------- | ---- | -------- | -------------------------- |
| `parentId` | Long | Yes      | Parent category identifier |

### Status Codes

| Status | Description                |
| ------ | -------------------------- |
| `200`  | Child categories retrieved |
| `404`  | Parent category not found  |
| `500`  | Internal server error      |

---

## Get Categories By Status

**GET**

```text
/categories/status/{status}
```

### Path Parameters

| Parameter | Type    | Required | Description                      |
| --------- | ------- | -------- | -------------------------------- |
| `status`  | Boolean | Yes      | `true` active / `false` inactive |

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Categories retrieved  |
| `500`  | Internal server error |

---

## Get Categories By Sort Order

**GET**

```text
/categories/sort-order/{sortOrder}
```

### Path Parameters

| Parameter   | Type    | Required | Description   |
| ----------- | ------- | -------- | ------------- |
| `sortOrder` | Integer | Yes      | Display order |

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Categories retrieved  |
| `500`  | Internal server error |

---

## Search Categories By Name

**GET**

```text
/categories/search?name={name}
```

### Query Parameters

| Parameter | Type   | Required | Description   |
| --------- | ------ | -------- | ------------- |
| `name`    | String | Yes      | Category name |

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Categories retrieved  |
| `500`  | Internal server error |

---

## Check Category Slug

**GET**

```text
/categories/exists?slug={slug}
```

### Query Parameters

| Parameter | Type   | Required | Description   |
| --------- | ------ | -------- | ------------- |
| `slug`    | String | Yes      | Category slug |

### Response

```json
true
```

### Status Codes

| Status | Description           |
| ------ | --------------------- |
| `200`  | Check completed       |
| `500`  | Internal server error |

---

# 2. Brands

> Endpoints will be added when Brand management is implemented.

---

# 3. Products

> Endpoints will be added when Product management is implemented.

---

# 4. Product Images

> Endpoints will be added when Product Image management is implemented.

---

# 5. Product Options

> Endpoints will be added when Product Option management is implemented.

---

# 6. Product Option Values

> Endpoints will be added when Product Option Value management is implemented.

---

# 7. Product Variants

> Endpoints will be added when Product Variant management is implemented.

---

# 8. Variant Attributes

> Endpoints will be added when Variant Attribute management is implemented.

---

# 9. Inventory

> Endpoints will be added when Inventory management is implemented.

---

# 10. Product Pricing

> Endpoints will be added when Product Pricing management is implemented.

---

# HTTP Status Codes

| Status | Meaning                       |
| ------ | ----------------------------- |
| `200`  | Request successful            |
| `201`  | Resource created              |
| `204`  | Resource deleted successfully |
| `400`  | Bad request                   |
| `404`  | Resource not found            |
| `409`  | Resource conflict             |
| `500`  | Internal server error         |
