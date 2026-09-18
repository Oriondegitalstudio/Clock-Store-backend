# Clock Store — Database

## Database Engine

```text
PostgreSQL
```

## ORM

```text
Spring Data JPA / Hibernate
```

## Primary Key

All entity IDs use:

```text
Long
```

---

# Database Tables

## 1. Categories

### Table

```text
categories
```

| Column               | Type    | Required | Description          |
| -------------------- | ------- | -------- | -------------------- |
| `id`                 | BIGINT  | Yes      | Primary key          |
| `parent_category_id` | BIGINT  | No       | Parent category      |
| `category_name`      | VARCHAR | Yes      | Category name        |
| `slug`               | VARCHAR | Yes      | SEO-friendly URL     |
| `description`        | TEXT    | No       | Category description |
| `image`              | VARCHAR | No       | Category image       |
| `status`             | BOOLEAN | Yes      | Active / inactive    |
| `sort_order`         | INTEGER | No       | Display order        |

### Relationship

```text
categories
    │
    └── parent_category_id
            │
            ▼
       categories.id
```

A category can have another category as its parent.

Example:

```text
Watches
├── Men's Watches
├── Women's Watches
└── Smart Watches
```

---

# 2. Brands

### Table

```text
brands
```

| Column        | Type    | Required | Description       |
| ------------- | ------- | -------- | ----------------- |
| `id`          | BIGINT  | Yes      | Primary key       |
| `name`        | VARCHAR | Yes      | Brand name        |
| `logo`        | VARCHAR | No       | Brand logo        |
| `description` | TEXT    | No       | Brand description |
| `website`     | VARCHAR | No       | Official website  |
| `status`      | BOOLEAN | Yes      | Active / inactive |

---

# 3. Products

### Table

```text
products
```

| Column              | Type      | Required | Description               |
| ------------------- | --------- | -------- | ------------------------- |
| `id`                | BIGINT    | Yes      | Primary key               |
| `name`              | VARCHAR   | Yes      | Product name              |
| `slug`              | VARCHAR   | Yes      | SEO-friendly URL          |
| `sku`               | VARCHAR   | Yes      | Product SKU               |
| `barcode`           | VARCHAR   | No       | Product barcode           |
| `brand_id`          | BIGINT    | Yes      | Product brand             |
| `category_id`       | BIGINT    | Yes      | Main category             |
| `short_description` | TEXT      | No       | Short description         |
| `description`       | TEXT      | Yes      | Full description          |
| `weight`            | DECIMAL   | No       | Product weight            |
| `warranty`          | VARCHAR   | No       | Warranty information      |
| `status`            | VARCHAR   | Yes      | Draft / Active / Archived |
| `visibility`        | VARCHAR   | Yes      | Public / Private          |
| `featured`          | BOOLEAN   | Yes      | Featured product          |
| `created_at`        | TIMESTAMP | Yes      | Creation date             |
| `updated_at`        | TIMESTAMP | Yes      | Last modification         |

---

# 4. Product Images

### Table

```text
product_images
```

| Column       | Type    | Required | Description        |
| ------------ | ------- | -------- | ------------------ |
| `id`         | BIGINT  | Yes      | Primary key        |
| `product_id` | BIGINT  | Yes      | Product            |
| `variant_id` | BIGINT  | No       | Product variant    |
| `url`        | VARCHAR | Yes      | Image URL          |
| `alt_text`   | VARCHAR | No       | Accessibility text |
| `sort_order` | INTEGER | No       | Display order      |
| `is_primary` | BOOLEAN | Yes      | Main image         |

---

# 5. Product Options

### Table

```text
product_options
```

| Column         | Type    | Required | Description                       |
| -------------- | ------- | -------- | --------------------------------- |
| `id`           | BIGINT  | Yes      | Primary key                       |
| `name`         | VARCHAR | Yes      | Option name                       |
| `display_type` | VARCHAR | Yes      | Dropdown / Color / Image / Button |
| `sort_order`   | INTEGER | No       | Display order                     |

