package com.graphqlguy.moviedb.person;

public record DeletePersonResponse(boolean success, String message, Long deletedId) {
}
