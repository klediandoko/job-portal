package com.internship.portal.mapper;

import com.internship.portal.model.entity.Resume;
import com.internship.portal.model.resource.ResumeResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    ResumeResource toResource(Resume resume);
}
