# MovieDB - Security & Authentication

This is the companion code for **Class 6: Security & Authentication** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/security-authentication/) masterclass.

## What You'll Learn

In this class you learn how to lock down a GraphQL API — authenticating users with JSON Web Tokens (JWT) and authorizing individual operations by role. It covers:

- Configuring **stateless** Spring Security (`SecurityFilterChain`, `SessionCreationPolicy.STATELESS`) for a token-based API
- Issuing signed JWTs on `login` with the `jjwt` library — HMAC-SHA signing, a `role` claim, and an expiry
- A custom `JwtAuthFilter` (`OncePerRequestFilter`) that reads the `Authorization: Bearer …` header and populates the `SecurityContext`
- Hashing and verifying passwords with `BCryptPasswordEncoder`
- Method-level authorization via `@EnableMethodSecurity` and `@PreAuthorize("hasRole('ADMIN')")` on the mutating service methods
- Exposing authentication through the schema — a `login` mutation returning an `AuthResponse` (`token` + `User`)
- Mapping Spring Security's `AccessDeniedException` onto a GraphQL `FORBIDDEN` error
- Seeding users with BCrypt-encoded passwords at startup

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)
- Class 5 completed ([`class_5` branch](https://github.com/graphqlguy/moviedb/tree/class_5))

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Seed Users

Two users are created at startup:

| Username | Password   | Role    |
|----------|------------|---------|
| `admin`  | `admin123` | `ADMIN` |
| `user`   | `user123`  | `USER`  |

## Authenticating

**1. Log in to obtain a JWT:**

```graphql
mutation {
  login(input: { username: "admin", password: "admin123" }) {
    token
    user {
      id
      username
      email
      role
    }
  }
}
```

**2. Send the token on subsequent requests.** In GraphiQL, open the **Headers** panel and add:

```json
{
  "Authorization": "Bearer <paste-your-token-here>"
}
```

## Role-Based Authorization

Mutating operations are restricted to `ADMIN`. The mutation below succeeds with an admin token but returns a `FORBIDDEN` error for a `USER` token or an unauthenticated request:

```graphql
mutation {
  deletePerson(id: 1) {
    success
    error
    deletedId
  }
}
```

An unauthorized caller receives:

> You are not authorized to perform this action

## H2 Console

Visit [http://localhost:8080/h2-console](http://localhost:8080/h2-console) to browse the database.

- **JDBC URL:** `jdbc:h2:mem:moviedb`
- **Username:** `sa`
- **Password:** *(empty)*

## Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.0.5 |
| Spring Security | 7.0.4 |
| JWT (jjwt) | 0.13.0 |
| Java | 25 |
| Build Tool | Maven |
| Database | H2 (in-memory) |

## Course

This is part 6 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
