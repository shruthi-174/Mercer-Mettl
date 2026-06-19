package com.mercer.mettl.assessment.service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.security.Key;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {

    private static final String SECRET =
            "bXlfc2VjcmV0X2tleV9teV9zZWNyZXRfa2V5X3Byb2R1Y3Rpb25fdXNlX3N0cm9uZ2VyX2tleV8yNTZfYml0c19sb25n";

    private Key getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Integer userId, Long testId) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("testId", testId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}