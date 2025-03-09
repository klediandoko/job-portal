package com.internship.portal.service;

import com.internship.portal.mapper.JobMapper;
import com.internship.portal.model.entity.Job;
import com.internship.portal.model.resource.JobResource;
import com.internship.portal.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final AuthService authService;

    public JobService(JobRepository jobRepository, JobMapper jobMapper, AuthService authService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.authService = authService;
    }

    @Transactional
    public void saveJob(JobResource jobResource) {
        jobResource.setEmployerId(authService.getLoggedInUserId());
        jobResource.setCreatedDate(new Date());
        jobRepository.save(jobMapper.jobResourceToJob(jobResource));
    }

    public Page<JobResource> getJobsByEmployer(String title,
                                               String location, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Long loggedInUserId = authService.getLoggedInUserId();

        Page<Job> jobPage = jobRepository.findByEmployerIdAndFilters(loggedInUserId, title, location, pageable);

        return jobPage.map(jobMapper::jobToJobResource);
    }

    public Page<JobResource> findAllJobsAndFilters(String title, String location,
                                                   Long employerId, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobRepository.findAllAndFiltersJobs(title, location, employerId, pageable);
        return jobPage.map(jobMapper::jobToJobResource);
    }
}
