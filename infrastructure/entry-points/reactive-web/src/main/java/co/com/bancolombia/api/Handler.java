package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.CreateUserWithPasswordDTO;
import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.TokenDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.usecase.auth.AuthUseCause;
import co.com.bancolombia.usecase.user.RegisterWithPasswordUseCase;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RegisterWithPasswordUseCase registerWithPasswordUseCase;



    @Transactional
    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(d -> log.info("[register] request email={} idRole={}", d.email(), d.idRole()))
                .map(mapper::toDomain)
                .flatMap(userUseCase::save)
                .doOnNext(u -> log.info("[register] saved id={} email={}", u.getId(), u.getEmail()))
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse
                        .created(URI.create("/api/v1/usuarios/" + response.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> verify(ServerRequest request) {
        String identityNumber = request.queryParam("identityNumber").orElse(null);
        String email = request.queryParam("email").orElse(null);

        if (identityNumber == null || email == null) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("code", "BAD_REQUEST",
                            "message", "identityNumber and email are required"));
        }

        return userUseCase.findByIdentityAndEmail(identityNumber, email)
                .flatMap(u -> ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
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




    public static final class BadRequestException extends RuntimeException {
        public final List<String> details;
        public BadRequestException(String message, List<String> details) {
            super(message);
            this.details = details;
        }
    }
}
