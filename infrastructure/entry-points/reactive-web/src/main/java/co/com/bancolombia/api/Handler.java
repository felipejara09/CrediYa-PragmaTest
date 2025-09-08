package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.CreateUserWithPasswordDTO;
import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.TokenDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
<<<<<<< HEAD
import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
=======
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
import co.com.bancolombia.usecase.auth.AuthUseCause;
import co.com.bancolombia.usecase.user.RegisterWithPasswordUseCase;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper mapper;
    private final AuthUseCause authUseCase;
<<<<<<< HEAD
    private final PasswordEncoder passwordEncoder;
=======
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
    private final RegisterWithPasswordUseCase registerWithPasswordUseCase;



    @Transactional
<<<<<<< HEAD
    public Mono<ServerResponse> register(ServerRequest req) {
        return req.bodyToMono(CreateUserDTO.class)
                .doOnNext(d -> log.info("[register] request email={} idRole={}", d.email(), d.idRole()))
                .map(dto -> {
                    var user = mapper.toDomain(dto);
                    if (dto.password() != null && !dto.password().isBlank()) {
                        var hash = passwordEncoder.hash(dto.password());
                        user = user.toBuilder().password(hash).build();
                    } else {
                        user = user.toBuilder().password(null).build();
                    }
                    return user;
                })
=======
    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(d -> log.info("[register] request email={} idRole={}", d.email(), d.idRole()))
                .map(mapper::toDomain)
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
                .flatMap(userUseCase::save)
                .doOnNext(u -> log.info("[register] saved id={} email={}", u.getId(), u.getEmail()))
                .map(mapper::toResponse)
                .flatMap(resp ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .header(HttpHeaders.LOCATION, "/api/v1/usuarios/" + resp.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(resp)
                );
    }

    public Mono<ServerResponse> verify(ServerRequest request) {
        String identityNumber = request.queryParam("identityNumber").orElse(null);
        String email = request.queryParam("email").orElse(null);
        log.info("Request received: identityNumber={}, email={}", identityNumber, email);
        if (identityNumber == null || email == null) {
            log.warn("Missing parameters: identityNumber={}, email={}", identityNumber, email);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("code", "BAD_REQUEST",
                            "message", "identityNumber and email are required"));
        }

        return userUseCase.findByIdentityAndEmail(identityNumber, email)
                .doOnNext(u -> log.info("User found: {}", u))
                .doOnError(e -> log.error("Error querying user with identityNumber={} and email={}", identityNumber, email, e))
                .flatMap(u -> ServerResponse.ok().build())
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.info("No user found with identityNumber={} and email={}", identityNumber, email);
                            return ServerResponse.notFound().build();
                        })
                );



    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .doOnNext(dto -> log.info("login.attempt email={}", dto.email()))
                .flatMap(dto -> authUseCase.login(dto.email(), dto.password()))
                .doOnNext(tr -> log.info("login.success"))
                .map(tr -> new TokenDTO(tr.tokenType(), tr.accessToken(), tr.expiresIn()))
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> registerWithPassword(ServerRequest req) {
        return req.bodyToMono(CreateUserWithPasswordDTO.class)
                .flatMap(dto -> {
                    var user = co.com.bancolombia.model.user.User.builder()
                            .name(dto.name()).lastName(dto.lastName()).email(dto.email())
                            .identityNumber(dto.identityNumber()).phoneNumber(dto.phoneNumber())
                            .dateBorn(dto.dateBorn()).address(dto.address())
                            .idRole(dto.idRole()).baseSalary(dto.baseSalary()).build();
                    return registerWithPasswordUseCase.register(user, dto.password());
                })
                .map(mapper::toResponse)
                .flatMap(resp -> ServerResponse.created(URI.create("/api/v1/usuarios/" + resp.id()))
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(resp));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .doOnNext(dto -> log.info("login.attempt email={}", dto.email()))
                .flatMap(dto -> authUseCase.login(dto.email(), dto.password()))
                .doOnSuccess(tr -> log.info("login.success"))
                .doOnError(e -> log.warn("login.failed {}", e.toString()))
                .map(tr -> new TokenDTO(tr.tokenType(), tr.accessToken(), tr.expiresIn()))
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
    }

<<<<<<< HEAD
=======
    public Mono<ServerResponse> registerWithPassword(ServerRequest req) {
        return req.bodyToMono(CreateUserWithPasswordDTO.class)
                .flatMap(dto -> {
                    var user = co.com.bancolombia.model.user.User.builder()
                            .name(dto.name()).lastName(dto.lastName()).email(dto.email())
                            .identityNumber(dto.identityNumber()).phoneNumber(dto.phoneNumber())
                            .dateBorn(dto.dateBorn()).address(dto.address())
                            .idRole(dto.idRol()).baseSalary(dto.baseSalary()).build();
                    return registerWithPasswordUseCase.register(user, dto.password());
                })
                .map(mapper::toResponse)
                .flatMap(resp -> ServerResponse.created(URI.create("/api/v1/usuarios/" + resp.id()))
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(resp));
    }


>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a


    public static final class BadRequestException extends RuntimeException {
        public final List<String> details;
        public BadRequestException(String message, List<String> details) {
            super(message);
            this.details = details;
        }
    }
}
