package com.internship.portal.mapper;

import com.internship.portal.model.entity.Review;
import com.internship.portal.model.resource.ReviewResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "employer.name", target = "employerName", defaultValue = "UNKNOWN")
    @Mapping(source = "job.jobTitle", target = "jobTitle", defaultValue = "UNKNOWN")
    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(source = "job.id", target = "jobId")
    ReviewResource reviewToReviewResource(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "jobId", target = "job.id")
    Review reviewResourceToReview(ReviewResource reviewResource);

}
