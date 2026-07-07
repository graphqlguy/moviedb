package com.graphqlguy.moviedb.person;

import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.exception.InvalidInputException;
import com.graphqlguy.moviedb.movie.MovieCastRepository;
import com.graphqlguy.moviedb.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final MovieCastRepository movieCastRepository;
    private final MovieRepository movieRepository;

    List<Person> getAllPeople() {
        log.debug("Getting all people");
        return personRepository.findAll();
    }

    Person createPerson(final CreatePersonInput input) {
        log.debug("Creating person {}", input);
        return personRepository.save(Person.builder()
                .name(input.name())
                .birthYear(input.birthYear())
                .nationality(input.nationality())
                .build());
    }

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
        applyIfPresent(input.nationality(), person::setNationality);

        return personRepository.save(person);
    }

    private <T> void applyIfPresent(final ArgumentValue<T> arg, final Consumer<T> setter) {
        if (arg.isPresent()) {
            setter.accept(arg.value());
        }
    }

    DeletePersonResponse delete(final Long id) {
        log.debug("Deleting person {}", id);
        final Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException("Person", id);
        }

        final Person person = personOptional.get();
        if (movieCastRepository.existsByPerson(person) || movieRepository.existsByDirectorsContaining(person)) {
            return new DeletePersonResponse(false, DeletePersonError.LINKED_TO_MOVIE, null);
        }

        personRepository.delete(person);
        return new DeletePersonResponse(true, null, id);


    }
}
