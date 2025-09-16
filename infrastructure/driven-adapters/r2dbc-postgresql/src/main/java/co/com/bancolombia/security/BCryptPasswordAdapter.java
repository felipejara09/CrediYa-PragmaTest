package co.com.bancolombia.security;

import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class BCryptPasswordAdapter implements PasswordEncoder {
    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
    @Override
    public String hash(String raw) {
        return delegate.encode(raw);
    }
    @Override
    public boolean matches(String raw, String hashed) {
        return delegate.matches(raw, hashed);
    }
<<<<<<< HEAD
=======
=======
    @Override public String hash(String raw) { return delegate.encode(raw); }
    @Override public boolean matches(String raw, String hashed) { return delegate.matches(raw, hashed); }
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
}