package com.internship.portal.repository;

import com.internship.portal.model.entity.Application;
import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationWithResumeResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByJob_Id(Long id);

    @Modifying
    @Query("update Application a set a.status = :status where a.id = :id")
    void updateApplicationStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") String status);

    @Query("select a from Application a where a.user.id = :userId" +
            " and (:applicationStatus is null or  lower(a.status) like (concat('%', :applicationStatus, '%') )) " +
            "and (:jobTitle is null or lower(a.job.jobTitle) like lower(concat('%', :jobTitle, '%')))")
    Page<Application> findAllByUserAndFilters(
            @RequestParam("userId") Long userId,
            @RequestParam("applicationStatus") String applicationStatus,
            @RequestParam("jobTitle") String jobTitle,
            Pageable pageable);

    @Query("select a, r.filePath from Application a " +
            "join a.user u " +
            "join u.resume r " +
            "where a.job.id= :jobId " +
            "and (:applicationStatus is null or a.status = :applicationStatus)")
    Page<ApplicationWithResumeResource> findAllByJobIdAndFilters(
            @Param("jobId") Long jobId,
            @Param("applicationStatus") ApplicationStatus applicationStatus,
            Pageable pageable);

}
