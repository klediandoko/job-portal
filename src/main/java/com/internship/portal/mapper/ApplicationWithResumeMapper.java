package com.internship.portal.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationWithResumeMapper {

//    @Mapping(source = "user.resume.filePath", target = "filePath", defaultValue = "NO RESUME")
//    ApplicationWithResumeResource applicationToApplicationWithResumeResource(Application application);
}