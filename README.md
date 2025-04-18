# 📌 Microservices & Ports Configuration


![image](https://github.com/user-attachments/assets/71c2ea44-fd7a-46cb-b449-e5d38c868cb3)


# 📦 TechGear - Product Service Documentation

## 🧾 Overview
The Product Service is a microservice responsible for managing the catalog of products sold on TechGear, including smartphones, TVs, smartwatches, and other tech gadgets.

---

## 🧩 Features

- Create, Read, Update, Delete (CRUD) products
- Support for multiple categories (e.g., smartphones, TVs, smartwatches)
- Store multiple images per product
- Compare product prices with eBay listings via API
- Automatic fetching of eBay token with hourly refresh

---

## 🔧 Tech Stack

- Java + Spring Boot
- JPA + Hibernate
- RESTful API
- Jackson (for JSON)
- Scheduled Tasks
- eBay Browse API Integration

---

## 📁 Entity: `Product`

| Field        | Type              | Description                          |
|--------------|-------------------|--------------------------------------|
| `id`         | Long              | Unique identifier                    |
| `name`       | String            | Product name                         |
| `description`| String            | Product details                      |
| `price`      | double            | Product price                        |
| `stock`      | int               | Available units                      |
| `category`   | String            | Product category                     |
| `images`     | List<String>      | List of image URLs                   |
| `createdAt`  | LocalDateTime     | Timestamp of creation                |
| `updatedAt`  | LocalDateTime     | Timestamp of last update             |

---

## 🌐 Endpoints

### `GET /products`
- Description: Retrieve all products
- Response: `List<Product>`

### `GET /products/{id}`
- Description: Retrieve product by ID

### `GET /products/category/{category}`
- Description: Get all products in a specific category

### `POST /products`
- Description: Create a new product
- Body:
```json
{
  "name": "iPhone 17 Pro Max",
  "description": "Latest Apple flagship smartphone",
  "price": 1299.99,
  "stock": 25,
  "category": "Smartphone",
  "images": [
    "https://example.com/images/iphone17-front.jpg",
    "https://example.com/images/iphone17-back.jpg"
  ]
}
```

### `PUT /products/{id}`
- Description: Update an existing product

### `DELETE /products/{id}`
- Description: Delete a product

---

## 🔄 Scheduled Tasks

- **Token Refresh**: Every hour, the application refreshes the eBay access token using credentials stored in environment variables.

