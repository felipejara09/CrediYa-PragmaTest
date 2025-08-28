package co.com.bancolombia.api.error;

import co.com.bancolombia.api.Handler.BadRequestException;

import co.com.bancolombia.usecase.user.exception.DuplicateEmailException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.util.AsciiString.contains;
import static io.netty.util.AsciiString.containsIgnoreCase;
import static co.com.bancolombia.api.helpers.ErrorHelpers.*;

@Slf4j
@RestControllerAdvice
@Order(0)
public class GlobalErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorBody> onValidation(BadRequestException ex) {
        log.debug("400 - {}", ex.getMessage());
        return Mono.just(ErrorBody.of("BAD_REQUEST", ex.getMessage(), ex.details, 400));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorBody> onDecode(ServerWebInputException ex) {
        log.debug("The payload is invalid", ex);
        return Mono.just(ErrorBody.of("BAD_REQUEST",
                "Invalid request payload: structure or data types are incorrect", List.of(), 400));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<ErrorBody>> onDataIntegrity(DataIntegrityViolationException ex) {
        String deep = deepMessage(ex);
        log.warn("Integrity constraint violation: {}", deep);


        if (contains(deep, "usuario_email_key") || contains(deep, "uq_usuario_email")) {
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorBody.of("CONFLICT", "Email address already in use.", List.of(), 409)));
        }
        if (contains(deep, "usuario_identity_number_key") || contains(deep, "uq_usuario_identity")) {
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorBody.of("CONFLICT", "This ID number is already associated with an account.", List.of(), 409)));
        }


        if (contains(deep, "23505")
                || containsIgnoreCase(deep, "duplicate key value")
                || containsIgnoreCase(deep, "restricción de unicidad")
                || containsIgnoreCase(deep, "unique constraint")) {
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorBody.of("CONFLICT", "Violación de restricción de unicidad", List.of(), 409)));
        }


        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorBody.of("INTERNAL_ERROR",
                        "Internal error processing request.", List.of(), 500)));
    }


    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorBody> onBadSql(BadSqlGrammarException ex) {
        log.error("SQL execution error", ex);
        return Mono.just(ErrorBody.of("INTERNAL_ERROR",
                "Internal error processing request.", List.of(), 500));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ErrorBody> onDuplicate(DuplicateEmailException ex) {
        return Mono.just(ErrorBody.of("CONFLICT", "Email address already in use.", List.of(), 409));
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorBody> onAny(Throwable ex) {
        log.error("Unhandled exception", ex);
        return Mono.just(ErrorBody.of("INTERNAL_ERROR",
                "Unexpected error.", List.of(), 500));
    }


}