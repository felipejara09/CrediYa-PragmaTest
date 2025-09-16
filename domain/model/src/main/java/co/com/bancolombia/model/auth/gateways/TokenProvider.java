package co.com.bancolombia.model.auth.gateways;

import java.util.Map;

public interface TokenProvider {
    String generate(Map<String, Object> claims, long expiresInSeconds);
    Map<String, Object> verify(String token);
}
