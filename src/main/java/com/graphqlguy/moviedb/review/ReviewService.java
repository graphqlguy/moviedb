package com.graphqlguy.moviedb.review;

import com.graphqlguy.moviedb.exception.DuplicateReviewException;
import com.graphqlguy.moviedb.exception.EntityNotFoundException;
import com.graphqlguy.moviedb.movie.Movie;
import com.graphqlguy.moviedb.movie.MovieRepository;
import com.graphqlguy.moviedb.tvshow.TvShow;
import com.graphqlguy.moviedb.tvshow.TvShowRepository;
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
    private final TvShowRepository tvShowRepository;
    private final UserRepository userRepository;
    private final ReviewPublisher reviewPublisher;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Review createReview(CreateReviewInput input, String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no matching record: " + username));

        Review.ReviewBuilder review = Review.builder()
                .user(user)
                .score(input.score())
                .comment(input.comment());

        // The @oneOf directive guarantees exactly one of movieId/tvShowId is set
        if (input.subject().movieId() != null) {
            Long movieId = Long.parseLong(input.subject().movieId());
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Movie", movieId));
            if (reviewRepository.existsByMovieIdAndUserId(movieId, user.getId())) {
                throw new DuplicateReviewException();
            }
            review.movie(movie);
        } else {
            Long tvShowId = Long.parseLong(input.subject().tvShowId());
            TvShow tvShow = tvShowRepository.findById(tvShowId)
                    .orElseThrow(() -> new EntityNotFoundException("TvShow", tvShowId));
            if (reviewRepository.existsByTvShowIdAndUserId(tvShowId, user.getId())) {
                throw new DuplicateReviewException();
            }
            review.tvShow(tvShow);
        }

        Review saved = reviewRepository.save(review.build());
        reviewPublisher.publish(ReviewNotification.of(saved));
        return saved;
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