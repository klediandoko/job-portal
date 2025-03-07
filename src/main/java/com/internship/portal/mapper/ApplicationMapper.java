package com.internship.portal.mapper;

import com.internship.portal.model.entity.Application;
import com.internship.portal.model.resource.ApplicationResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "user.name", target = "applicantName", defaultValue = "UNKNOWN")
    @Mapping(source = "job.jobTitle", target = "jobTitle", defaultValue = "UNKNOWN")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "job.id", target = "jobId")
    ApplicationResource applicationToApplicationResource(Application application);

    List<ApplicationResource> applicationsToApplicationResources(List<Application> applications);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "jobId", target = "job.id")
    Application applicationResourceToApplication(ApplicationResource applicationResource);
}
