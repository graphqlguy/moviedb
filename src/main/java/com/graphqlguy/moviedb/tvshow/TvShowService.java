package com.graphqlguy.moviedb.tvshow;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TvShowService {

    private final TvShowRepository tvShowRepository;

    public Optional<TvShow> findById(Long id) {
        return tvShowRepository.findById(id);
    }

    public TvShowPage findAll(int page, int size) {
        Page<TvShow> result = tvShowRepository.findAll(
                PageRequest.of(page, size, Sort.by("startYear").descending().and(Sort.by("id"))));
        return new TvShowPage(result.getContent(), result.getTotalElements(),
                result.getTotalPages(), result.getNumber(), result.getSize());
    }

    public List<TvShow> searchByTitle(String title) {
        return tvShowRepository.findByTitleContainingIgnoreCase(title);
    }
}