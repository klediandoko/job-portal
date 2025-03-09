package com.internship.portal.controller;

import com.internship.portal.model.resource.ReviewResource;
import com.internship.portal.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.internship.portal.controller.ReviewController.REVIEW_PAGE;

@RestController
@RequestMapping(REVIEW_PAGE)
public class ReviewController {

    static final String REVIEW_PAGE = "/review";
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/save-review", consumes = "application/json")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> saveReview(@Valid @RequestBody ReviewResource reviewResource) {
        reviewService.save(reviewResource);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/jobs/reviews")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<ReviewResource>> getReviewsForJob(
            @RequestParam(value = "jobId") Long jobId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(reviewService.getAllReviewsByJobIdAndFilters(jobId, rating, page, size));
    }
}
