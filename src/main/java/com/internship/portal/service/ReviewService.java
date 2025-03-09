package com.internship.portal.service;

import com.internship.portal.exception.CustomException;
import com.internship.portal.mapper.ReviewMapper;
import com.internship.portal.model.entity.Job;
import com.internship.portal.model.entity.Review;
import com.internship.portal.model.resource.ReviewResource;
import com.internship.portal.repository.JobRepository;
import com.internship.portal.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final JobRepository jobRepository;
    private final AuthService authService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewMapper reviewMapper, JobRepository jobRepository, AuthService authService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.jobRepository = jobRepository;
        this.authService = authService;
    }

    public Page<ReviewResource> getAllReviewsByJobIdAndFilters(
            Long jobId, Integer rating, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Long loggedInUserId = authService.getLoggedInUserId();
       Job job = jobRepository.existsById(jobId) ? jobRepository.findById(jobId).get() : null;

       if (job == null || !job.getEmployer().getId().equals(loggedInUserId)) {
           throw new CustomException("Job not found!", HttpStatus.NOT_FOUND);
       }
        Page<Review> reviewPage = reviewRepository.getAllByJobAndFilters(jobId, rating, pageable);
        return reviewPage.map(reviewMapper::reviewToReviewResource);
    }

    @Transactional
    public void save(ReviewResource reviewResource) {
        Long loggedInUserId = authService.getLoggedInUserId();
        reviewResource.setEmployerId(loggedInUserId);

        Job job = jobRepository.findById(reviewResource.getJobId())
                .orElseThrow(() -> new CustomException("Job not found", HttpStatus.NOT_FOUND));

        if (!job.getEmployer().getId().equals(loggedInUserId)) {
            throw new CustomException("Job not found", HttpStatus.NOT_FOUND);
        }
    reviewRepository.save(reviewMapper.reviewResourceToReview(reviewResource));
    }
}
