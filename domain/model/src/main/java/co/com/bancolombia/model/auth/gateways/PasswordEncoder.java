package co.com.bancolombia.model.auth.gateways;

public interface PasswordEncoder {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}
