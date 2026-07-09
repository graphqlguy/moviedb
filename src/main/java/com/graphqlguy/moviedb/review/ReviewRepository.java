package com.graphqlguy.moviedb.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r join fetch r.user where r.movie.id in :movieIds order by r.createdAt desc")
    List<Review> findWithUserByMovieIdIn(@Param("movieIds") List<Long> movieIds);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);
}