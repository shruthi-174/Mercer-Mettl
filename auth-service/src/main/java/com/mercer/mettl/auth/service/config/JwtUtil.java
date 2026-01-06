package com.mercer.mettl.auth.service.config;

import com.mercer.mettl.auth.service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiry}")
    private long accessExpiry;

    @Value("${jwt.refresh.expiry}")
    private long refreshExpiry;

    @Value("${jwt.reset.expiry}")
    private long resetExpiry;

    private Key key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateAccessToken(User user, String role) {
        return buildToken(user, role, "ACCESS", accessExpiry);
    }

    public String generateRefreshToken(User user, String role) {
        return buildToken(user, role, "REFRESH", refreshExpiry);
    }

    private String buildToken(User user, String role, String type, long expiry) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setIssuer("auth-service")
                .setAudience("api-gateway")
                .claim("email", user.getEmail())
                .claim("role", role)
                .claim("tokenType", type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateResetToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("tokenType", "RESET")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + resetExpiry))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
