package com.example.backend.services.users;

import com.example.backend.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJwtToken(Integer userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY,SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println("jwt: " + user.getNickName());
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getUserType());
        claims.put("nickname", user.getNickName());
        claims.put("typ", "ACCESS_TOKEN");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("https://intproj24.sit.kmutt.ac.th/us2/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Integer extractUserId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("userId", Integer.class);
    }

    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public void verifyToken(String token) {
//        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

//        } catch (ExpiredJwtException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired", e);
//        } catch (MalformedJwtException  | UnsupportedJwtException | IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token", e);
//        } catch (SignatureException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature", e);
//        }

    }

    public boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


//    public Boolean isValidClaims(Map<String,Object> jwtClaims) {
//        System.out.println(jwtClaims);
//        return jwtClaims.containsKey("iat")
//                && "https://intproj24.sit.kmutt.ac.th/us2/"
//                .equals(jwtClaims.get("iss"))
//                        && jwtClaims.containsKey("uid")
//                        && (Long) jwtClaims.get("uid") > 0 ;
//    }

    public Boolean isValidClaims(Claims claims) {
        System.out.println("URI: " + claims.getSubject());
        String issuer = claims.getIssuer();
        return issuer != null
                && issuer.equals("https://intproj24.sit.kmutt.ac.th/us2/")
                && claims.containsKey("id")
                && claims.get("id", Integer.class) > 0;
    }






}