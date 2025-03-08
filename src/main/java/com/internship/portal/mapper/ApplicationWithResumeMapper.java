package com.internship.portal.mapper;

import com.internship.portal.model.entity.Application;
import com.internship.portal.model.resource.ApplicationWithResumeResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationWithResumeMapper {

//    @Mapping(source = "user.resume.filePath", target = "filePath", defaultValue = "NO RESUME")
//    ApplicationWithResumeResource applicationToApplicationWithResumeResource(Application application);
}