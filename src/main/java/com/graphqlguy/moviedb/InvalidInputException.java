package com.graphqlguy.moviedb;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {

    private final String field;

    public InvalidInputException(String field,  String message) {
        super(message);
        this.field = field;
    }
}
