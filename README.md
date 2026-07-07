# MovieDB - Error Handling

This is the companion code for **Class 5: Error Handling** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/error-handling/) masterclass.

## What You'll Learn

In this class you learn how a GraphQL service reports failure — turning exceptions into meaningful errors and telling *exceptional* problems apart from *expected* business outcomes. It covers:

- Centralizing error handling with `@ControllerAdvice` and `@GraphQlExceptionHandler`
- A custom exception hierarchy in a dedicated `exception` package (`EntityNotFoundException`, `InvalidInputException`)
- Mapping domain exceptions onto GraphQL `ErrorType`s (`NOT_FOUND`, `BAD_REQUEST`, `INTERNAL_ERROR`)
- Building errors with `GraphqlErrorBuilder.newError(env)` — and why every error needs a `message`
- Enriching errors with `extensions` metadata (`entityType`, `field`) so clients can react programmatically
- Hiding internals behind a catch-all handler that logs the stack trace and returns only a traceable `reference` id
- Returning **partial results** — data *and* errors in one response — with `DataFetcherResult` (`moviesByIds`)
- Modeling *expected* failures as typed schema enums (`DeletePersonError`) instead of throwing

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)
- Class 4 completed ([`class_4` branch](https://github.com/graphqlguy/moviedb/tree/class_4))

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Example Queries

**A missing entity produces a `NOT_FOUND` error with an `entityType` extension:**

```graphql
query {
  movie(id: 999999) {
    id
    title
  }
}
```

**Partial results — valid movies come back as data while missing ids are reported as errors:**

```graphql
query {
  moviesByIds(ids: [1, 2, 999999]) {
    id
    title
  }
}
```

## Example Mutations

**Invalid input is rejected with a `BAD_REQUEST` error carrying the offending `field`:**

```graphql
mutation {
  updatePerson(input: { id: 1, name: "" }) {
    id
    name
  }
}
```

**Delete a person — expected outcomes are modeled as a typed `error` enum, not a thrown exception:**

```graphql
mutation {
  deletePerson(id: 1) {
    success
    error
    deletedId
  }
}
```

## H2 Console

Visit [http://localhost:8080/h2-console](http://localhost:8080/h2-console) to browse the database.

- **JDBC URL:** `jdbc:h2:mem:moviedb`
- **Username:** `sa`
- **Password:** *(empty)*

## Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.0.5 |
| Java | 25 |
| Build Tool | Maven |
| Database | H2 (in-memory) |

## Course

This is part 5 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
