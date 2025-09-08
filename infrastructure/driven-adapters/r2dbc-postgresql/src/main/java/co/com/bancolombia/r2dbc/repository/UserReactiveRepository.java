package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository
        extends ReactiveCrudRepository<UserEntity, Long>,
        ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmail(String email);

    Mono<UserEntity> findByIdentityNumberAndEmail(String identityNumber, String email);

    @Modifying
    @Query("UPDATE users SET password = :password WHERE id_user = :id")
    Mono<Integer> updatePasswordById(@Param("id") Long id, @Param("password") String password);

}
