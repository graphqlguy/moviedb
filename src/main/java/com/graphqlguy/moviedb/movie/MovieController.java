package com.graphqlguy.moviedb.movie;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MovieController {

    // Sample data - we'll use a database in later classes
    private final List<Movie> movies = List.of(
            new Movie(1L, "The Shawshank Redemption", 1994, "Drama"),
            new Movie(2L, "The Godfather", 1972, "Crime"),
            new Movie(3L, "The Dark Knight", 2008, "Action"),
            new Movie(4L, "Pulp Fiction", 1994, "Crime"),
            new Movie(5L, "Forrest Gump", 1994, "Drama")
    );

    @QueryMapping
    String hello() {
        return "Hello World!";
    }

    @QueryMapping
    List<Movie> movies() {
        return movies;
    }

    @QueryMapping
    Movie movie(@Argument Long id) {
        return movies.stream().filter(movie -> movie.getId().equals(id)).findFirst().orElse(null);
    }

}
