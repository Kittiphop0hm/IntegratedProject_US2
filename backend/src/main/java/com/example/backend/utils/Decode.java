package com.example.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class Decode {
    public Decode() {}

    public String getUserEmailByRefreshToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT");
        }

        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> payloadObject = objectMapper.readValue(payloadJson, Map.class);
            return payloadObject.get("sub");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
