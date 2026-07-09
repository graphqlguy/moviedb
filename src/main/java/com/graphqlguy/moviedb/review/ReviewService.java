package com.graphqlguy.moviedb.review;

import com.graphqlguy.moviedb.exception.DuplicateReviewException;
import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieRepository;
import com.graphqlguy.moviedb.user.AppUser;
import com.graphqlguy.moviedb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Review createMovieReview(CreateMovieReviewInput input, String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no matching record: " + username));
        Long movieId = Long.parseLong(input.movieId());
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie", movieId));

        if (reviewRepository.existsByMovieIdAndUserId(movieId, user.getId())) {
            throw new DuplicateReviewException();
        }

        return reviewRepository.save(Review.builder()
                .movie(movie)
                .user(user)
                .score(input.score())
                .comment(input.comment())
                .build());
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public DeleteReviewResponse deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review", reviewId));
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no matching record: " + username));

        boolean isOwner = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
        return new DeleteReviewResponse(true, reviewId);
    }
}