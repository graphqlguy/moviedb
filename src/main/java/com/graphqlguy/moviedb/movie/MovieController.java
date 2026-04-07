package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;

    @QueryMapping
    List<Movie> movies() {
        return movieRepository.findAll();
    }

    @QueryMapping
    Movie movie(@Argument Long id) {
        return movieRepository.findById(id).get();
    }

    @SchemaMapping
    List<Person> directors(Movie movie) {
        log.info("Fetching Directors for movie {}", movie.getTitle());
        return movie.getDirectors();
    }
}
