package com.internship.portal.controller;

import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.model.resource.ApplicationWithResumeResource;
import com.internship.portal.service.ApplicationService;
import com.internship.portal.service.AuthService;
import com.internship.portal.service.JobService;
import com.internship.portal.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.internship.portal.controller.ApplicationController.APPLICATION_PAGE;

@RestController
@RequestMapping(APPLICATION_PAGE)
public class ApplicationController {

    static final String APPLICATION_PAGE = "/applications";
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ReviewService reviewService;
    private final AuthService authService;

    @Autowired
    public ApplicationController(JobService jobService, ApplicationService applicationService,
                                 ReviewService reviewService, AuthService authService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.reviewService = reviewService;
        this.authService = authService;
    }


    @GetMapping(value = "/jobs/applications/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<ApplicationResource>> getApplicationsForJob(@PathVariable Long jobId) {
        if (!authService.getLoggedInUserId().equals(jobService.getJobById(jobId).getEmployerId())) {
            throw new RuntimeException("Job not found");
        }

        return ResponseEntity.ok(applicationService.getAllApplicationsByJobId(jobId));
    }

    @GetMapping(value = "/my-job/applications/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<ApplicationWithResumeResource> getAllApplicationsForJob(
            @PathVariable Long jobId,
            @RequestParam(value = "applicationStatus", required = false) ApplicationStatus applicationStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authService.getLoggedInUserId().equals(jobService.getJobById(jobId).getEmployerId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found!");
        }

        return applicationService.findAllByJobIdAndFilters(jobId, applicationStatus, page, size);
    }

    @PutMapping(value = "/update-status/{applicationId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam("applicationStatus") ApplicationStatus applicationStatus) {

        applicationService.updateApplicationStatus(applicationId, applicationStatus);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<Void> addApplication(
            @Valid @RequestBody ApplicationResource applicationResource) {

        applicationService.saveApplication(applicationResource);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<Page<ApplicationResource>> getMyApplications(
            @RequestParam(value = "applicationStatus", required = false) String applicationStatus,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(applicationService.getMyApplicationsAndFilters(
                applicationStatus, jobTitle, page, size));
    }
}
