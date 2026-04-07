# MovieDB - Schema Design & Relationships

This is the companion code for **Class 2: Schema Design & Relationships** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/schema-design-fundamentals/) masterclass.

## What You'll Learn

In this class you add JPA persistence, enums, and your first entity relationship. It covers:

- GraphQL enums and how they map to Java enums
- Setting up JPA with an in-memory H2 database
- Modeling a many-to-many relationship (movies have directors)
- Resolving related types with `@SchemaMapping`
- Seeding initial data with a `CommandLineRunner`

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)
- Class 1 completed ([`class_1` branch](https://github.com/graphqlguy/moviedb/tree/class_1))

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Example Queries

**Get all movies with genre:**

```graphql
query {
  movies {
    title
    genre
  }
}
```

**Get a movie with its directors:**

```graphql
query {
  movie(id: 6) {
    title
    genre
    rating
    directors {
      name
      nationality
    }
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

This is part 2 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
