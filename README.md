# PingX - Backend API Service

PingX is a Twitter/X-inspired social media backend built with Java 17, Jersey (JAX-RS), PostgreSQL, and Grizzly HTTP Server. It provides RESTful APIs for authentication, user profiles, activity feeds, tweets, retweets, quote tweets, comments/replies, notifications, and media uploads.

---

## Live Deployments

| Service | Platform | URL / Base Endpoint |
| :--- | :--- | :--- |
| **Backend API Service** | Railway | [https://pingx-backend-production.up.railway.app](https://pingx-backend-production.up.railway.app) |

### Connected Services
- **PingX Frontend:** [https://pingx-sanjaii04.vercel.app](https://pingx-sanjaii04.vercel.app)
- **Auth Microservice:** [https://auth-service-production-4ccd.up.railway.app](https://auth-service-production-4ccd.up.railway.app)

---

## Features

### Authentication & Security
- `Authorization: Bearer <token>` header validation for protected endpoints.
- Integration with external Auth Service for JWT issuance & token refresh.
- Jersey `@Secured` annotation & container request filter.

### User & Profile Management
- User profile registration (`POST /api/users/`) & update (`PUT /api/users/update`).
- Fetch user profiles by username with follower, following, and tweet counts (`GET /api/users/{username}`).
- Search users (`GET /api/users/search?q=query`).
- Suggested users recommendations (`GET /api/users/suggesstions`).
- Follow & Unfollow users (`POST /api/users/{userId}/follow`, `DELETE /api/users/{userId}/unfollow`, `GET /api/users/{userId}/is-following`).

### Activity Feed (Home & User Feed)
- Activity-based feed architecture returning original tweets, retweets, and quote tweets ordered by activity timestamp.
- High-performance `limit` & `offset` pagination (`GET /api/feed/?limit=20&offset=0` and `GET /api/feed/user/{userId}?limit=20&offset=0`).

### Tweet System
- Post original tweets and quote tweets (`POST /api/tweets/`).
- View individual tweet details (`GET /api/tweets/{tweetId}`).
- Fetch user tweets (`GET /api/users/{userId}/tweets`).
- Like & Unlike tweets (`POST /api/tweets/{tweetId}/like`, `DELETE /api/tweets/{tweetId}/like`).
- Retweet & Undo Retweet (`POST /api/tweets/{tweetId}/retweet`, `DELETE /api/tweets/{tweetId}/retweet`).
- Recursive quote tweet rendering up to configured depth limit (`MAX_QUOTE_DEPTH`).

### Comments & Replies
- Post replies to tweets (`POST /api/tweets/{tweetId}/reply`).
- Fetch replies for a tweet (`GET /api/tweets/{tweetId}/replies`).

### Notification System
- Real-time event notifications generated for **Follows**, **Likes**, **Retweets**, **Quotes**, and **Replies**.
- Fetch user notifications (`GET /api/notifications/`).
- Mark all notifications as read (`POST /api/notifications/read`).
- Unread notification count badge (`GET /api/notifications/unread-count`).

### Media & Cloud Storage
- Multipart image uploads integrated with Cloudinary (`POST /api/users/upload-image`).

### Database & Performance
- PostgreSQL relational schema.
- JDBC Repositories using HikariCP high-performance connection pooling.

---

## Technology Stack

- **Language:** Java 17
- **REST Framework:** Jersey (JAX-RS) 3.x
- **HTTP Server:** Grizzly Server
- **Database:** PostgreSQL
- **Connection Pool:** HikariCP
- **Cloud Storage:** Cloudinary SDK
- **Dependency Management:** Maven

---

## Architecture & Layering

```text
Client (React Frontend on Vercel)
       │
       ▼
Resource Layer (JAX-RS Controllers)
       │
       ▼
Service Layer (Business Logic & Notifications)
       │
       ▼
Repository Layer (HikariCP + JDBC)
       │
       ▼
PostgreSQL Database
```

---

## API Endpoints Reference

### Users & Profiles (`/api/users`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users/` | Save initial user profile | Yes |
| `PUT` | `/api/users/update` | Update profile (displayName, bio, image) | Yes |
| `GET` | `/api/users/{username}` | Fetch user profile details by username | No |
| `GET` | `/api/users/{userId}/tweets` | Fetch tweets posted by user | Yes |
| `POST` | `/api/users/{userId}/follow` | Follow a user | Yes |
| `DELETE` | `/api/users/{userId}/unfollow` | Unfollow a user | Yes |
| `GET` | `/api/users/{userId}/is-following` | Check if logged in user follows target user | Yes |
| `GET` | `/api/users/search?q={query}` | Search users by username or display name | Yes |
| `GET` | `/api/users/suggesstions` | Get suggested users to follow | Yes |
| `POST` | `/api/users/upload-image` | Upload profile/tweet image (Multipart) | Yes |

### Feed (`/api/feed`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/feed/?limit=20&offset=0` | Fetch home activity feed (paginated) | Yes |
| `GET` | `/api/feed/user/{userId}?limit=20&offset=0` | Fetch user activity feed (paginated) | Yes |

### Tweets (`/api/tweets`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/tweets/` | Create tweet or quote tweet | Yes |
| `GET` | `/api/tweets/{tweetId}` | Get single tweet details | Yes |
| `POST` | `/api/tweets/{tweetId}/like` | Like a tweet | Yes |
| `DELETE` | `/api/tweets/{tweetId}/like` | Unlike a tweet | Yes |
| `POST` | `/api/tweets/{tweetId}/retweet` | Retweet a tweet | Yes |
| `DELETE` | `/api/tweets/{tweetId}/retweet` | Undo retweet | Yes |
| `POST` | `/api/tweets/{tweetId}/reply` | Post a reply/comment to a tweet | Yes |
| `GET` | `/api/tweets/{tweetId}/replies` | Get replies for a tweet | Yes |

### Notifications (`/api/notifications`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/notifications/` | Fetch recipient's notifications | Yes |
| `POST` | `/api/notifications/read` | Mark all notifications as read | Yes |
| `GET` | `/api/notifications/unread-count` | Get unread notification badge count | Yes |

---

## Environment Variables (`.env`)

Create a `.env` file in the root directory:

```env
PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/pingx
DATABASE_USER=postgres
DATABASE_PASSWORD=your_password

JWT_SECRET=your_jwt_secret_key
CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
MAX_QUOTE_DEPTH=3
```

---

## Getting Started

### Build
```bash
mvn clean package
```

### Run
```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```

The service will start at `http://localhost:8080/api`.
