package com.internship.portal.mapper;

import com.internship.portal.model.entity.Resume;
import com.internship.portal.model.resource.ResumeResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(source = "jobSeeker.id", target = "userId")
    ResumeResource toResource(Resume resume);
}
