package co.com.bancolombia.mysqlr2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserDataRepository extends ReactiveCrudRepository<UserData, Long> {

    Mono<UserData> findByCorreoElectronico(String correo);
}
