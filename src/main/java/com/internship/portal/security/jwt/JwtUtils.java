package com.internship.portal.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpirationMs}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate a token with the email as the subject
    public String generateJwtToken(Authentication authentication) {
        // Here, we assume that the email is stored in the UserDetails's username field.
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Optionally log the exception
        }
        return false;
    }

    // Extract the email from the token (stored in the subject)
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // For compatibility with existing filters, you can also provide:
    public String getEmailFromJwtToken(String token) {
        return extractEmail(token);
    }
}
