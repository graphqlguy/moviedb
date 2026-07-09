package com.graphqlguy.moviedb.review;

public record CreateMovieReviewInput(String movieId, int score, String comment) {}