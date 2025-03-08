package com.internship.portal.controller;


import com.internship.portal.model.resource.UserResource;
import com.internship.portal.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.internship.portal.controller.AdminController.ADMIN_PAGE;

@RestController
@RequestMapping(ADMIN_PAGE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    static final String ADMIN_PAGE = "/admin";

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping(value = "/delete")
    @Transactional
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/get-all")
    public Page<UserResource> getAllByRole(@RequestParam(value = "roleName", required = false) String roleName,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(roleName, page, size);
    }
}
