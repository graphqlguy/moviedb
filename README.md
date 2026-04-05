# MovieDB - Your First GraphQL Service

This is the companion code for **Class 1: Your First GraphQL Service** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/your-first-graphql-service/) masterclass.

## What You'll Learn

In this class you build a minimal Spring Boot GraphQL API that serves movie data from an in-memory list. It covers:

- Setting up a Spring Boot project with GraphQL dependencies
- Defining a GraphQL schema
- Writing query resolvers with `@QueryMapping` and `@Argument`
- Using GraphiQL to test queries interactively

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Example Queries

**Get all movies:**

```graphql
query {
  movies {
    id
    title
    releaseYear
    genre
  }
}
```

**Get a movie by ID:**

```graphql
query {
  movie(id: 1) {
    title
    releaseYear
  }
}
```

**Hello world:**

```graphql
query {
  hello
}
```

## Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.0.5 |
| Java | 25 |
| Build Tool | Maven |

## Course

This is part 1 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
