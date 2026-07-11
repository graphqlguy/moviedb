package com.graphqlguy.moviedb.tvshow;

import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.movie.MovieService;
import com.graphqlguy.moviedb.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TvShowController {

    private final TvShowService tvShowService;
    private final MovieService movieService;   // used by the search query in Step 5
    private final TvShowRepository tvShowRepository;
    private final TvShowCastRepository tvShowCastRepository;
    private final EpisodeRepository episodeRepository;

    @QueryMapping
    TvShow tvShow(@Argument Long id) {
        return tvShowService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TvShow", id));
    }

    @QueryMapping
    TvShowPage tvShows(@Argument Integer page, @Argument Integer size) {
        return tvShowService.findAll(page != null ? page : 0, size != null ? size : 10);
    }

    @BatchMapping(typeName = "TvShow")
    Map<TvShow, Set<Person>> creators(List<TvShow> shows) {
        log.info("@BatchMapping: loading creators for {} TV shows", shows.size());
        Set<Long> ids = shows.stream().map(TvShow::getId).collect(Collectors.toSet());
        List<TvShow> withCreators = tvShowRepository.findWithCreatorsByIdIn(ids);

        Map<Long, Set<Person>> byShowId = withCreators.stream()
                .collect(Collectors.toMap(TvShow::getId, TvShow::getCreators));

        Map<TvShow, Set<Person>> result = new HashMap<>();
        for (TvShow show : shows) {
            result.put(show, byShowId.getOrDefault(show.getId(), Set.of()));
        }
        return result;
    }

    @BatchMapping(typeName = "TvShow")
    Map<TvShow, List<TvShowCast>> cast(List<TvShow> shows) {
        log.info("@BatchMapping: loading cast for {} TV shows", shows.size());
        Set<Long> ids = shows.stream().map(TvShow::getId).collect(Collectors.toSet());
        List<TvShowCast> allCast = tvShowCastRepository.findWithPersonByTvShowIdIn(ids);

        Map<Long, List<TvShowCast>> byShowId = allCast.stream()
                .collect(Collectors.groupingBy(c -> c.getTvShow().getId()));

        Map<TvShow, List<TvShowCast>> result = new HashMap<>();
        for (TvShow show : shows) {
            result.put(show, byShowId.getOrDefault(show.getId(), List.of()));
        }
        return result;
    }

    @BatchMapping(typeName = "TvShow")
    Map<TvShow, List<Episode>> episodes(List<TvShow> shows) {
        log.info("@BatchMapping: loading episodes for {} TV shows", shows.size());
        Set<Long> ids = shows.stream().map(TvShow::getId).collect(Collectors.toSet());
        List<Episode> allEpisodes = episodeRepository
                .findByTvShowIdInOrderBySeasonNumberAscEpisodeNumberAsc(ids);

        Map<Long, List<Episode>> byShowId = allEpisodes.stream()
                .collect(Collectors.groupingBy(e -> e.getTvShow().getId()));

        Map<TvShow, List<Episode>> result = new HashMap<>();
        for (TvShow show : shows) {
            result.put(show, byShowId.getOrDefault(show.getId(), List.of()));
        }
        return result;
    }

    @QueryMapping
    List<Object> search(@Argument String query) {
        List<Object> results = new ArrayList<>();
        results.addAll(movieService.searchByTitle(query));
        results.addAll(tvShowService.searchByTitle(query));
        return results;
    }
}