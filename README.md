# PingX - Backend API Service

PingX is a Twitter/X-inspired social media backend built with Java, Jersey (JAX-RS), PostgreSQL, and Grizzly HTTP Server. It provides RESTful APIs for authentication, user management, activity feeds, tweets, reposts, quote tweets, comments, and social interactions.

---

## Features

### Authentication & Security
- JWT-based stateless authentication
- Request authorization using Jersey filters
- Protected API endpoints

### User Management
- User registration and login
- User profile management
- Follow and unfollow users
- User search
- Profile information with followers/following counts

### Tweet System
- Create tweets
- Delete tweets
- View tweet details
- Like and unlike tweets
- Quote tweets (recursive rendering)
- Reposts (Retweets)
- View original tweet support

### Home Feed
- Activity-based timeline
- Displays:
  - Original tweets
  - Reposts
  - Quote tweets
- Feed ordered by activity timestamp
- Pagination using LIMIT/OFFSET

### Comments
- Create comments
- Fetch comments for a tweet
- Nested comment structure ready for replies
- Comment like infrastructure

### Media
- Cloudinary integration
- Profile image uploads
- Tweet image uploads
- Multipart file handling

### Database
- PostgreSQL
- JDBC repositories
- HikariCP connection pooling

---

# Technology Stack

- Language: Java 17
- REST Framework: Jersey (JAX-RS)
- HTTP Server: Grizzly
- Database: PostgreSQL
- Connection Pool: HikariCP
- Authentication: JWT (JJWT)
- Cloud Storage: Cloudinary
- Dependency Management: Maven

---

# Architecture

The project follows a layered architecture.

```text
Client
      │
      ▼
Resource Layer (Controllers)
      │
      ▼
Service Layer
      │
      ▼
Repository Layer
      │
      ▼
PostgreSQL
```

---

# Project Structure

```text
pingx-backend/
├── pom.xml
├── .env
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    ├── App.java
                    ├── config/
                    ├── controller/
                    │     ├── UserResource
                    │     ├── TweetResource
                    │     ├── FeedResource
                    │     └── CommentResource
                    ├── dto/
                    ├── exception/
                    ├── model/
                    ├── repository/
                    └── service/
```

---

# API Modules

- Authentication
- Users
- Profiles
- Feed
- Tweets
- Likes
- Reposts
- Comments
- Follow System
- Media Uploads

---

# Feed Architecture

PingX uses an activity-based feed rather than returning only tweets.

```text
FeedResponse
│
├── performedBy
├── activityType
├── activityAt
└── TweetResponse
        │
        ├── author
        ├── content
        ├── quotedTweet
        ├── likes
        └── reposts
```

This allows the feed to display:

- Original Tweets
- Reposts
- Quote Tweets

without modifying the tweet model.

---

# Database Tables

Current schema includes:

- users
- followers
- tweets
- likes
- retweets
- comments
- comment_likes

---

# Getting Started

## Prerequisites

- Java 17+
- Maven
- PostgreSQL

---

## Environment Variables

Create a `.env` file.

```env
PORT=8080

DATABASE_URL=jdbc:postgresql://localhost:5432/pingx
DATABASE_USER=your_postgres_user
DATABASE_PASSWORD=your_postgres_password

JWT_SECRET=your_secret

CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
```

---

## Build

```bash
mvn clean package
```

---

## Run

```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```

Server starts at

```
http://localhost:8080/api
```

---

# Current Features

- JWT Authentication
- User Profiles
- Follow / Unfollow
- Tweet Creation
- Tweet Deletion
- Like / Unlike Tweets
- Quote Tweets
- Reposts
- Recursive Quote Rendering
- Activity Feed
- Tweet Details
- Comment System (v1)
- Image Uploads
- Pagination (LIMIT/OFFSET)

---

# Planned Features

- Reply Threads
- Notifications
- Infinite Scrolling (Cursor Pagination)
- Bookmarks
- User Profile Tabs
- Trending Feed
- Direct Messaging