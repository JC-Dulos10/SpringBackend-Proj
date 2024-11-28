# 🌟 **Spring Boot Backend API** 🌟

Welcome to the **Spring Boot Backend API**, a personal project developed to demonstrate a robust backend system with authentication, role-based access control, and seamless database integration. This API is designed for learning and showcasing best practices in **Spring Boot**, **JWT Authentication**, and **MySQL**.

---

### **📝 Project Details**

This project serves as a learning platform and a showcase of my skills in developing secure and scalable backend systems. It features:
- **Authentication and Authorization**: Secured using **JWT**.
- **Role-Based Access Control**: Differentiates functionality for **admin** and **regular users**.
- **Product Management**: Create, update, delete, and view products (restricted by roles).
- **Database Integration**: Powered by **MySQL**, running on a Docker container for easy scalability.
- **CORS Configuration**: Configured to allow communication with frontend applications.

**🚀 Deployment:** The API is deployed on [Render](https://render.com), a free-tier cloud hosting platform.

---

### **⚠️ Important Note**
The API may take **a few seconds to load for the first request** after a period of inactivity. This is due to the free-tier deployment on Render, which puts the server to sleep when not in use. Please be patient while the server starts up.

**🌐 Base URL:**  
[`https://springbackend-proj.onrender.com`](https://springbackend-proj.onrender.com)

---

## 🚀 **API Endpoints Overview**

### 🛡️ **Authentication**

1. **🔐 Register a User**
    - **Endpoint:** `POST /api/v1/auth/register`
    - **Request Body:**
      ```json
      {
        "firstname": "John",
        "lastname": "Doe",
        "email": "john.doe@example.com",
        "password": "securePassword",
        "isAdmin": "false" // Set "true" for admin users
      }
      ```
    - **Response:**
      ```json
      {
        "token": "JWT_TOKEN"
      }
      ```

2. **🔑 Login**
    - **Endpoint:** `POST /api/v1/auth/authenticate`
    - **Request Body:**
      ```json
      {
        "email": "john.doe@example.com",
        "password": "securePassword"
      }
      ```
    - **Response:**
      ```json
      {
        "token": "JWT_TOKEN",
        "isAdmin": true // or false based on the user's role
      }
      ```

---

### 👤 **User Management**

1. **🔄 Change Password**
    - **Endpoint:** `POST /api/v1/users/change-password`
    - **Headers:**  
      `Authorization: Bearer JWT_TOKEN`
    - **Request Body:**
      ```json
      {
        "currentPassword": "oldPassword",
        "newPassword": "newSecurePassword",
        "confirmPassword": "newSecurePassword"
      }
      ```
    - **Response:**
        - `200 OK` if successful.

2. **👑 Admin-Only Actions**
    - **Endpoint:** Any `/api/v1/users/**` (e.g., `GET /api/v1/users/all`)
    - Requires Admin token.

---

### 📦 **Product Management**

1. **🆕 Create a Product** (Admin Only)
    - **Endpoint:** `POST /api/v1/products`
    - **Headers:**  
      `Authorization: Bearer JWT_TOKEN`
    - **Request Body:**
      ```json
      {
        "productName": "Sample Product",
        "productPrice": 99.99
      }
      ```
    - **Response:**
        - `201 Created` with product details.

2. **📖 Get All Products**
    - **Endpoint:** `GET /api/v1/products`
    - **Response:**
      ```json
      [
        {
          "productId": 1,
          "productName": "Sample Product",
          "productPrice": 99.99
        }
      ]
      ```

3. **✏️ Update a Product** (Admin Only)
    - **Endpoint:** `PUT /api/v1/products/{id}`
    - **Headers:**  
      `Authorization: Bearer JWT_TOKEN`
    - **Request Body:**
      ```json
      {
        "productName": "Updated Product Name",
        "productPrice": 79.99
      }
      ```
    - **Response:**
        - Updated product details.

4. **🗑️ Delete a Product** (Admin Only)
    - **Endpoint:** `DELETE /api/v1/products/{id}`
    - **Headers:**  
      `Authorization: Bearer JWT_TOKEN`
    - **Response:**
        - `204 No Content` if successful.

---

## 🛠️ **How to Test with Postman**

1. **✨ Import Endpoints:** Copy the API endpoints into Postman or create requests manually as described above.
2. **🔐 Authentication:**
    - Use the `POST /api/v1/auth/authenticate` endpoint to obtain your `JWT_TOKEN`.
    - For protected endpoints, add the token to the `Authorization` header:
      ```
      Authorization: Bearer YOUR_JWT_TOKEN
      ```
3. **🌐 Base URL:** Prefix all endpoints with:  
   `https://springbackend-proj.onrender.com`
4. **📤 Test Requests:** Use the sample request bodies and headers provided above.

---

## 💡 **Notes**

- Only admin users can perform **create**, **update**, and **delete** actions for products.
- Ensure proper authentication by including the token in the `Authorization` header for protected endpoints.
- First-time requests may take a few seconds due to server startup on free-tier hosting.

---

Thank you for exploring the **Spring Boot Backend API**! 🚀