package com.graphqlguy.moviedb.movie;

import com.graphqlguy.moviedb.person.Person;
import com.graphqlguy.moviedb.shared.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    boolean existsByDirectorsContaining(Person director);

    @Query("select distinct m from Movie m left join fetch m.directors where m.id in :ids")
    List<Movie> findAllWithDirectorsByIdIn(@Param("ids") List<Long> ids);


    @Query("SELECT m FROM Movie m WHERE " +
            "(:genre IS NULL OR m.genre = :genre) AND " +
            "(:minRating IS NULL OR m.rating >= :minRating) AND " +
            "(:maxRating IS NULL OR m.rating <= :maxRating) AND " +
            "(:minYear IS NULL OR m.releaseYear >= :minYear) AND " +
            "(:maxYear IS NULL OR m.releaseYear <= :maxYear) AND " +
            "(:titleContains IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :titleContains, '%')))")
    Page<Movie> findWithFilters(
            @Param("genre") Genre genre,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("titleContains") String titleContains,
            Pageable pageable
    );

    List<Movie> findByDirectorsContaining(Person person);
}
