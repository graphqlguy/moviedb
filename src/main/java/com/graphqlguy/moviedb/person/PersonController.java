package com.graphqlguy.moviedb.person;

import com.graphqlguy.moviedb.config.LatencySimulator;
import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieCast;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final LatencySimulator latencySimulator;

    @QueryMapping
    List<Person> people() {
        return personService.getAllPeople();
    }

    @BatchMapping
    Map<Movie, List<Person>> directors(List<Movie> movies) {
        latencySimulator.pause();
        log.info("Batch fetching directors for {} movies", movies.size());
        List<Long> movieIds = movies.stream().map(Movie::getId).toList();
        Map<Long, List<Person>> directorsByMovieId = personService.findDirectorsByMovieIds(movieIds);
        return movies.stream()
                .collect(Collectors.toMap(movie -> movie,
                        movie -> directorsByMovieId.getOrDefault(movie.getId(), List.of())));
    }

    @MutationMapping
    Person createPerson(@Argument @Valid CreatePersonInput input) {
        return personService.createPerson(input);
    }

    @MutationMapping
    Person updatePerson(@Argument UpdatePersonInput input) {
        return personService.updatePerson(input);
    }

    @MutationMapping
    DeletePersonResponse deletePerson(@Argument Long id) {
        return personService.delete(id);
    }


    @BatchMapping
    Map<Movie, List<MovieCast>> cast(List<Movie> movies) {
        latencySimulator.pause();
        log.info("Batch fetching cast for {} movies", movies.size());
        List<Long> movieIds = movies.stream().map(Movie::getId).toList();
        Map<Long, List<MovieCast>> castByMovieId = personService.findCastByMovieIds(movieIds);
        return movies.stream()
                .collect(Collectors.toMap(movie -> movie,
                        movie -> castByMovieId.getOrDefault(movie.getId(), List.of())));
    }
}
