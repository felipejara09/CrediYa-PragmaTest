package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.auth.gateways.CredentialsRepository;
import co.com.bancolombia.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CredentialsRepositoryAdapter implements CredentialsRepository {

    private final UserReactiveRepository repository;

    @Override
    public Mono<Void> setPasswordHashByUserId(Long userId, String passwordHash) {
        return repository.updatePasswordById(userId, passwordHash)
                .then();
    }
}