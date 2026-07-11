package com.graphqlguy.moviedb.person;

import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.exception.InvalidInputException;
import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieCast;
import com.graphqlguy.moviedb.movie.MovieCastRepository;
import com.graphqlguy.moviedb.movie.MovieRepository;
import com.graphqlguy.moviedb.tvshow.TvShow;
import com.graphqlguy.moviedb.tvshow.TvShowCast;
import com.graphqlguy.moviedb.tvshow.TvShowCastRepository;
import com.graphqlguy.moviedb.tvshow.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final MovieCastRepository movieCastRepository;
    private final MovieRepository movieRepository;
    private final TvShowCastRepository tvShowCastRepository;
    private final TvShowRepository tvShowRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    Person createPerson(final CreatePersonInput input) {
        log.debug("Creating person {}", input);
        return personRepository.save(Person.builder()
                .name(input.name())
                .birthYear(input.birthYear())
                .countryCode(input.countryCode())
                .build());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    Person updatePerson(final UpdatePersonInput input) {
        log.debug("Updating person {}", input);

        if (!input.name().isOmitted() && StringUtils.isBlank(input.name().value())) {
            throw new InvalidInputException("name", "Name can't be blank");
        }

        final Optional<Person> personOptional = personRepository.findById(input.id());
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException("Person",  input.id());
        }

        final Person person = personOptional.get();
        applyIfPresent(input.name(), person::setName);
        applyIfPresent(input.birthYear(), person::setBirthYear);
        applyIfPresent(input.countryCode(), person::setCountryCode);

        return personRepository.save(person);
    }

    private <T> void applyIfPresent(final ArgumentValue<T> arg, final Consumer<T> setter) {
        if (arg.isPresent()) {
            setter.accept(arg.value());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    DeletePersonResponse delete(final Long id, final boolean force) {
        log.debug("Deleting person {} (force={})", id, force);
        final Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException("Person", id);
        }

        final Person person = personOptional.get();
        if (force) {
            unlinkCredits(person);
        } else {
            if (movieCastRepository.existsByPerson(person) || movieRepository.existsByDirectorsContaining(person)) {
                return new DeletePersonResponse(false, DeletePersonError.LINKED_TO_MOVIE, null);
            }
            if (tvShowCastRepository.existsByPersonId(person.getId()) || tvShowRepository.existsByCreatorsContaining(person)) {
                return new DeletePersonResponse(false, DeletePersonError.LINKED_TO_TV_SHOW, null);
            }
        }

        personRepository.delete(person);
        return new DeletePersonResponse(true, null, id);
    }

    // Every FK reference to the person must go before the row can be deleted:
    // cast credits are owned rows (deleted), director/creator links are join-table
    // entries removed via the owning side's collection.
    private void unlinkCredits(final Person person) {
        movieCastRepository.deleteByPerson(person);
        tvShowCastRepository.deleteByPersonId(person.getId());
        movieRepository.findByDirectorsContaining(person)
                .forEach(movie -> movie.getDirectors().remove(person));
        tvShowRepository.findByCreatorsContaining(person)
                .forEach(show -> show.getCreators().remove(person));
    }

    public Map<Long, List<Person>> findDirectorsByMovieIds(final List<Long> movieIds) {
        return movieRepository.findAllWithDirectorsByIdIn(movieIds).stream()
                .collect(Collectors.toMap(Movie::getId, Movie::getDirectors));
    }

    public Map<Long, List<MovieCast>> findCastByMovieIds(final List<Long> movieIds) {
        return movieCastRepository.findWithPersonByMovieIdIn(movieIds).stream()
                .collect(Collectors.groupingBy(movieCast -> movieCast.getMovie().getId()));
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public PersonPage findAll(int page, int size) {
        Page<Person> result = personRepository.findAll(
                PageRequest.of(page, size, Sort.by("name").ascending().and(Sort.by("id"))));
        return new PersonPage(result.getContent(), result.getTotalElements(),
                result.getTotalPages(), result.getNumber(), result.getSize());
    }

    public List<Person> searchByName(String name) {
        return personRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Movie> findDirectedMovies(Person person) {
        return movieRepository.findByDirectorsContaining(person);
    }

    public List<TvShow> findCreatedShows(Person person) {
        return tvShowRepository.findByCreatorsContaining(person);
    }

    public List<MovieCast> findMovieCastCredits(Long personId) {
        return movieCastRepository.findWithMovieByPersonId(personId);
    }

    public List<TvShowCast> findTvShowCastCredits(Long personId) {
        return tvShowCastRepository.findWithTvShowByPersonId(personId);
    }

}
