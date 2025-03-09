package com.internship.portal.repository;

import com.internship.portal.model.entity.Application;
import com.internship.portal.model.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("select a from Application a where a.user.id = :userId" +
            " and (:applicationStatus is null or  lower(a.status) like (concat('%', :applicationStatus, '%') )) " +
            "and (:jobTitle is null or lower(a.job.jobTitle) like lower(concat('%', :jobTitle, '%')))")
    Page<Application> findAllByUserAndFilters(
            @Param("userId") Long userId,
            @Param("applicationStatus") String applicationStatus,
            @Param("jobTitle") String jobTitle,
            Pageable pageable);

    @Query("SELECT a FROM Application a " +
            "WHERE a.job.id = :jobId " +
            "AND (:applicationStatus IS NULL OR a.status = :applicationStatus)")
    Page<Application> findAllByJobIdAndFilters(
            @Param("jobId") Long jobId,
            @Param("applicationStatus") ApplicationStatus applicationStatus,
            Pageable pageable);

}
