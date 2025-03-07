package com.internship.portal.repository;

import com.internship.portal.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> getAllByJob_Id(Long id);

    @Query("select r from Review r where r.job.id = :jobId " +
            "and (:rating is null or r.rating = :rating)")
    Page<Review>getAllByJobAndFilters(
            @RequestParam("jobId") Long jobId,
            @RequestParam("rating") Integer rating,
            Pageable pageable);
}
