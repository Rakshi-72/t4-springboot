package com.rakshi.t4.config;

import com.rakshi.t4.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsService service;

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.time.expiration}")
    Long expiration;

    private static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public User extractCurrentUser() {
        return (User) getContext().getAuthentication().getPrincipal();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuedAt(new Date())
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public void validateToken(String token) {
        String userName = this.extractUserName(token);
        if (Objects.nonNull(userName) && isTokenNonExpired(token)
                && getContext().getAuthentication() == null) {
            UserDetails details = service.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
            getContext().setAuthentication(authToken);
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = this.extractAllClaims(token);
        return resolver.apply(claims);
    }

    private String extractUserName(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    private Boolean isTokenNonExpired(String token) {
        return this.extractClaim(token, Claims::getExpiration).after(new Date());
    }
}
