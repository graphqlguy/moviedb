# MovieDB - Mutations & Input Validation

This is the companion code for **Class 4: Mutations & Input Validation** from the [Spring GraphQL Tutorial](https://graphqlguy.com/docs/tutorial-SpringGraphQL/mutations-input-validation/) masterclass.

## What You'll Learn

In this class you move from reading data to changing it, and learn how to keep bad input out. It covers:

- Defining a `type Mutation` and wiring resolvers with `@MutationMapping`
- Modeling write operations with `input` types (`CreatePersonInput`, `UpdatePersonInput`)
- Declarative validation with Jakarta Bean Validation (`@NotBlank`, `@Size`, `@Min`) plus `@Valid`
- Partial updates with Spring GraphQL's `ArgumentValue<T>` — telling an *omitted* field apart from an explicit `null`
- A reusable `applyIfPresent` helper that updates only the fields the client actually sent
- Returning structured response payloads (`DeletePersonResponse`) instead of bare scalars
- Signalling bad input with a custom `InvalidInputException` that carries the offending field
- Extracting a service layer (`MovieService` / `PersonService`) to keep controllers thin

## Prerequisites

- Java 25+
- Maven 3.6+ (or use the included Maven wrapper)
- Class 3 completed ([`class_3` branch](https://github.com/graphqlguy/moviedb/tree/class_3))

## Running the Application

```bash
./mvnw spring-boot:run
```

Once running, open [http://localhost:8080/graphiql](http://localhost:8080/graphiql) to access the GraphiQL IDE.

## Example Mutations

**Create a person (validated input):**

```graphql
mutation {
  createPerson(input: { name: "Greta Gerwig", birthYear: 1983, nationality: "American" }) {
    id
    name
    birthYear
    nationality
  }
}
```

**Partially update a person — only the fields you send are changed:**

```graphql
mutation {
  updatePerson(input: { id: 1, nationality: "British" }) {
    id
    name
    nationality
  }
}
```

**Delete a person (structured response payload):**

```graphql
mutation {
  deletePerson(id: 1) {
    success
    message
    deletedId
  }
}
```

## Example Query

**List all people:**

```graphql
query {
  people {
    id
    name
    nationality
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

This is part 4 of a 13-part masterclass. See the [full course](https://graphqlguy.com/docs/category/spring-graphql-tutorial) for all classes.
