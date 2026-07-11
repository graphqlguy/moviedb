package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.shared.SortOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

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

    public List<Movie> searchByTitle(final String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public MoviePage findMovies(MovieFilter filter, int page, int size, MovieSort sort) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sort));
        Page<Movie> result = movieRepository.findWithFilters(
                filter != null ? filter.genre() : null,
                filter != null ? filter.minRating() : null,
                filter != null ? filter.maxRating() : null,
                filter != null ? filter.minYear() : null,
                filter != null ? filter.maxYear() : null,
                filter != null ? filter.titleContains() : null,
                pageable
        );
        return new MoviePage(
                result.getContent(), result.getTotalElements(), result.getTotalPages(),
                result.getNumber(), result.getSize(), result.isFirst(), result.isLast(),
                result.hasNext(), result.hasPrevious()
        );
    }

    private Sort buildSort(MovieSort sort) {
        if (sort == null || sort.field() == null) return Sort.by("releaseYear").descending();
        String field = switch (sort.field()) {
            case TITLE -> "title";
            case RELEASE_YEAR -> "releaseYear";
            case RATING -> "rating";
            case RUNTIME -> "runtime";
        };
        return sort.order() == SortOrder.ASC ? Sort.by(field).ascending() : Sort.by(field).descending();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    Movie createMovie(final CreateMovieInput input) {
        log.info("Creating movie {}", input.title());
        return movieRepository.save(Movie.builder()
                .title(input.title()).releaseYear(input.releaseYear()).genre(input.genre())
                .rating(input.rating()).runtime(input.runtime()).plot(input.plot())
                .posterUrl(input.posterUrl()).tmdbId(input.tmdbId())
                .build());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    Movie updateMovie(final UpdateMovieInput input) {
        log.info("Updating movie {}", input.id());
        final Movie movie = movieRepository.findById(input.id())
                .orElseThrow(() -> new EntityNotFoundException("Movie", input.id()));

        applyIfPresent(input.title(), movie::setTitle);
        applyIfPresent(input.releaseYear(), movie::setReleaseYear);
        applyIfPresent(input.genre(), movie::setGenre);
        applyIfPresent(input.rating(), movie::setRating);
        applyIfPresent(input.runtime(), movie::setRuntime);
        applyIfPresent(input.plot(), movie::setPlot);
        applyIfPresent(input.posterUrl(), movie::setPosterUrl);
        applyIfPresent(input.tmdbId(), movie::setTmdbId);

        return movieRepository.save(movie);
    }

    private <T> void applyIfPresent(final ArgumentValue<T> arg, final Consumer<T> setter) {
        if (arg.isPresent()) {
            setter.accept(arg.value());
        }
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
