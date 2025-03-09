package com.internship.portal.controller;

import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.service.ApplicationService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.internship.portal.controller.ApplicationController.APPLICATION_PAGE;

@RestController
@RequestMapping(APPLICATION_PAGE)
public class ApplicationController {

    static final String APPLICATION_PAGE = "/applications";
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(value = "/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<ApplicationResource> getAllApplicationsForJob(
            @PathVariable Long jobId,
            @RequestParam(value = "applicationStatus", required = false) ApplicationStatus applicationStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
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

    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<Void> createApplication(
            @RequestPart("application") ApplicationResource applicationResource,
            @RequestPart(name = "resume", required = false) MultipartFile resumeFile) throws IOException {

        applicationService.saveApplicationWithOptionalResume(applicationResource, resumeFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
