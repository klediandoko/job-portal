package com.internship.portal.controller;

import com.internship.portal.model.resource.ResumeResource;
import com.internship.portal.service.AuthService;
import com.internship.portal.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.internship.portal.controller.ResumeController.RESUME_PAGE;

@RestController
@RequestMapping(RESUME_PAGE)
@PreAuthorize("hasRole('JOB_SEEKER')")
public class ResumeController {

    static final String RESUME_PAGE = "/resume";
    private final ResumeService resumeService;

    public ResumeController(
            ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResource> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {

        ResumeResource resumeResource = resumeService.uploadResume(file);
        return ResponseEntity.ok(resumeResource);
    }
}
