package com.internship.portal.model.resource;

public class ResumeResource {

        private Long id;
        private String filePath;

        public ResumeResource() {
        }

        public ResumeResource(Long id, String filePath) {
            this.id = id;
            this.filePath = filePath;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }


