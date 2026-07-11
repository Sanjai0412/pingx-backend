# PingX - Backend API Service

This is the backend API service for the PingX microblogging platform. It is built using Java, JAX-RS (Jersey RESTful Web Services), Grizzly HTTP server, and PostgreSQL. It manages user authentication, user profiles, following relationships, and tweet operations.

## Features

- RESTful API Endpoints: Implements resource controller layers for user account management, profiles, follower directory, and tweets.
- User Authentication: JWT-based stateless authentication filters verifying requests.
- Database Management: Direct PostgreSQL integration utilizing HikariCP for high-performance connection pooling.
- Cloud Media Storage: Integrated with Cloudinary for handling and serving user profile avatars and tweet attachments.
- Multipart Form Uploads: Uses Jersey Media Multipart to process image uploads.

## Technology Stack

- Language: Java (JDK 17 or higher)
- Web Server: Grizzly HTTP Server
- REST Engine: Jersey JAX-RS (Jakarta REST)
- Database: PostgreSQL
- Connection Pooling: HikariCP
- Security Tokens: JSON Web Tokens (JJWT)
- Cloud Storage: Cloudinary HTTP Client
- Dependency Management: Maven

## Project Structure

```text
pingx-backend/
├── pom.xml                 # Maven dependency and build config
├── .env                    # Local environment variables
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    ├── App.java            # Server bootstrap entrypoint
                    ├── config/             # CORS and JWT security filter configurations
                    ├── controller/         # JAX-RS resource controller classes
                    ├── dto/                # Data Transfer Objects for request/response serialization
                    ├── exception/          # Custom JAX-RS exception mappers
                    ├── model/              # Database entity models
                    ├── repository/         # Database access layers (JDBC and SQL queries)
                    └── service/            # Business logic service implementations
```

## Getting Started

### 1. Prerequisites
- Java Development Kit (JDK 17 or higher)
- Apache Maven
- Running PostgreSQL database instance

### 2. Database Setup
Create a PostgreSQL database (e.g. named `pingx`) and configure the schema. The application manages tables for:
- users
- profiles
- tweets
- follows

### 3. Environment Configuration
Create a `.env` file in the root directory:
```env
PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/pingx
DATABASE_USER=your_postgres_user
DATABASE_PASSWORD=your_postgres_password
JWT_SECRET=your_base64_or_hex_jwt_secret
CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
```

### 4. Build and Run Server
Build the jar package:
```bash
mvn clean package
```

Run the Grizzly application server:
```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```
The API server will listen on `http://localhost:8080/api`.
