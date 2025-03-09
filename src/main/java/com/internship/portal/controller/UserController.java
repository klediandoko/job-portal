package com.internship.portal.controller;


import com.internship.portal.model.resource.UserResource;
import com.internship.portal.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.internship.portal.controller.UserController.USERS_PAGE;

@RestController
@RequestMapping(USERS_PAGE)
public class UserController {

    static final String USERS_PAGE = "/users";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResource>> getAllUsersByRole(
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(userService.getAllUsers(roleName, page, size));
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
