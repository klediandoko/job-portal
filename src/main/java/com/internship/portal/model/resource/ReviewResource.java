package com.internship.portal.model.resource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewResource {

    private Long id;
    @NotBlank(message = "A review description is needed")
    private String reviewDescription;
    @NotNull(message = "Rating must be compiled")
    @Min(value = 1, message = "Rating cannot be lower than 1")
    @Max(value = 5, message = "Value cannot be bigger than 5")
    private Integer rating;
    private String employerName;
    private Long employerId;
    @NotNull(message = "Job Id missing")
    private Long jobId;
    private String jobTitle;

    public ReviewResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
