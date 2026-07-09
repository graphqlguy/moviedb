package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {

    List<MovieCast> findAllByMovieId(Long movieId);

    boolean existsByPerson(Person person);

    @Query("select mc from MovieCast mc join fetch mc.person where mc.movie.id in :ids")
    List<MovieCast> findWithPersonByMovieIdIn(@Param("ids") List<Long> ids);
}
