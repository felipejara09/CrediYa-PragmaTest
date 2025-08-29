package co.com.bancolombia.api.error;

import co.com.bancolombia.api.Handler.BadRequestException;

import co.com.bancolombia.usecase.user.exception.DuplicateEmailException;

import co.com.bancolombia.usecase.user.exception.InvalidBaseSalaryRangeException;
import co.com.bancolombia.usecase.user.exception.InvalidEmailFormatException;
import co.com.bancolombia.usecase.user.exception.RequiredFieldMissingException;
import co.com.bancolombia.usecase.user.exception.DuplicatIdentityNumber;
import lombok.extern.slf4j.Slf4j;
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
public class GlobalErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorBody> onBadRequest(BadRequestException ex) {
        log.debug("400 - {}", ex.getMessage());
        return Mono.just(ErrorBody.of("BAD_REQUEST", ex.getMessage(), ex.details, 400));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorBody> onDecode(ServerWebInputException ex) {
        log.debug("Invalid payload", ex);
        return Mono.just(ErrorBody.of("BAD_REQUEST",
                "Invalid request payload: structure or data types are incorrect", List.of(), 400));
    }

    @ExceptionHandler({
            RequiredFieldMissingException.class,
            InvalidEmailFormatException.class,
            InvalidBaseSalaryRangeException.class,
            DuplicateEmailException.class,
            DuplicatIdentityNumber.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorBody> onDomainValidation(RuntimeException ex) {
        log.debug("Domain validation failed: {}", ex.getMessage());
        return Mono.just(ErrorBody.of("BAD_REQUEST", ex.getMessage(), List.of(), 400));
    }

    /*@ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ErrorBody> onDuplicate(DuplicateEmailException ex) {
        return Mono.just(ErrorBody.of("CONFLICT", "Email address already in use.", List.of(), 409));
    }*/

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorBody> onBadSql(BadSqlGrammarException ex) {
        log.error("SQL execution error", ex);
        return Mono.just(ErrorBody.of("INTERNAL_ERROR",
                "Internal error processing request.", List.of(), 500));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<ErrorBody>> onDataIntegrity(DataIntegrityViolationException ex) {
        String deep = deepMessage(ex);
        log.warn("Integrity constraint violation: {}", deep);


        if (contains(deep, "users_identity_number_key") || contains(deep, "uq_users_identity")) {
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



}