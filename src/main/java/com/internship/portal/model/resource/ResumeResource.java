package com.internship.portal.model.resource;

public class ResumeResource {

        private Long id;
        private Long userId;
        private String filePath;

        public ResumeResource() {
        }

        public ResumeResource(Long id, Long userId, String filePath) {
            this.id = id;
            this.userId = userId;
            this.filePath = filePath;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }


