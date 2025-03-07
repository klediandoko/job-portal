package com.internship.portal.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {

    public static void main(String[] args) {
        String rawPassword = "adminpass"; // Replace with the password you need to hash
        String hashed = new BCryptPasswordEncoder().encode("employerpass2");
        System.out.println("Hashed: " + hashed);
    }
}
