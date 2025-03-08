package com.internship.portal.service;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;


    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    public List<ApplicationResource> getAllApplicationsByJobId(Long jobId) {
        return applicationMapper.applicationsToApplicationResources(applicationRepository.findAllByJob_Id(jobId));
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(applicationStatus);
        applicationRepository.save(application);
    }

    public Page<ApplicationResource> getMyApplicationsAndFilters(Long userId, String applicationStatus, String jobTitle, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Application> applicationsPage = applicationRepository.findAllByUserAndFilters(userId, applicationStatus, jobTitle, pageRequest);

        return applicationsPage.map(applicationMapper::applicationToApplicationResource);
    }

    @Transactional
    public void saveApplication(ApplicationResource applicationResource) {
        applicationResource.setAppliedDate(LocalDateTime.now());
        applicationRepository.save(applicationMapper.applicationResourceToApplication(applicationResource));
    }

    public Page<ApplicationWithResumeResource> findAllByJobIdAndFilters(Long jobId, ApplicationStatus applicationStatus, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return applicationRepository.findAllByJobIdAndFilters(jobId, applicationStatus, pageable);
    }
}