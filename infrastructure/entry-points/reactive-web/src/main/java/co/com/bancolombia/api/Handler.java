package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
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


    @Transactional
    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)

                .map(mapper::toDomain)
                .flatMap(userUseCase::save)
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


    public Mono<ServerResponse> verifyPost(ServerRequest request) {
        return request.bodyToMono(VerifyRequest.class)
                .flatMap(r -> {
                    if (r.identificationNumber() == null || r.email() == null) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("code", "BAD_REQUEST",
                                        "message", "identificationNumber and email are required"));
                    }
                    return userUseCase.findByIdentityAndEmail(r.identificationNumber(), r.email())
                            .flatMap(u -> ServerResponse.ok().build())
                            .switchIfEmpty(ServerResponse.notFound().build());
                });
    }
    public Mono<ServerResponse> verifyByEmail(ServerRequest request) {
        String email = request.queryParam("email").orElse(null);
        if (email == null || email.isBlank()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(java.util.Map.of("code","BAD_REQUEST","message","email is required"));
        }
        return userUseCase.findByEmail(email)
                .flatMap(u -> ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public record VerifyRequest(String identificationNumber, String email) {}

    public static final class BadRequestException extends RuntimeException {
        public final List<String> details;
        public BadRequestException(String message, List<String> details) {
            super(message);
            this.details = details;
        }
    }
}
