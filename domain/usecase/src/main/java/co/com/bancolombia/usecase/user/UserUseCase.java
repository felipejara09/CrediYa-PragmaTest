package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> save(User user){
        return userRepository.save(user);
    }

    public Mono<User>  findByEmail(String email){
        return userRepository.findByEmail(email);
    }

}

/*

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository repository;

    public Mono<Object> register(User user){

        if (user.getNombres() == null || user.getNombres().isBlank()) {
            return Mono.error(new IllegalArgumentException("Nombres no pueden ser vacíos"));
        }
        if (user.getCorreoElectronico() == null) {
            return Mono.error(new IllegalArgumentException("Correo electrónico requerido"));
        }
        if (user.getSalarioBase() == null || user.getSalarioBase() <= 0) {
            return Mono.error(new IllegalArgumentException("Salario base inválido"));
        }

        return repository.findByCorreoElectronico(user.getCorreoElectronico())
                .flatMap(existing -> Mono.error(new IllegalArgumentException("Correo ya registrado")))
                .switchIfEmpty(repository.save(user));
    }
}*/