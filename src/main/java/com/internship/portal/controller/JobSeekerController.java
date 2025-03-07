package com.internship.portal.controller;

import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.model.resource.JobResource;
import com.internship.portal.model.resource.ResumeResource;
import com.internship.portal.service.ApplicationService;
import com.internship.portal.service.AuthService;
import com.internship.portal.service.JobService;
import com.internship.portal.service.ResumeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.internship.portal.controller.JobSeekerController.JOB_SEEKER_PAGE;

@RestController
@RequestMapping(JOB_SEEKER_PAGE)
@PreAuthorize("hasRole('JOB_SEEKER')")
public class JobSeekerController {

    static final String JOB_SEEKER_PAGE = "/job-seeker";

    private final JobService jobService;
    private final ResumeService resumeService;
    private final AuthService authService;
    private final ApplicationService applicationService;


    public JobSeekerController(JobService jobService, ResumeService resumeService, AuthService authService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.resumeService = resumeService;
        this.authService = authService;
        this.applicationService = applicationService;
    }

    @GetMapping(value = "/jobs")
    public ResponseEntity<List<JobResource>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResource> uploadResume(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            ResumeResource resumeResource = resumeService.uploadResume(userId, file);
            return ResponseEntity.ok(resumeResource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping(value = "/jobs-filter")
    public Page<JobResource> getJobs(
            @RequestParam(required= false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long employerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        return jobService.findAllPageable(title, location, employerId, page, size);
    }

    @GetMapping("/my-applications")
    public Page<ApplicationResource> getMyApplications(
            @RequestParam(value = "applicationStatus", required = false) String applicationStatus,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Long loggedInUserId = authService.getLoggedInUserId();
        return applicationService.getMyApplicationsAndFilters(
                loggedInUserId, applicationStatus, jobTitle, page, size);

    }

    @PostMapping
    public void saveApplication(){
        //TODO implementing the create a new application method.
    }
}
