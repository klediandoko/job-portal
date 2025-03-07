package com.internship.portal.mapper;

import com.internship.portal.model.entity.Job;
import com.internship.portal.model.entity.User;
import com.internship.portal.model.resource.JobResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "employer.id", target = "employerId")
    JobResource jobToJobResource(Job job);

    List<JobResource> jobsToJobResource(List<Job> jobs);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer", qualifiedByName = "mapEmployer")
    Job jobResourceToJob(JobResource jobResource);

    @Named("mapEmployer")
    default User mapEmployer(Long employerId) {
        if (employerId == null) {
            return null;
        }
        User employer = new User();
        employer.setId(employerId);
        return employer;
    }
}
