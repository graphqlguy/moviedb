package com.graphqlguy.moviedb.review;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * In-memory pub/sub bridge between review mutations and the reviewAdded
 * subscription. directBestEffort: events are simply dropped when nobody
 * is subscribed.
 */
@Component
public class ReviewPublisher {

    private final Sinks.Many<ReviewNotification> sink = Sinks.many().multicast().directBestEffort();

    public void publish(ReviewNotification notification) {
        sink.tryEmitNext(notification);
    }

    public Flux<ReviewNotification> flux() {
        return sink.asFlux();
    }
}
