package com.internship.portal.controller;

import com.internship.portal.model.resource.JobResource;
import com.internship.portal.service.JobService;
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

import static com.internship.portal.controller.JobController.JOBS_PAGE;

@RestController
@RequestMapping(JOBS_PAGE)
public class JobController {

    static final String JOBS_PAGE = "/jobs";
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(path = "/save-job", consumes = "application/json")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> saveJob(@Valid @RequestBody JobResource jobResource) {

        jobService.saveJob(jobResource);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<JobResource>> getMyJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(jobService.getJobsByEmployer(title, location, page, size));
    }

    @GetMapping(value = "/jobs-filter")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<Page<JobResource>> getAllJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long employerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(jobService.findAllJobsAndFilters(title, location, employerId, page, size));
    }
}
