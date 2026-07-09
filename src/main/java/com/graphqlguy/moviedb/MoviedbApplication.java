package com.graphqlguy.moviedb;

import com.graphqlguy.moviedb.config.DemoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DemoProperties.class)
public class MoviedbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviedbApplication.class, args);
	}

}
