package com.internship.portal.service;

import com.internship.portal.mapper.ReviewMapper;
import com.internship.portal.model.entity.Review;
import com.internship.portal.model.resource.ReviewResource;
import com.internship.portal.repository.JobRepository;
import com.internship.portal.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final JobRepository jobRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, JobRepository jobRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.jobRepository = jobRepository;
    }

    public Page<ReviewResource> getAllReviewsByJobIdAndFilters(Long jobId, Integer rating, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.getAllByJobAndFilters(jobId, rating, pageable);

        return reviewPage.map(reviewMapper::reviewToReviewResource);
    }

    @Transactional
    public void save(ReviewResource reviewResource) {

        Long employerId =  jobRepository.getJobById(reviewResource.getJobId()).getEmployer().getId();

        if (jobRepository.existsById(reviewResource.getJobId()) &&
                employerId.equals(reviewResource.getEmployerId())) {
            reviewRepository.save(reviewMapper.reviewResourceToReview(reviewResource));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
