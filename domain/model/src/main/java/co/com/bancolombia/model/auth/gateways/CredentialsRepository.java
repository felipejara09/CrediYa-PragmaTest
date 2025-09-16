package co.com.bancolombia.model.auth.gateways;

import reactor.core.publisher.Mono;

public interface CredentialsRepository {
    Mono<Void> setPasswordHashByUserId(Long userId, String password);
}
