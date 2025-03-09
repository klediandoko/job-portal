package com.internship.portal.service;

import com.internship.portal.exception.CustomException;
import com.internship.portal.mapper.ApplicationMapper;
import com.internship.portal.model.entity.Application;
import com.internship.portal.model.enums.ApplicationStatus;
import com.internship.portal.model.resource.ApplicationResource;
import com.internship.portal.model.resource.ApplicationWithResumeResource;
import com.internship.portal.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final AuthService authService;


    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, AuthService authService) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.authService = authService;
    }

    public List<ApplicationResource> getAllApplicationsByJobId(Long jobId) {
        return applicationMapper.applicationsToApplicationResources(applicationRepository.findAllByJob_Id(jobId));
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

        Page<Application> applicationsPage = applicationRepository.findAllByUserAndFilters(
                loggedInUserId, applicationStatus, jobTitle, pageRequest);

        return applicationsPage.map(applicationMapper::applicationToApplicationResource);
    }

    @Transactional
    public void saveApplication(ApplicationResource applicationResource) {
        applicationResource.setUserId(authService.getLoggedInUserId());
        applicationResource.setAppliedDate(LocalDateTime.now());
        applicationResource.setStatus(ApplicationStatus.PENDING);

        applicationRepository.save(applicationMapper.applicationResourceToApplication(applicationResource));
    }

    public Page<ApplicationWithResumeResource> findAllByJobIdAndFilters(
            Long jobId, ApplicationStatus applicationStatus, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return applicationRepository.findAllByJobIdAndFilters(jobId, applicationStatus, pageable);
    }
}