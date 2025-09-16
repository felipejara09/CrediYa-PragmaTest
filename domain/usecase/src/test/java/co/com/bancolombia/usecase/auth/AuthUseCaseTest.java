package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.auth.AuthUser;
import co.com.bancolombia.model.auth.gateways.AuthUserRepository;
import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import co.com.bancolombia.usecase.auth.exception.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthUseCaseTest {


    @Mock AuthUserRepository authRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenProvider tokenProvider;


    AuthUseCause useCase;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new AuthUseCause(authRepo, passwordEncoder, tokenProvider, 3600);
    }


    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        var user = AuthUser.builder()
                .id(10L)
                .email("john.doe@test.com")
                .password("hashed")
                .idRole(1L)
                .build();

        when(authRepo.findByEmail("john.doe@test.com")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(tokenProvider.generate(anyMap(), eq(3600L))).thenReturn("jwt-token");

        StepVerifier.create(useCase.login("john.doe@test.com", "secret"))
                .expectNextMatches(tr -> tr.accessToken().equals("jwt-token"))
                .verifyComplete();

        verify(tokenProvider).generate(
                argThat((Map<String,Object> m) ->
                        m.get("sub").equals(10L) &&
                                m.get("email").equals("john.doe@test.com") &&
                                m.get("role").equals(1L)
                ),
                eq(3600L)
        );
    }

    @Test
    void shouldFailWhenEmailOrPasswordIsBlank() {
        StepVerifier.create(useCase.login("", " "))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        when(authRepo.findByEmail("no@exists.com")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.login("no@exists.com", "pass"))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    void shouldFailWhenPasswordIsIncorrect() {
        var user = AuthUser.builder()
                .id(1L)
                .email("john.doe@test.com")
                .password("hashed")
                .idRole(2L)
                .build();

        when(authRepo.findByEmail("john.doe@test.com")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        StepVerifier.create(useCase.login("john.doe@test.com", "wrong"))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
}