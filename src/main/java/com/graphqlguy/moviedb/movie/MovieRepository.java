package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    boolean existsByDirectorsContaining(Person director);

    @Query("select distinct m from Movie m left join fetch m.directors where m.id in :ids")
    List<Movie> findAllWithDirectorsByIdIn(@Param("ids") List<Long> ids);
}
