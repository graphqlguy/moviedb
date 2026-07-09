package com.graphqlguy.moviedb.review;

import com.graphqlguy.moviedb.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @BatchMapping
    Map<Movie, List<Review>> reviews(List<Movie> movies) {
        List<Long> movieIds = movies.stream().map(Movie::getId).toList();
        Map<Long, List<Review>> reviewsByMovieId = reviewRepository.findWithUserByMovieIdIn(movieIds)
                .stream().collect(Collectors.groupingBy(review -> review.getMovie().getId()));
        return movies.stream()
                .collect(Collectors.toMap(movie -> movie,
                        movie -> reviewsByMovieId.getOrDefault(movie.getId(), List.of())));
    }

    @MutationMapping
    Review createMovieReview(@Argument CreateMovieReviewInput input, Principal principal) {
        return reviewService.createMovieReview(input, principal.getName());
    }

    @MutationMapping
    DeleteReviewResponse deleteReview(@Argument Long id, Principal principal) {
        return reviewService.deleteReview(id, principal.getName());
    }
}