package org.example.ReviewServiceGP.service;

import org.example.ReviewServiceGP.model.Review;
import org.example.ReviewServiceGP.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(String userId, String productId, int rating, String comment) {
        Review review = new Review(
                productId,
                userId,
                rating,
                comment,
                Instant.now()
        );
        return reviewRepository.save(review);
    }

    public Review updateReview(String userId, String reviewId, int rating, String comment) {
        Optional<Review> existingOpt = reviewRepository.findById(reviewId);

        if (existingOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        Review existing = existingOpt.get();

        if (!existing.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own reviews");
        }

        existing.setRating(rating);
        existing.setComment(comment);
        existing.setTimestamp(Instant.now()); // обновляем время

        return reviewRepository.save(existing);
    }

    public void deleteReview(String userId, String reviewId) {
        Optional<Review> existingOpt = reviewRepository.findById(reviewId);

        if (existingOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        Review existing = existingOpt.get();

        if (!existing.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own reviews");
        }

        reviewRepository.delete(existing);
    }

    public List<Review> getReviewsByProduct(String productId) {
        return reviewRepository.findByProductId(productId);
    }
}
