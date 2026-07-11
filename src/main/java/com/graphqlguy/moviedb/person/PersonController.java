package com.graphqlguy.moviedb.person;

import com.graphqlguy.moviedb.config.LatencySimulator;
import com.graphqlguy.moviedb.country.Country;
import com.graphqlguy.moviedb.country.CountryService;
import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieCast;
import com.graphqlguy.moviedb.tvshow.TvShow;
import com.graphqlguy.moviedb.tvshow.TvShowCast;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final CountryService countryService;
    private final LatencySimulator latencySimulator;

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

    @QueryMapping
    Person person(@Argument Long id) {
        return personService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person", id));
    }

    @QueryMapping
    PersonPage people(@Argument Integer page, @Argument Integer size) {
        return personService.findAll(page != null ? page : 0, size != null ? size : 20);
    }

    @QueryMapping
    List<Person> searchPeople(@Argument String name) {
        return personService.searchByName(name);
    }

    @SchemaMapping(typeName = "Person")
    List<Movie> directedMovies(Person person) {
        return personService.findDirectedMovies(person);
    }

    @SchemaMapping(typeName = "Person")
    List<TvShow> createdShows(Person person) {
        return personService.findCreatedShows(person);
    }

    @SchemaMapping(typeName = "Person")
    Country country(Person person) {
        if (person.getCountryCode() == null) {
            return null;
        }
        try {
            return countryService.findByCode(person.getCountryCode());
        } catch (Exception e) {
            // The external countries API being down should not break person queries
            log.warn("Could not resolve country {} from external API: {}", person.getCountryCode(), e.getMessage());
            return null;
        }
    }

    @SchemaMapping(typeName = "Person")
    List<MovieCast> movieCastCredits(Person person) {
        return personService.findMovieCastCredits(person.getId());
    }

    @SchemaMapping(typeName = "Person")
    List<TvShowCast> tvShowCastCredits(Person person) {
        return personService.findTvShowCastCredits(person.getId());
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
