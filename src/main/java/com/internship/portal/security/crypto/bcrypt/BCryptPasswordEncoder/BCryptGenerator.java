package com.internship.portal.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {

    public static void main(String[] args) {
        String hashed = new BCryptPasswordEncoder().encode("employerpass2");
        System.out.println("Hashed: " + hashed);
    }
}
