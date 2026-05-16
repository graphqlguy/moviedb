# MovieDB - Relationships & Nested Resolution

This is the companion code for **Class 3: Relationships & Nested Resolution** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/relationships-nested-resolution/) masterclass.

## What You'll Learn

In this class you model a relationship that carries its own data and see how GraphQL resolves nested queries field by field. It covers:

- Why some relationships need a junction entity instead of `@ManyToMany`
- Building a `MovieCast` entity that links actors to movies with character names
- How GraphQL resolves nested queries as a tree (root → children → grandchildren)
- Adding search with Spring Data derived query methods (`findByTitleContainingIgnoreCase`)
- Auto-resolution: when Spring GraphQL uses a getter and when you need `@SchemaMapping`
- Null propagation: how a single non-null field error can bubble up an entire response

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)
- Class 2 completed ([`class_2` branch](https://github.com/graphqlguy/moviedb/tree/class_2))

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Example Queries

**Get a movie with its full cast and character names:**

```graphql
query {
  movie(id: 1) {
    title
    directors {
      name
    }
    cast {
      characterName
      person {
        name
        nationality
      }
    }
  }
}
```

**Search movies by partial, case-insensitive title:**

```graphql
query {
  searchMovies(title: "god") {
    title
    releaseYear
    genre
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

This is part 3 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
