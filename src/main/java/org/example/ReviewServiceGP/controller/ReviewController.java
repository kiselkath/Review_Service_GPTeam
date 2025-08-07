package org.example.ReviewServiceGP.controller;


import org.example.ReviewServiceGP.model.Review;
import org.example.ReviewServiceGP.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // POST /api/reviews
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, Object> payload,
                                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        String productId = (String) payload.get("productId");
        int rating = (int) payload.get("rating");
        String comment = (String) payload.get("comment");

        Review review = reviewService.createReview(userId, productId, rating, comment);
        return ResponseEntity.ok(review);
    }

    // PUT /api/reviews/{reviewId}
    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable String reviewId,
                                               @RequestBody Map<String, Object> payload,
                                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        int rating = (int) payload.get("rating");
        String comment = (String) payload.get("comment");

        Review updated = reviewService.updateReview(userId, reviewId, rating, comment);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/reviews/{reviewId}
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId,
                                             @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/reviews/product/{productId}
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }
}
