package com.internship.portal.service;

import com.internship.portal.mapper.JobMapper;
import com.internship.portal.model.entity.Job;
import com.internship.portal.model.resource.JobResource;
import com.internship.portal.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    public List<JobResource> getAllJobsByEmployer(Long employerId) {
        return Optional.ofNullable(jobMapper.jobsToJobResource(jobRepository.findAllByEmployer_Id(employerId)))
                .filter(jobs -> !jobs.isEmpty())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No jobs found for employer ID: " + employerId));
    }

    @Transactional
    public void saveJob(JobResource jobResource) {
        jobRepository.save(jobMapper.jobResourceToJob(jobResource));
    }

    public List<JobResource> getAllJobs() {
        return jobMapper.jobsToJobResource(jobRepository.findAll());
    }

    public Page<JobResource> getJobsByEmployer(Long employerId, String title, String location, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobRepository.findByEmployerIdAndFilters(employerId, title, location, pageable);

        return jobPage.map(jobMapper::jobToJobResource);
    }

    public Page<JobResource> findAllPageable(String title, String location,
                                             Long employerId, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobRepository.findAllAndFiltersJobs(title, location, employerId, pageable);
        return jobPage.map(jobMapper::jobToJobResource);
    }

    public JobResource getJobById(Long id) {
        return jobMapper.jobToJobResource(jobRepository.findById(id).get());
    }


}
