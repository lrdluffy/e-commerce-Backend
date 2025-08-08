# 🛒 E-commerce Backend

A secure and scalable backend application for an e-commerce platform, built using **Spring Boot**. It provides RESTful APIs for product management, user authentication, order processing, and more. Designed with modularity, security, and maintainability in mind.

---

## 🚀 Features

- 🔐 **JWT-based Authentication & Authorization**
- 📦 Product & Order Management APIs
- 🧾 Validation & Exception Handling
- 🧰 Spring Data JPA with H2 (dev)
- 📖 OpenAPI (Swagger UI) documentation
- 🛡️ Spring Security integration
- ✨ ModelMapper for DTO mapping

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Web, Spring Security, Spring Data JPA**
- **H2 Database (for development)**
- **Lombok**
- **JWT (io.jsonwebtoken)**
- **ModelMapper**
- **OpenAPI / Swagger UI**

---

## 📂 Project Structure

src  
├── main  
│ ├── java  
│ │ └── com.strawhats.ecommercebackend  
│ └── resources  
│ ├── application.properties  
├── test  


---

## 🧪 Running the Project

### Prerequisites

- Java 21
- Maven

### Run Locally

```bash
git clone https://github.com/yourusername/ecommerce-backend.git
cd ecommerce-backend
mvn spring-boot:run
```

### Access Swagger UI

url: `http://localhost:8080/swagger-ui.html`

## Authentication

- Uses **JWT** for stateless authentication.
- Login endpoint returns a JWT token.
- Secure endpoints require `Authorization: Bearer <token>` header.

## ⚙️ Configuration

You can configure properties in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:ecommerce
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
jwt.secret=your_secret_key
```

## 🧑‍💻 Developer Info

- **Group ID**: `com.strawhats`
- **Artifact ID**: `ecommerceBackend`
- **Version**: `0.0.1-SNAPSHOT`

## 📃 License

This project is open-source and available under the [MIT License](./LICENSE).
