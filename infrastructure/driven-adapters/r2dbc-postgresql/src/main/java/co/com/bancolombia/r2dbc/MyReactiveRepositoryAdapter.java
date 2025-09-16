package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import co.com.bancolombia.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
=======
=======
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.repository.UserReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Repository
public class MyReactiveRepositoryAdapter implements UserRepository {

    private final UserReactiveRepository repository;
    private final UserMapper mapper;


    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
        log.info("[repo] incoming entity.id={} email={}", entity.getId(), entity.getEmail());
        return repository.save(entity)
                .doOnSuccess(v -> log.info("[repo] insert completed for email={}", entity.getEmail()))
                .then(repository.findByEmail(entity.getEmail()))
                .switchIfEmpty(Mono.error(new IllegalStateException("insert didn’t return row")))
                .map(mapper::toDomain);
    }
    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByIdentityAndEmail(String identityNumber, String email) {
        return repository.findByIdentityNumberAndEmail(identityNumber, email)
                .map(mapper::toDomain);
    }

}
