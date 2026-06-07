package com.graphqlguy.moviedb.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;


    List<Movie> findAll() {
        return movieRepository.findAll();
    }

    Movie findById(final Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    List<Movie> findByTitleContainingIgnoreCase(final String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    DeleteMovieResponse deleteMovie(final Long id) {
        log.info("Delete movie with id {}", id);
        if (!movieRepository.existsById(id)) {
            return new DeleteMovieResponse(false, "Movie not found for Id = " + id, null);
        }

        movieRepository.deleteById(id);
        return new DeleteMovieResponse(true, "Movie deleted successfully", id);
    }
}
