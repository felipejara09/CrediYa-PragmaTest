package co.com.bancolombia.api.dto;

public record TokenDTO(String tokenType, String accessToken, long expiresIn) {}
