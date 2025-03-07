package com.internship.portal.controller;

import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.model.resource.JobResource;
import com.internship.portal.model.resource.ReviewResource;
import com.internship.portal.service.ApplicationService;
import com.internship.portal.service.AuthService;
import com.internship.portal.service.JobService;
import com.internship.portal.service.ReviewService;
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
import java.util.Optional;

import static com.internship.portal.controller.EmployerController.EMPLOYER_PAGE;

@RestController
@RequestMapping(EMPLOYER_PAGE)
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    static final String EMPLOYER_PAGE = "/employer";
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ReviewService reviewService;
    private final AuthService authService;

    @Autowired
    public EmployerController(JobService jobService, ApplicationService applicationService, ReviewService reviewService, AuthService authService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.reviewService = reviewService;
        this.authService = authService;
    }

    @GetMapping(value = "/jobs/{employerId}")
    public ResponseEntity<List<JobResource>> getJobsForEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(jobService.getAllJobsByEmployer(employerId));
    }

    @PostMapping(path = "/save", consumes = "application/json")
    public ResponseEntity<Void> saveJob(@RequestBody JobResource jobResource) {
        jobService.saveJob(jobResource);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/jobs/applications/{jobId}")
    public ResponseEntity<List<ApplicationResource>> getApplicationsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getAllApplicationsByJobId(jobId));
    }

    @PutMapping(value = "/update-status/{applicationId}")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long applicationId, @RequestParam("applicationStatus") ApplicationStatus applicationStatus) {
        applicationService.updateApplicationStatus(applicationId, applicationStatus);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/jobs/reviews/{jobId}")
    public ResponseEntity<Optional<List<ReviewResource>>> getReviewsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ofNullable(reviewService.getAllByJobId(jobId));
    }

    @GetMapping("/my-jobs")
    public Page<JobResource> getMyJobs(@RequestParam(required = false) String title, @RequestParam(required = false) String location, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        Long loggedInUserId = authService.getLoggedInUserId();
        return jobService.getJobsByEmployer(loggedInUserId, title, location, page, size);

    }

    @GetMapping("/jobs/reviews")
    public Page<ReviewResource> getReviewsForJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (authService.getLoggedInUserId().equals(jobService.getJobById(jobId).getEmployerId())) {
            return reviewService.getAllReviewsByJobIdAndFilters(jobId, rating, page, size);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found!");
        }
    }

    @PostMapping
    public void saveReview(@RequestBody ReviewResource reviewResource) {
        //TODO: implement method for to save a new review for a job
    }

}
