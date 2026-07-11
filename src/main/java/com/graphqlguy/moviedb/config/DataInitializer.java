package com.graphqlguy.moviedb.config;

import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieCast;
import com.graphqlguy.moviedb.movie.MovieCastRepository;
import com.graphqlguy.moviedb.movie.MovieRepository;
import com.graphqlguy.moviedb.person.Person;
import com.graphqlguy.moviedb.person.PersonRepository;
import com.graphqlguy.moviedb.shared.Genre;
import com.graphqlguy.moviedb.user.AppUser;
import com.graphqlguy.moviedb.user.Role;
import com.graphqlguy.moviedb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MovieCastRepository movieCastRepository;

    @Override
    public void run(String... args) {

        // At the top of run() method:
        userRepository.save(AppUser.builder()
                .username("admin").email("admin@moviedb.com")
                .password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());
        userRepository.save(AppUser.builder()
                .username("user").email("user@moviedb.com")
                .password(passwordEncoder.encode("user123")).role(Role.USER).build());

        // Directors
        Person frankDarabont      = createAndSavePerson("Frank Darabont", 1959, "Hungarian-American");
        Person francisFordCoppola = createAndSavePerson("Francis Ford Coppola", 1939, "American");
        Person christopherNolan   = createAndSavePerson("Christopher Nolan", 1970, "British-American");
        Person robertZemeckis     = createAndSavePerson("Robert Zemeckis", 1952, "American");
        Person sidneyLumet        = createAndSavePerson("Sidney Lumet", 1924, "American");
        Person martinScorsese     = createAndSavePerson("Martin Scorsese", 1942, "American");
        Person davidFincher       = createAndSavePerson("David Fincher", 1962, "American");
        Person sergioLeone        = createAndSavePerson("Sergio Leone", 1929, "Italian");
        Person jamesCameron       = createAndSavePerson("James Cameron", 1954, "Canadian");
        Person stanleyKubrick     = createAndSavePerson("Stanley Kubrick", 1928, "American");
        Person clintEastwood      = createAndSavePerson("Clint Eastwood", 1930, "American");

        // Actors
        Person morganFreeman        = createAndSavePerson("Morgan Freeman", 1937, "American");
        Person timRobbins           = createAndSavePerson("Tim Robbins", 1958, "American");
        Person marlonBrando         = createAndSavePerson("Marlon Brando", 1924, "American");
        Person alPacino             = createAndSavePerson("Al Pacino", 1940, "American");
        Person leonardoDiCaprio     = createAndSavePerson("Leonardo DiCaprio", 1974, "American");
        Person tomHanks             = createAndSavePerson("Tom Hanks", 1956, "American");
        Person henryFonda           = createAndSavePerson("Henry Fonda", 1905, "American");
        Person arnoldSchwarzenegger = createAndSavePerson("Arnold Schwarzenegger", 1947, "Austrian-American");
        Person jackNicholson        = createAndSavePerson("Jack Nicholson", 1937, "American");
        Person bradPitt             = createAndSavePerson("Brad Pitt", 1963, "American");
        Person rayLiotta            = createAndSavePerson("Ray Liotta", 1954, "American");
        Person robertDeNiro         = createAndSavePerson("Robert De Niro", 1943, "American");

        // Movies with cast
        Movie shawshank = createAndSaveMovie("The Shawshank Redemption", 1994, Genre.DRAMA, 9.3, 278, List.of(frankDarabont));
        createAndSaveCastEntry(shawshank, timRobbins, "Andy Dufresne");
        createAndSaveCastEntry(shawshank, morganFreeman, "Red");

        Movie godfather = createAndSaveMovie("The Godfather", 1972, Genre.CRIME, 9.2, 238, List.of(francisFordCoppola));
        createAndSaveCastEntry(godfather, marlonBrando, "Don Vito Corleone");
        createAndSaveCastEntry(godfather, alPacino, "Michael Corleone");

        Movie godfather2 = createAndSaveMovie("The Godfather Part II", 1974, Genre.CRIME, 9.0, 240, List.of(francisFordCoppola));
        createAndSaveCastEntry(godfather2, alPacino, "Michael Corleone");
        createAndSaveCastEntry(godfather2, robertDeNiro, "Young Vito Corleone");

        Movie forrest = createAndSaveMovie("Forrest Gump", 1994, Genre.DRAMA, 8.8, 13, List.of(robertZemeckis));
        createAndSaveCastEntry(forrest, tomHanks, "Forrest Gump");

        Movie angryMen = createAndSaveMovie("12 Angry Men", 1957, Genre.DRAMA, 9.0, 389, List.of(sidneyLumet));
        createAndSaveCastEntry(angryMen, henryFonda, "Juror 8");

        Movie inception = createAndSaveMovie("Inception", 2010, Genre.SCIFI, 8.8, 27205, List.of(christopherNolan));
        createAndSaveCastEntry(inception, leonardoDiCaprio, "Dom Cobb");

        Movie interstellar = createAndSaveMovie("Interstellar", 2014, Genre.SCIFI, 8.6, 157336, List.of(christopherNolan));
        createAndSaveCastEntry(interstellar, leonardoDiCaprio, "Cooper");

        Movie darkKnight = createAndSaveMovie("The Dark Knight", 2008, Genre.ACTION, 9.0, 155, List.of(christopherNolan));
        createAndSaveCastEntry(darkKnight, leonardoDiCaprio, "Bruce Wayne");

        Movie goodfellas = createAndSaveMovie("Goodfellas", 1990, Genre.CRIME, 8.7, 769, List.of(martinScorsese));
        createAndSaveCastEntry(goodfellas, rayLiotta, "Henry Hill");
        createAndSaveCastEntry(goodfellas, robertDeNiro, "James Conway");

        Movie se7en = createAndSaveMovie("Se7en", 1995, Genre.THRILLER, 8.6, 807, List.of(davidFincher));
        createAndSaveCastEntry(se7en, bradPitt, "Detective David Mills");
        createAndSaveCastEntry(se7en, morganFreeman, "Detective William Somerset");

        Movie goodBadUgly = createAndSaveMovie("The Good, the Bad and the Ugly", 1966, Genre.WESTERN, 8.8, 429, List.of(sergioLeone));
        createAndSaveCastEntry(goodBadUgly, clintEastwood, "Blondie");

        Movie t2 = createAndSaveMovie("Terminator 2: Judgment Day", 1991, Genre.SCIFI, 8.6, 280, List.of(jamesCameron));
        createAndSaveCastEntry(t2, arnoldSchwarzenegger, "The Terminator");

        Movie shining = createAndSaveMovie("The Shining", 1980, Genre.HORROR, 8.4, 694, List.of(stanleyKubrick));
        createAndSaveCastEntry(shining, jackNicholson, "Jack Torrance");

        Movie unforgiven = createAndSaveMovie("Unforgiven", 1992, Genre.WESTERN, 8.2, 33, List.of(clintEastwood));
        createAndSaveCastEntry(unforgiven, clintEastwood, "William Munny");
        createAndSaveCastEntry(unforgiven, morganFreeman, "Ned Logan");
    }

    private Person createAndSavePerson(String name, int birthYear, String nationality) {
        return personRepository.save(Person.builder().name(name).birthYear(birthYear).nationality(nationality).build());
    }

    private Movie createAndSaveMovie(String title, int year, Genre genre, double rating, Integer tmdbId, List<Person> directors) {
        Movie movie = Movie.builder()
                .title(title).releaseYear(year).genre(genre).rating(rating).tmdbId(tmdbId)
                .build();
        movie.getDirectors().addAll(directors);
        return movieRepository.save(movie);
    }

    private void createAndSaveCastEntry(Movie movie, Person person, String characterName) {
        movieCastRepository.save(MovieCast.builder().movie(movie).person(person).characterName(characterName).build());
    }
}