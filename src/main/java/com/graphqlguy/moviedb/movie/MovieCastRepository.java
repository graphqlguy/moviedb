package com.graphqlguy.moviedb.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {

    List<MovieCast> findAllByMovieId(Long movieId);
}
