package com.graphqlguy.moviedb.config;

import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieRepository;
import com.graphqlguy.moviedb.person.Person;
import com.graphqlguy.moviedb.person.PersonRepository;
import com.graphqlguy.moviedb.shared.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;

    @Override
    public void run(String... args) {
        // Directors
        Person darabont  = createAndSavePerson("Frank Darabont", 1959, "Hungarian-American");
        Person coppola   = createAndSavePerson("Francis Ford Coppola", 1939, "American");
        Person nolan     = createAndSavePerson("Christopher Nolan", 1970, "British-American");
        Person zemeckis  = createAndSavePerson("Robert Zemeckis", 1952, "American");
        Person lumet     = createAndSavePerson("Sidney Lumet", 1924, "American");
        Person scorsese  = createAndSavePerson("Martin Scorsese", 1942, "American");
        Person fincher   = createAndSavePerson("David Fincher", 1962, "American");
        Person sergiL    = createAndSavePerson("Sergio Leone", 1929, "Italian");
        Person cameron   = createAndSavePerson("James Cameron", 1954, "Canadian");
        Person kubrick   = createAndSavePerson("Stanley Kubrick", 1928, "American");

        // Movies
        createAndSaveMovie("The Shawshank Redemption", 1994, Genre.DRAMA, 9.3, List.of(darabont));
        createAndSaveMovie("The Godfather", 1972, Genre.CRIME, 9.2, List.of(coppola));
        createAndSaveMovie("The Godfather Part II", 1974, Genre.CRIME, 9.0, List.of(coppola));
        createAndSaveMovie("Forrest Gump", 1994, Genre.DRAMA, 8.8, List.of(zemeckis));
        createAndSaveMovie("12 Angry Men", 1957, Genre.DRAMA, 9.0, List.of(lumet));
        createAndSaveMovie("Inception", 2010, Genre.SCIFI, 8.8, List.of(nolan));
        createAndSaveMovie("Interstellar", 2014, Genre.SCIFI, 8.6, List.of(nolan));
        createAndSaveMovie("The Dark Knight", 2008, Genre.ACTION, 9.0, List.of(nolan));
        createAndSaveMovie("Goodfellas", 1990, Genre.CRIME, 8.7, List.of(scorsese));
        createAndSaveMovie("Se7en", 1995, Genre.THRILLER, 8.6, List.of(fincher));
        createAndSaveMovie("The Good, the Bad and the Ugly", 1966, Genre.WESTERN, 8.8, List.of(sergiL));
        createAndSaveMovie("Terminator 2: Judgment Day", 1991, Genre.SCIFI, 8.6, List.of(cameron));
        createAndSaveMovie("The Shining", 1980, Genre.HORROR, 8.4, List.of(kubrick));
    }

    private Person createAndSavePerson(String name, int birthYear, String nationality) {
        return personRepository.save(
                Person.builder()
                        .name(name)
                        .birthYear(birthYear)
                        .nationality(nationality)
                        .build()
        );
    }

    private void createAndSaveMovie(String title, int year, Genre genre, double rating, List<Person> directors) {
        Movie movie = Movie.builder()
                .title(title)
                .releaseYear(year)
                .genre(genre)
                .rating(rating)
                .build();
        movie.getDirectors().addAll(directors);
        movieRepository.save(movie);
    }
}
