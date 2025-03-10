package com.internship.portal.model.resource;

public class ResumeResource {

    private Long id;
    private String filePath;

    public ResumeResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}


