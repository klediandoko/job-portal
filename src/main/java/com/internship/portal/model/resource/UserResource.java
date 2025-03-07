package com.internship.portal.model.resource;

import com.internship.portal.model.entity.User;

public class UserResource {

    private String name;
    private String email;
    private String roleName;

    public UserResource() {
    }

    public UserResource(User user){
        this.name = user.getName();
        this.email=user.getEmail();
        this.roleName=user.getRole().getRoleName().name();
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
