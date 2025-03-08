package com.internship.portal.model.resource;

import com.internship.portal.model.entity.Application;

public class ApplicationWithResumeResource {

    private Application application;
    private String filePath;

    public ApplicationWithResumeResource(Application application, String filePath) {
        this.application = application;
        this.filePath = filePath;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
