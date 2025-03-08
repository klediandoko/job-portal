package com.internship.portal.repository;

import com.internship.portal.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE j.employer.id = :employerId " +
            "AND (:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Job> findByEmployerIdAndFilters(
            @Param("employerId") Long employerId,
            @Param("title") String title,
            @Param("location") String location,
            Pageable pageable);

    List<Job> findAllByEmployer_Id(Long employerId);

    @Query("SELECT j FROM Job j WHERE (:title IS NULL OR LOWER(j.jobTitle)" +
            " LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:employerId IS NULL OR j.employer.id = :employerId)")
    Page<Job> findAllAndFiltersJobs(
            @RequestParam("title") String title,
            @RequestParam("location") String location,
            @RequestParam("employerId") Long employerId,
            Pageable pageable);

    Job getJobById(Long id);
}
