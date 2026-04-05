package com.graphqlguy.moviedb.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Long id;
    private String title;
    private Integer releaseYear;
    private String genre;
}
