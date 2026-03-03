# RevShop – Full Stack E-Commerce Web Application

## Project Overview
RevShop is a full-stack monolithic e-commerce web application developed using Spring Boot and Thymeleaf.  
It supports role-based access for Buyers and Sellers with secure authentication using JWT.

The application allows:
- Buyers to browse products, manage cart, and place orders.
- Sellers to manage inventory and track orders.
- Secure login and registration system.

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate
- Oracle Database

### Frontend
- Thymeleaf
- HTML5
- CSS3

### Tools
- IntelliJ IDEA
- Maven
- Git & GitHub

---

## Security Features
- JWT-based Authentication
- BCrypt Password Encryption
- Role-Based Authorization
- Input Validation
- Global Exception Handling

---

## Database Design

### Main Entities
- User
- Seller
- Product
- Cart
- Order
- OrderItem
- Review
- Address
- Payment

---

## Setup Instructions

### 1 Clone the Repository

```bash
git clone https://github.com/Jagan2001-J/RevShop_P2_Jagan.git
```
### 2 Configure Database

Update application.properties:
```
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=your_username
spring.datasource.password=your_password
```
### 3 Run the Application
```
mvn spring-boot:run
```
Or run the main class from IntelliJ.

## Author
**Palle Jagan Mohan Reddy**  
GitHub: https://github.com/Jagan2001-J