package com.graphqlguy.moviedb.person;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @QueryMapping
    List<Person> people() {
        return personService.getAllPeople();
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
}
