
---

# 📦 Order Management System: Seller Dashboard

![img_2.png](img_2.png)

### **The Enterprise Standard for Clean Code**

This isn't just a simple CRUD app. This project is a high-performance **Seller Dashboard** engineered with a strict **Hexagonal Architecture**. By decoupling the core business logic from technical details like MySQL and Thymeleaf, we’ve built a system that is as scalable as it is maintainable.

---

## 📑 Quick Navigation

* [🚀 Features & Intent](https://www.google.com/search?q=%23-introduction)
* [🧠 The Domain Heart](https://www.google.com/search?q=%23-domain-layer-aggregates--value-objects)
* [🔄 The Three-Model Flow](https://www.google.com/search?q=%23-the-three-model-enterprise-flow)
* [⚡ CQRS Implementation](https://www.google.com/search?q=%23-implementation-of-cqrs)
* [🏗️ Design Patterns](https://www.google.com/search?q=%23-strategic-design-patterns)
* [🛠️ Getting Started](https://www.google.com/search?q=%23-how-to-run)

---

## 1. Introduction 🚀

In a real enterprise environment, the "How" (Business Rules) should never be tangled with the "Where" (Database/UI).

* **Decoupled Logic:** Whether we use **MySQL**, PostgreSQL, or even an Excel sheet, the core logic of processing an order remains untouched.
* **Seller-Centric:** Designed specifically for fundamental order fulfillment and real-time tracking.

---

## 2. Domain Layer: Aggregates & Value Objects 🧠

We use **Domain-Driven Design (DDD)** to ensure your data is always consistent and valid.

* **Order (Aggregate Root) 👑:** The entry point for all order logic. It guards the lifecycle of an order (e.g., ensuring an order can't be "Delivered" before it's "Paid").
* **OrderItem (Value Object) 📦:** Immutable units representing the products. They don't have their own ID; they exist only as part of the Order.
* **OrderFactory 🏗️:** Enforces strict business rules during creation, ensuring every order starts with a unique UUID and a "Pending" status.

---

## 3. The Three-Model Enterprise Flow 🔄

To prevent "leaky abstractions," we maintain three distinct versions of our data. This ensures the UI never dictates how the database is structured.

1. **Domain Model (`Order.java`)**: The source of truth. Pure Java, zero dependencies.
2. **Persistence Model (`OrderEntity.java`)**: The database specialist. Maps our domain logic to **MySQL** via JPA.
3. **Presentation Model (`OrderResponse.java`)**: The UI specialist. A **DTO** that flattens data for a lightning-fast dashboard experience.

---
![img_3.png](img_3.png)

## 4. Implementation of CQRS ⚡

We separate **Writes** from **Reads** to maximize efficiency.

| Feature | Commands (Write) ✍️ | Queries (Read) 📖 |
| --- | --- | --- |
| **Primary Goal** | Data Integrity | High-Speed Display |
| **Logic Type** | Business Validation | Data Transformation |
| **Main Model** | Domain Aggregate | Optimized DTO |

---

## 5. Strategic Design Patterns 🏗️

* **Use Case Pattern:** Every action (like `UpdateStatusHandler`) is a standalone unit. This makes testing incredibly easy.
* **Repository Pattern:** A "gatekeeper" that hides the complexity of MySQL from the rest of the app.
* **DTO Pattern:** Prevents security risks by only sending the UI exactly what it needs—nothing more, nothing less.

---

## 6. How to Run 🛠️

### **1. Prepare the Database**

Run this in your MySQL instance:

```sql
CREATE DATABASE order_mgmt_db;

```

### **2. Configure Environment**

Set these variables to connect:

* `DB_USERNAME`: *your_username*
* `DB_PASSWORD`: *your_password*

### **3. Launch the App**

```bash
./mvnw spring-boot:run

```

✨ **Dashboard Live at:** `http://localhost:8080`

---