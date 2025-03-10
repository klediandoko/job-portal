package com.internship.portal.service;

import com.internship.portal.mapper.UserMapper;
import com.internship.portal.model.entity.User;
import com.internship.portal.model.resource.UserResource;
import com.internship.portal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserResource> getAllUsers(String roleName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllFilterByRole(roleName, pageable);
        return userPage.map(userMapper::userToUserResource);
    }

    @Transactional
    public void deleteUser(String email) {
        if (userRepository.existsByEmail(email)) {
            userRepository.deleteByEmail(email);
        } else {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
    }
}
