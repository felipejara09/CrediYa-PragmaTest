package co.com.bancolombia.security;

import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class BCryptPasswordAdapter implements PasswordEncoder {
    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();
    @Override public String hash(String raw) { return delegate.encode(raw); }
    @Override public boolean matches(String raw, String hashed) { return delegate.matches(raw, hashed); }
}