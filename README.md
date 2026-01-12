# 🛒 Marketplace Backend — Spring Boot & JPA

## 📌 Overview
This project is a **Marketplace Backend API** built using **Spring Boot and Spring Data JPA**, simulating a real-world e-commerce system with **multi-seller support, order processing, payments, reviews, and analytical reports**.

It emphasizes **clean domain modeling, business rules, reporting, and performance-aware query design**, making it suitable for **backend engineering assessments and portfolio showcase**.

---

## 🧱 Core Features

### 🔹 Product & Seller Management
- Seller can manage multiple products
- Product supports multiple variants (SKU, price, stock)
- Proper JPA relationship mapping
- Cascade rules & orphan handling

### 🔹 Cart, Order & Payment Flow
- User cart & cart items
- Order & order items
- Payment status handling (`PAID`, `FAILED`, etc.)
- Transactional business logic
- Race condition awareness

### 🔹 Review System
- User can submit **one review per product variant**
- Rating (1–5) with optional review text
- Review only allowed for users who completed payment
- Clean domain naming (`Review` instead of technical naming)
- Unique constraint enforcement at database level

---

## 📊 Reporting & Analytics

Includes advanced reporting queries commonly found in production systems:

### Example Reports
- Daily Sales Report
- Revenue by Time Range
- Failed vs Successful Payments
- Repeat Buyer Rate
- Average Order Value (AOV)
- Seller Performance Ranking
- Product Stock Risk Report
- Review & Rating Analysis
- Buyer Behavior Report (multi-threaded processing)

**Implementation Highlights:**
- Native SQL & JPQL
- Interface-based projections
- Time-range filtering (no unnecessary grouping)
- Pagination where applicable
- Optimized aggregation queries

---

## 🧠 Design Principles

- **Domain-Driven Naming**
    - `Review` instead of `ProductVariantRating`
- **Bidirectional Relationship Control**
    - Prevent infinite JSON recursion using `@JsonIgnore`
- **Performance-Oriented Design**
    - Lazy loading
    - Avoid unnecessary entity fetching
- **Clean API Design**
    - DTO usage where appropriate
- **Database Integrity**
    - Unique constraints
    - Proper indexing strategy

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- RESTful API
- Jackson (JSON Serialization)

---

## 📂 Main Entities

| Entity | Description |
|--------|-------------|
| User | Marketplace user |
| Seller | Seller account |
| Product | Product managed by seller |
| ProductVariant | SKU/variant of product with stock & price |
| Cart | User cart |
| CartItem | Items inside cart |
| Order | Order created from cart |
| OrderItem | Items inside order |
| Payment | Payment record for orders |
| Review | User review & rating for product variant |

---

## 🎯 Learning Objectives

This project demonstrates:
- Real-world backend system design
- Writing efficient reporting queries
- Handling transactional consistency
- Designing scalable domain models
- Understanding ORM pitfalls (N+1, infinite loop)
- Applying best practices used in production systems

---

## 🚀 Future Enhancements
- JWT Authentication & Authorization
- Soft delete for reviews & orders
- Caching for heavy reports
- Event-driven payment processing
- Elasticsearch for product search

---

## 🧑‍💻 Author
**Backend Developer**  
Focused on Java, Spring Boot, and enterprise system design.

---