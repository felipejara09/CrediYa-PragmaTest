package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.auth.gateways.AuthUserRepository;
import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import co.com.bancolombia.usecase.auth.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class AuthUseCause {

    private final AuthUserRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final long expiresIn;

    public Mono<TokenResponse> login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return Mono.error(new InvalidCredentialsException());
        }
        return authRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
                .flatMap(principal -> {
                    if (!passwordEncoder.matches(password, principal.getPassword())) {
                        return Mono.error(new InvalidCredentialsException());
                    }
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("sub", principal.getId());
                    claims.put("email", principal.getEmail());
                    claims.put("role", principal.getIdRole());
                    String jwt = tokenProvider.generate(claims, expiresIn);
                    return Mono.just(new TokenResponse("Bearer", jwt, expiresIn));
                });
    }

    public record TokenResponse(String tokenType, String accessToken, long expiresIn) {}
}
