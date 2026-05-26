# Product ↔ Category Many-to-Many Update

## What changed

The old relationship was not correct for many-to-many:

- `ProductEntity` had one category.
- `CategoryEntity` had one product.

Now the relationship is:

- One product can have many categories.
- One category can contain many products.
- A join table named `product_categories` connects them.

---

## Database tables

Hibernate will use these tables:

```text
products
categories
product_categories
```

The join table contains:

```text
product_id
category_id
```

---

## Create category first

### Request

```http
POST /api/categories
```

```json
{
  "name": "Electronics",
  "description": "Electronic products"
}
```

Create another category:

```json
{
  "name": "Accessories",
  "description": "Product accessories"
}
```

---

## Create product with many categories

### Request

```http
POST /api/products
```

```json
{
  "name": "iPhone 15",
  "description": "Apple smartphone",
  "price": 1500,
  "quantity": 5,
  "categoryIds": [1, 2],
  "active": true
}
```

Important: use `categoryIds`, not `categoryId`.

---

## Update product categories

### Request

```http
PUT /api/products/1
```

```json
{
  "name": "iPhone 15 Pro",
  "categoryIds": [1, 3]
}
```

If you send `categoryIds`, the old product categories are replaced with the new list.
If you do not send `categoryIds`, categories stay unchanged.

---

## Product response example

```json
{
  "id": 1,
  "name": "iPhone 15",
  "description": "Apple smartphone",
  "price": 1500,
  "quantity": 5,
  "categories": [
    {
      "id": 1,
      "name": "Electronics",
      "description": "Electronic products"
    },
    {
      "id": 2,
      "name": "Accessories",
      "description": "Product accessories"
    }
  ],
  "active": true,
  "createdAt": "2026-05-06T00:00:00",
  "totalPrice": 7500
}
```

---

## Files changed

- `ProductEntity.java`
- `CategoryEntity.java`
- `CreateProductRequest.java`
- `UpdateProductRequest.java`
- `CreateCategoryRequest.java`
- `UpdateCategoryRequest.java`
- `ProductResponse.java`
- `CategoryResponse.java`
- `ProductMapper.java`
- `CategoryMapper.java`
- `ProductService.java`
- `CategoryService.java`
- `ProductRepository.java`
- `application.properties`


## Assign one category to existing product

```http
POST /api/products/{productId}/categories/{categoryId}
```

Example:

```http
POST /api/products/1/categories/3
```

This adds category `3` to product `1` without removing the product's existing categories.

---

## Remove one category from existing product

```http
DELETE /api/products/{productId}/categories/{categoryId}
```

Example:

```http
DELETE /api/products/1/categories/3
```

This removes category `3` from product `1`.

---

## Important difference

Use `PUT /api/products/{id}` with `categoryIds` when you want to replace all product categories.

Use `POST /api/products/{productId}/categories/{categoryId}` when you want to add only one category.

Use `DELETE /api/products/{productId}/categories/{categoryId}` when you want to remove only one category.
