# Twitter API

A REST API for a Twitter-style social application. It is built with Java 21 and
Spring Boot 4, using Spring Data JPA and PostgreSQL for persistence.

## Features

- Register and authenticate users
- Create, list, update, and delete tweets
- Create, list, update, and delete comments
- Like or dislike tweets and retrieve the authenticated user's reaction
- Retweet tweets and list the authenticated user's retweets
- Validate request bodies and return structured API errors
- Stateless HTTP Basic authentication for protected endpoints

## Requirements

- Java 21
- PostgreSQL

The Maven Wrapper is included, so Maven does not need to be installed
separately.

## Database setup

The defaults in `src/main/resources/application.properties` connect to a local
PostgreSQL server at `localhost:5432`, database `postgres`, as user `postgres`.
The application uses the `twitter_api` schema and Hibernate's `update` mode.
Ensure PostgreSQL is running, the database exists, and the configured user has
access. Update the datasource settings in `application.properties` (or
override them with Spring Boot environment variables such as
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and
`SPRING_DATASOURCE_PASSWORD`) to match your local setup. Do not commit real
database credentials.

For a local database, the schema can be created before the first run with:

```sql
CREATE SCHEMA IF NOT EXISTS twitter_api;
```

Hibernate is configured with `ddl-auto=update`; it updates the mapped tables
when the application starts. This is convenient for development, but production
deployments should use a deliberate schema migration strategy instead.

To supply the database password in PowerShell without putting it in the
repository:

```powershell
$env:SPRING_DATASOURCE_PASSWORD = "your-local-database-password"
```

The variable applies to the current terminal session. Set the datasource URL
and username the same way if your local PostgreSQL configuration differs from
the defaults.

## Run the application

From the project root on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The server listens on port `9000` with the `/twitter` context path. The API base
URL is:

```text
http://localhost:9000/twitter
```

Stop the server with `Ctrl+C` in the terminal running Maven. To build an
executable JAR instead of running through the Maven plugin:

```powershell
.\mvnw.cmd clean package
java -jar .\target\TwitterApi-0.0.1-SNAPSHOT.jar
```

Use `./mvnw clean package` and `java -jar target/TwitterApi-0.0.1-SNAPSHOT.jar`
on macOS or Linux.

## Authentication

Registration and login are public. All other API endpoints require HTTP Basic
authentication using the registered username and password. Requests are
stateless: the login endpoint validates credentials and returns the user
profile; it does not issue a session cookie or access token. Send credentials
with each protected request, for example:

```bash
curl -u username:password http://localhost:9000/twitter/tweet
```

Passwords are stored using BCrypt. The configured CORS origins are
`http://localhost:5173` and `http://localhost:3000`.

The API uses Basic authentication rather than issuing a token. For local
development, `curl -u username:password` adds the required authorization
header. Do not send Basic credentials over an unencrypted connection in a
production environment; deploy behind HTTPS and configure production CORS
origins and credentials appropriately.

## API endpoints

All paths below are relative to `/twitter`. Unless marked **Public**, send
HTTP Basic credentials. JSON request bodies use `Content-Type: application/json`.
Query parameters are shown in parentheses. Collection endpoints return JSON
arrays; individual resources return JSON objects unless a response is
explicitly described as empty.

### Authentication

| Method | Path | Access | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Register a user; returns the user profile |
| `POST` | `/auth/login` | Public | Validate credentials; returns the user profile |

Registration body:

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "username": "ada",
  "email": "ada@example.com",
  "password": "change-me"
}
```

`firstName` is optional. `lastName` is required; `username` must be 3–50
characters; `email` must be a valid email address; and `password` must be
6–100 characters. Login accepts `username` and `password`.

Example registration and login requests:

```bash
curl -i -X POST http://localhost:9000/twitter/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ada","lastName":"Lovelace","username":"ada","email":"ada@example.com","password":"change-me"}'

curl -i -X POST http://localhost:9000/twitter/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ada","password":"change-me"}'
```

Both endpoints return a user profile with `id`, `username`, and `email`.
Registration responds with `201 Created`; login responds with `200 OK`.
Successful login does not return a token.

### Tweets

| Method | Path | Description |
|---|---|---|
| `POST` | `/tweet` | Create a tweet |
| `GET` | `/tweet` | List tweets, newest first |
| `GET` | `/tweet/findByUserId?userId={id}` | List a user's tweets, newest first |
| `GET` | `/tweet/findById?id={id}` | Get a tweet by ID |
| `PUT` | `/tweet/{id}` | Update a tweet |
| `DELETE` | `/tweet/{id}` | Delete a tweet; returns `204 No Content` |

Create and update requests use `{"content": "..."}`. Tweet content is required
and limited to 200 characters.

Example authenticated requests:

```bash
curl -i -u ada:change-me http://localhost:9000/twitter/tweet

