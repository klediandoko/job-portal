package com.internship.portal.service;

import com.internship.portal.exception.CustomException;
import com.internship.portal.mapper.ApplicationMapper;
import com.internship.portal.model.entity.Application;
import com.internship.portal.model.entity.Job;
import com.internship.portal.model.entity.Resume;
import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.repository.ApplicationRepository;
import com.internship.portal.repository.JobRepository;
import com.internship.portal.repository.ResumeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            ApplicationMapper applicationMapper,
            AuthService authService, ResumeRepository resumeRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.authService = authService;
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
    }

    public Page<ApplicationResource> findAllByJobIdAndFilters(Long jobId,
                                                              ApplicationStatus applicationStatus,
                                                              int page,
                                                              int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Long loggedInUserId = authService.getLoggedInUserId();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found", HttpStatus.NOT_FOUND));

        if (!job.getEmployer().getId().equals(loggedInUserId)) {
            throw new CustomException("Job not found", HttpStatus.NOT_FOUND);
        }
        Page<Application> applicationsPage = applicationRepository.findAllByJobIdAndFilters(
                jobId, applicationStatus, pageRequest);
        return applicationsPage.map(applicationMapper::applicationToApplicationResource);
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException("Application not found", HttpStatus.NOT_FOUND));

        Long loggedInUserId = authService.getLoggedInUserId();
        if (!application.getUser().getId().equals(loggedInUserId)) {
            throw new CustomException("Application not found", HttpStatus.NOT_FOUND);
        }

        application.setStatus(newStatus);
        applicationRepository.save(application);
    }

    public Page<ApplicationResource> getMyApplicationsAndFilters(
            String applicationStatus, String jobTitle, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Long loggedInUserId = authService.getLoggedInUserId();

        Page<Application> applicationsPage = applicationRepository
                .findAllByUserAndFilters(loggedInUserId, applicationStatus, jobTitle, pageRequest);

        return applicationsPage.map(applicationMapper::applicationToApplicationResource);
    }

    @Transactional
    public void saveApplication(ApplicationResource applicationResource) {
        applicationResource.setUserId(authService.getLoggedInUserId());
        applicationResource.setAppliedDate(LocalDateTime.now());
        applicationResource.setStatus(ApplicationStatus.PENDING);

        applicationRepository.save(applicationMapper.applicationResourceToApplication(applicationResource));
    }

    @Transactional
    public void saveApplicationWithOptionalResume(ApplicationResource applicationResource,
                                                  MultipartFile resumeFile) throws IOException {
        Long loggedInUserId = authService.getLoggedInUserId();
        applicationResource.setUserId(loggedInUserId);
        applicationResource.setAppliedDate(LocalDateTime.now());
        applicationResource.setStatus(ApplicationStatus.PENDING);

        Resume resume = null;

        if (resumeFile != null && !resumeFile.isEmpty()) {
            final String UPLOAD_DIR = "C:/Users/user/Desktop/uploads/resumes";

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFileName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
            String fileExtension = originalFileName.contains(".")
                    ? originalFileName.substring(originalFileName.lastIndexOf("."))
                    : "";
            String fileName = loggedInUserId + "_" + System.currentTimeMillis() + fileExtension;

            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(resumeFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            resume = new Resume();
            resume.setFilePath(targetPath.toString());
            resume = resumeRepository.save(resume);
        }
        Application application = applicationMapper.applicationResourceToApplication(applicationResource);

        if (resume != null) {
            application.setResume(resume);
        }
        applicationRepository.save(application);
    }
}