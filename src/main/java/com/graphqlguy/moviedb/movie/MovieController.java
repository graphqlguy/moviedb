package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @QueryMapping
    List<Movie> movies() {
        return movieService.findAll();
    }

    @QueryMapping
    Movie movie(@Argument Long id) {
        return movieService.findById(id);
    }

    @QueryMapping
    List<Movie> searchMovies(@Argument String title) {
        return movieService.findByTitleContainingIgnoreCase(title);
    }

    @MutationMapping
    DeleteMovieResponse deleteMovie(@Argument Long id) {
        return movieService.deleteMovie(id);
    }

    @SchemaMapping
    List<Person> directors(Movie movie) {
        log.info("Fetching Directors for movie {}", movie.getTitle());
        return movie.getDirectors();
    }

    @SchemaMapping
    List<MovieCast> cast(Movie movie) {
        return movie.getCast();
    }

}
