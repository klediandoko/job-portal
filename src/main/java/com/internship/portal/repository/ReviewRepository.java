package com.internship.portal.repository;

import com.internship.portal.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.job.id = :jobId " +
            "and (:rating is null or r.rating = :rating)")
    Page<Review>getAllByJobAndFilters(
            @Param("jobId") Long jobId,
            @Param("rating") Integer rating,
            Pageable pageable);


}
