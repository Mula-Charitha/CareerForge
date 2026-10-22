# CareerForge - Placement Preparation & Tracking Platform

CareerForge is a comprehensive placement preparation and tracking platform designed for college students to monitor DSA preparation, aptitude practice, resume versions, job applications, and mock interview performance.

---

## 🏗️ Architecture Overview

```
React + Vite (Frontend)  ---> REST API (JSON) ---> Spring Boot (Backend) ---> MySQL Database
```

- **Frontend**: React.js, Vite, JavaScript, React Router, Axios, Custom CSS Design System
- **Backend**: Java 17+, Spring Boot 3.4.3, Spring Web, Spring Data JPA, Spring Security, MySQL Driver, Maven
- **Port Defaults**:
  - Frontend: `http://localhost:5173`
  - Backend: `http://localhost:8080`

---

## 📋 Prerequisites & Required Environment

- **Java Development Kit (JDK)**: Java 17 or higher (Java 25 verified compatible)
- **Apache Maven**: 3.8+ (or included `./mvnw` wrapper)
- **Node.js**: v18.0.0 or higher (v24.12.0 verified compatible)
- **npm**: v9.0.0 or higher (v11.6.2 verified compatible)
- **MySQL Database**: MySQL 8.0+

---

## ⚙️ Database Configuration

CareerForge Backend uses environment variables with fallback defaults in `application.properties`:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `DB_URL` | JDBC Connection String | `jdbc:mysql://localhost:3306/careerforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USERNAME` | MySQL Username | `root` |
| `DB_PASSWORD` | MySQL Password | *(empty)* |

To override database settings, set the environment variables in your terminal before launching the backend:

**PowerShell (Windows)**:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/careerforge_db?createDatabaseIfNotExist=true"
$env:DB_USERNAME="your_mysql_user"
$env:DB_PASSWORD="your_mysql_password"
```

**Bash / Linux / macOS**:
```bash
export DB_URL="jdbc:mysql://localhost:3306/careerforge_db?createDatabaseIfNotExist=true"
export DB_USERNAME="your_mysql_user"
export DB_PASSWORD="your_mysql_password"
```

---

## 🚀 Running the Project

### 1. Start the Backend Service (Spring Boot)

```powershell
cd CareerForge-Backend
mvn spring-boot:run
```

Or using Maven wrapper:
```powershell
.\mvnw spring-boot:run
```

The Spring Boot backend will start on **`http://localhost:8080`**.

### 2. Verify Backend Health Endpoint

Open your browser or run:
```bash
curl http://localhost:8080/api/health
```

Expected Response:
```json
{
  "status": "UP",
  "message": "CareerForge Backend is running"
}
```

### 3. Start the Frontend Application (React + Vite)

Open a new terminal window and run:

```powershell
cd CareerForge-Frontend
npm install
npm run dev
```

The React Vite dev server will run on **`http://localhost:5173`**.

---

## 📁 Project Directory Structure

```
CareerForge/
├── CareerForge-Backend/           # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/careerforge/careerforge/
│   │   │   │   ├── config/        # Security & CORS configuration
│   │   │   │   ├── controller/    # Health REST endpoints
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── entity/        # JPA Database Entities
│   │   │   │   ├── repository/    # Spring Data JPA Repositories
│   │   │   │   ├── service/       # Business Logic Services
│   │   │   │   ├── exception/     # Global Exception Handling
│   │   │   │   └── CareerForgeApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
│
├── CareerForge-Frontend/          # React + Vite UI
│   ├── src/
│   │   ├── assets/                # Static assets & graphics
│   │   ├── components/            # Header, Footer, HealthStatusBanner
│   │   ├── pages/                 # LandingPage
│   │   ├── layouts/               # MainLayout
│   │   ├── services/              # Axios API client (api.js)
│   │   ├── context/               # Auth & App Contexts
│   │   ├── hooks/                 # Custom React hooks
│   │   ├── utils/                 # Utility helper functions
│   │   ├── App.jsx                # React Router setup
│   │   ├── main.jsx               # Entry mount point
│   │   └── index.css              # Custom design system & theme
│   ├── package.json
│   └── vite.config.js
└── README.md
```
