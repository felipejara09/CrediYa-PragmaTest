package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository>
        implements UserRepository {

    private final UserReactiveRepository repository;


    public MyReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        // 👇 aquí indicamos cómo convertir entity → domain
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.repository = repository;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        // usamos la conversión que ya trae ReactiveAdapterOperations
        return repository.findByEmail(email)
                .map(this::toEntity);
    }

}