---

# 6. Product Option Values

### Table

```text
product_option_values
```

| Column       | Type    | Required | Description   |
| ------------ | ------- | -------- | ------------- |
| `id`         | BIGINT  | Yes      | Primary key   |
| `option_id`  | BIGINT  | Yes      | Parent option |
| `value`      | VARCHAR | Yes      | Option value  |
| `color_code` | VARCHAR | No       | Hex color     |
| `image`      | VARCHAR | No       | Swatch image  |
| `sort_order` | INTEGER | No       | Display order |

---

# 7. Product Variants

### Table

```text
product_variants
```

| Column       | Type    | Required | Description        |
| ------------ | ------- | -------- | ------------------ |
| `id`         | BIGINT  | Yes      | Primary key        |
| `product_id` | BIGINT  | Yes      | Parent product     |
| `sku`        | VARCHAR | Yes      | Variant SKU        |
| `barcode`    | VARCHAR | No       | Variant barcode    |
| `price`      | DECIMAL | Yes      | Selling price      |
| `sale_price` | DECIMAL | No       | Promotional price  |
| `cost_price` | DECIMAL | No       | Purchase price     |
| `weight`     | DECIMAL | No       | Variant weight     |
| `stock`      | INTEGER | Yes      | Available quantity |
| `status`     | VARCHAR | Yes      | Active / Disabled  |

---

# 8. Variant Attributes

### Table

```text
variant_attributes
```

| Column            | Type   | Required | Description           |
| ----------------- | ------ | -------- | --------------------- |
| `variant_id`      | BIGINT | Yes      | Variant               |
| `option_id`       | BIGINT | Yes      | Option                |
| `option_value_id` | BIGINT | Yes      | Selected option value |

---

# 9. Inventory

### Table

```text
inventory
```

| Column                | Type    | Required | Description      |
| --------------------- | ------- | -------- | ---------------- |
| `id`                  | BIGINT  | Yes      | Primary key      |
| `variant_id`          | BIGINT  | Yes      | Product variant  |
| `quantity`            | INTEGER | Yes      | Current stock    |
| `reserved`            | INTEGER | Yes      | Reserved stock   |
| `available`           | INTEGER | Yes      | Available stock  |
| `low_stock_threshold` | INTEGER | Yes      | Alert threshold  |
| `warehouse_location`  | VARCHAR | No       | Storage location |

---

# 10. Product Pricing

### Table

```text
product_pricing
```

| Column       | Type    | Required | Description     |
| ------------ | ------- | -------- | --------------- |
| `id`         | BIGINT  | Yes      | Primary key     |
| `variant_id` | BIGINT  | Yes      | Product variant |
| `currency`   | VARCHAR | Yes      | Currency        |
| `price`      | DECIMAL | Yes      | Standard price  |
| `sale_price` | DECIMAL | No       | Discount price  |
| `start_date` | DATE    | No       | Promotion start |
| `end_date`   | DATE    | No       | Promotion end   |

---

# Entity Relationships

```text
                    ┌──────────────┐
                    │   Category   │
                    └──────┬───────┘
                           │
                           │
                    ┌──────▼───────┐
                    │    Product   │
                    └──────┬───────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
              ▼            ▼            ▼
       ProductImage   ProductVariant   ProductOption
                           │
                ┌──────────┼──────────┐
                │          │          │
                ▼          ▼          ▼
           Inventory   Pricing   VariantAttribute
                                      │
                                      ▼
                              OptionValue
                                      │
                                      ▼
                                    Option

Product ───────────────► Brand
```

---

# Database Rules

* Primary keys use `BIGINT`.
* Entity IDs use Java `Long`.
* Foreign keys use `BIGINT`.
* Required fields should be non-null.
* Slugs should be unique where applicable.
* SKU values should be unique.
* Database relationships are managed through JPA.
* Sensitive database credentials must not be committed to Git.
