package com.internship.portal.model.resource;

public class UserResource {

    private String name;
    private String email;
    private String roleName;

    public UserResource() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
