
-----

# 📦 Order Management System: Enterprise Edition

### **The Gold Standard for Clean Architecture & DDD**

This project is a high-performance **Seller & Student Marketplace** engineered with a strict **Hexagonal Architecture (Ports and Adapters)**. By decoupling the core business logic from technical details like MySQL, UUID generation, and Thymeleaf, we’ve built a system that is resilient, type-safe, and ready for production.

![img_4.png](img_4.png)

![img_5.png](img_5.png)
-----

## 📑 Strategic Navigation

* [🚀 The Evolution](https://www.google.com/search?q=%231-the-evolution-)
* [🧠 Domain Layer (The Heart)](https://www.google.com/search?q=%232-domain-layer-aggregates--value-objects-)
* [⚙️ Application Layer (The Orchestrator)](https://www.google.com/search?q=%233-application-layer-the-orchestrator-)
* [🔌 Infrastructure Layer (The Tools)](https://www.google.com/search?q=%234-infrastructure-layer-the-tools-)
* [🎨 Presentation Layer (The UI)](https://www.google.com/search?q=%235-presentation-layer-the-ui-)
* [🔄 The UUID & Triple-Model Flow](https://www.google.com/search?q=%236-the-id-strategy--triple-model-flow-)
* [⚡ CQRS Implementation](https://www.google.com/search?q=%237-implementation-of-cqrs-)
* [🛠️ Getting Started](https://www.google.com/search?q=%238-how-to-run-)

-----

## 1\. The Evolution 🚀

We transitioned this system through three major architectural milestones:

1.  **From Sequential IDs to UUIDs:** We moved away from predictable database IDs to **UUID (Universally Unique Identifiers)** for public-facing URLs to enhance security and system decoupling.
2.  **Strict Type Safety:** Resolved complex Java Type Mismatches between Web (String), Application (UUID), and Persistence (Long) layers.
3.  **Cross-Aggregate Mapping:** Implemented logic to resolve `Customer` names from `Order` ID references in the Application layer, keeping our database "Normalized" but our UI "Human-Readable."

-----

## 🧠 2. Domain Layer: Aggregates & Value Objects

We use **Domain-Driven Design (DDD)** to ensure data consistency.

* **Order (Aggregate Root) 👑:** Manages the entire lifecycle. It contains the logic for status transitions (PENDING → SHIPPED → DELIVERED).
* **Public ID (UUID) 🔑:** Every Order carries a `publicId`. This is what the outside world sees, while the database uses a hidden `Long` for performance.
* **OrderItem (Value Object) 📦:** Immutable units. They don't have their own identity; they are part of the Order's state.
* **OrderFactory 🏗️:** The "Birthplace" of orders. It ensures every new order is assigned a fresh UUID and starts in the correct state.

-----

## ⚙️ 3. Application Layer: The Orchestrator

This layer coordinates the **Use Cases**. It is the only place where the "Join" between Customers and Orders happens.

* **Command Handlers (Write Path):** (e.g., `PlaceOrderHandler`, `UpdateStatusHandler`). They convert UI Strings into Domain UUIDs and execute business logic.
* **Query Handlers (Read Path):** (e.g., `OrderQueryHandler`). These transform raw data into `OrderResponse` DTOs, fetching the **Customer Name** from the `CustomerRepository` so the seller sees "Eden Admasu" instead of "ID: 1."
* **Dependency Inversion:** This layer defines the **Repository Interfaces**. It doesn't care *how* MySQL works; it only cares that the data can be saved.

-----

## 🔌 4. Infrastructure Layer: The Tools

The "Heavy Lifting" layer where technology lives.

* **Persistence Model (`OrderEntity`):** Handles the JPA mapping to MySQL. It uses `@Column(unique = true)` for the UUID and `@Id` for the primary key.
* **Repository Implementation:** Implements the Domain interfaces. We added custom logic to `findByPublicId(UUID)` to bridge the gap between the UI and the Database.
* **Database Management:** Uses `spring.jpa.hibernate.ddl-auto=update` to manage schema changes like adding the new UUID columns without losing data.

-----

## 🎨 5. Presentation Layer: The UI

* **Modern Shop UI:** A Tailwind-powered student storefront featuring product categories, stock alerts (Red warnings for low stock), and a professional navigation bar.
* **Seller Dashboard:** A data-rich admin panel for managing order fulfillment.
* **Type-Safe Controllers:** All `@PathVariable` arguments are handled as `String` to support UUIDs, preventing "400 Bad Request" errors found in standard ID-based systems.

-----

## 🔄 6. The ID Strategy & Triple-Model Flow

To prevent "leaky abstractions," we maintain three distinct versions of an Order:

1.  **Domain Model (`Order.java`)**: Pure logic. Uses `UUID` for identity.
2.  **Persistence Model (`OrderEntity.java`)**: Database logic. Uses `Long` for indexing and `UUID` for searching.
3.  **Presentation Model (`OrderResponse.java`)**: UI logic. Flattens data and converts everything to `String` for the browser.

| Layer | ID Type | Purpose |
| :--- | :--- | :--- |
| **Database** | `Long` | Fast indexing & Primary Keys |
| **API / URL** | `UUID (String)` | Security & External Referencing |
| **Domain** | `UUID` | Business Identity |

-----

## ⚡ 7. Implementation of CQRS

| Feature | Commands (Write) ✍️ | Queries (Read) 📖 |
| :--- | :--- | :--- |
| **Primary Goal** | Data Integrity | High-Speed Display |
| **Logic** | Validates stock & status | Resolves Customer Names |
| **Result** | Saves to Repository | Returns DTOs |

-----

## 8\. How to Run 🛠️

### **1. MySQL Setup**

```sql
CREATE DATABASE order_mgmt_db;
```

### **2. Configuration**

Update `src/main/resources/application.properties`:

* `spring.datasource.username=root`
* `spring.datasource.password=your_password`

### **3. Run**

```bash
mvn spring-boot:run
```

✨ **Seller Admin:** `http://localhost:8080/`
✨ **Student Store:** `http://localhost:8080/store` (Wait, did you create the store mapping? I can help with that\!)

-----