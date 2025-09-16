package co.com.bancolombia.model.auth.gateways;

import co.com.bancolombia.model.auth.AuthUser;
import reactor.core.publisher.Mono;

public interface AuthUserRepository {
    Mono<AuthUser> findByEmail(String email);

}