curl -i -u ada:change-me -X POST http://localhost:9000/twitter/tweet \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello, Twitter API!"}'
```

Tweet responses include the tweet `id`, `content`, `createdAt`, author
`userId` and `userName`, plus `likeCount`, `dislikeCount`, `retweetCount`, and
`commentCount`.

### Comments

| Method | Path | Description |
|---|---|---|
| `POST` | `/comment?tweetId={id}` | Add a comment to a tweet |
| `GET` | `/comment/findByTweetId?tweetId={id}` | List comments for a tweet, newest first |
| `GET` | `/comment/findById?id={id}` | Get a comment by ID |
| `PUT` | `/comment/{id}` | Update a comment |
| `DELETE` | `/comment/{id}` | Delete a comment; returns `204 No Content` |

Create and update requests use `{"content": "..."}`. Comment content is
required and limited to 280 characters.

The create endpoint takes `tweetId` as a query parameter and the comment text
in the JSON body:

```bash
curl -i -u ada:change-me -X POST "http://localhost:9000/twitter/comment?tweetId=1" \
  -H "Content-Type: application/json" \
  -d '{"content":"Great post!"}'
```

Comment responses include `id`, `content`, `createdAt`, `tweetId`, and
`username`.

### Reactions

| Method | Path | Description |
|---|---|---|
| `POST` | `/like?tweetId={id}` | Like a tweet (toggles the reaction) |
| `POST` | `/dislike?tweetId={id}` | Dislike a tweet (toggles the reaction) |
| `GET` | `/reaction?tweetId={id}` | Get the authenticated user's reaction |

Like and dislike requests take the tweet ID as a query parameter. A reaction
response contains `tweetId` and `reactionType` (`LIKE` or `DISLIKE`).

### Retweets

| Method | Path | Description |
|---|---|---|
| `POST` | `/retweet` | Retweet or toggle the current user's retweet |
| `GET` | `/retweet/mine` | List the authenticated user's retweets |
| `DELETE` | `/retweet/{id}` | Toggle the authenticated user's retweet for the tweet ID |

The `POST /retweet` request body is `{"id": 123}`, where `id` is the tweet ID.
The response contains the retweet `id`, `tweetId`, `username`, and
`createdAt`. `POST /retweet` responds with `201 Created`; the delete route
currently toggles the retweet and returns a response body.

## Typical workflow

1. Register with `POST /auth/register`.
2. Use the registered username and password as HTTP Basic credentials.
3. Create and browse tweets with `/tweet`.
4. Add comments with `/comment?tweetId={id}` and react using `/like` or
   `/dislike`.
5. Retweet with `POST /retweet` and review your retweets at `/retweet/mine`.

IDs in path segments and query parameters are numeric database IDs. The API
does not provide pagination parameters; list endpoints return the full
matching collection.

## Errors

Application errors are returned as JSON with a status code, message, and
timestamp. Unauthenticated requests to protected endpoints return HTTP `401`.
For example, an application error has this shape:

```json
{
  "status": 404,
  "message": "Resource not found",
  "timestamp": "2026-01-01T12:00:00"
}
```

The message and status depend on the error; validation and unexpected errors
may be handled differently. Do not rely on the example message or timestamp.

## Run tests

On Windows:

```powershell
.\mvnw.cmd test
```

On macOS or Linux:

```bash
./mvnw test
```

Tests require the same dependencies as the application, including an available
PostgreSQL database when the Spring application context is started. The project
contains application-context, controller, service, and repository test classes.

## Project structure

```text
src/
├── main/
│   ├── java/com/workintech/TwitterApi/
│   │   ├── config/       Security configuration
│   │   ├── controller/   REST controllers
│   │   ├── dto/          Validated request and response objects
│   │   ├── entity/       JPA entities and reaction types
│   │   ├── exception/    API exceptions and error handling
│   │   ├── repository/   Data access
│   │   └── service/      Application services
│   └── resources/        Application configuration
└── test/                 Application and component tests
```

## Technology stack

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA and Hibernate
- Spring Security
- Jakarta Bean Validation
- PostgreSQL JDBC driver
- Maven Wrapper
- Lombok
