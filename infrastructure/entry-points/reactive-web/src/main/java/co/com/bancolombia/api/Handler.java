package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import jakarta.validation.ConstraintViolation;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.usecase.user.UserUseCase;
import jakarta.validation.Validator;
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


@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper mapper;
    private final Validator validator;

    @Transactional
    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .flatMap(this::validate)
                .map(mapper::toDomain)
                .flatMap(userUseCase::save)
                .map(mapper::toResponse)
                .flatMap(response -> {
                    log.info("User created with id={}", response.id());
                    return ServerResponse
                            .created(URI.create("/api/v1/usuarios/" + response.id()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    private Mono<CreateUserDTO> validate(CreateUserDTO dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            List<String> details = violations.stream()
                    .map(this::fmt)
                    .toList();
            return Mono.error(new BadRequestException("Invalid input", details));
        }
        return Mono.just(dto);
    }

    private String fmt(ConstraintViolation<?> v) {
        return v.getPropertyPath() + ": " + v.getMessage();
    }


    public static final class BadRequestException extends RuntimeException {
        public final List<String> details;
        public BadRequestException(String message, List<String> details) {
            super(message);
            this.details = details;
        }
    }
}
