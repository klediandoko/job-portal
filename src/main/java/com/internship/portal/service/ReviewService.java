package com.internship.portal.service;

import com.internship.portal.mapper.ReviewMapper;
import com.internship.portal.model.entity.Review;
import com.internship.portal.model.resource.ReviewResource;
import com.internship.portal.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public Optional<List<ReviewResource>> getAllByJobId(Long jobId){
        List<ReviewResource> reviewResources = reviewRepository.getAllByJob_Id(jobId)
                .stream()
                .map(reviewMapper::reviewToReviewResource)
                .toList();
        return reviewResources.isEmpty() ? Optional.empty() : Optional.of(reviewResources);
    }

    public Page<ReviewResource> getAllReviewsByJobIdAndFilters(Long jobId, Integer rating, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.getAllByJobAndFilters(jobId, rating, pageable);

        return reviewPage.map(reviewMapper::reviewToReviewResource);
    }



}
