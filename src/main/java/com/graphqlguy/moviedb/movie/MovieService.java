package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;


    List<Movie> findAll() {
        return movieRepository.findAll();
    }

    Movie findById(final Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie", id));
    }

    List<Movie> findByTitleContainingIgnoreCase(final String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    DeleteMovieResponse deleteMovie(final Long id) {
        log.info("Delete movie with id {}", id);
        if (!movieRepository.existsById(id)) {
            return new DeleteMovieResponse(false, "Movie not found for Id = " + id, null);
        }

        movieRepository.deleteById(id);
        return new DeleteMovieResponse(true, "Movie deleted successfully", id);
    }

    List<Movie> findByIds(final List<Long> ids) {
        return movieRepository.findAllById(ids);
    }

}
