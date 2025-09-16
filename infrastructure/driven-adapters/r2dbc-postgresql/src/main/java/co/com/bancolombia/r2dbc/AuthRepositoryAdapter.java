package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.auth.AuthUser;
import co.com.bancolombia.model.auth.gateways.AuthUserRepository;
import co.com.bancolombia.r2dbc.mapper.AuthMapper;
import co.com.bancolombia.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthUserRepository {

    private final UserReactiveRepository repository;
    private final AuthMapper mapper;

    @Override
    public Mono<AuthUser> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toAuth);
    }
}
