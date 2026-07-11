package com.graphqlguy.moviedb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MoviedbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviedbApplication.class, args);
	}

}