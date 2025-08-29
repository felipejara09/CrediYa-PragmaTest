package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.user.exception.DuplicateEmailException;
import co.com.bancolombia.usecase.user.exception.InvalidBaseSalaryRangeException;
import co.com.bancolombia.usecase.user.exception.InvalidEmailFormatException;
import co.com.bancolombia.usecase.user.exception.RequiredFieldMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;



public class UserUseCaseTest {
    private UserRepository repo;
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(UserRepository.class);
        useCase = new UserUseCase(repo);
    }

    private User validUser() {
        return User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Pérez")
                .email("juan.perez@test.com")
                .identityNumber("123")
                .dateBorn(LocalDate.of(1990,1,1))
                .address("CL 1 # 2-3")
                .phoneNumber("3000000000")
                .idRol(1L)
                .baseSalary(new BigDecimal("1000000"))
                .build();
    }

    @Test
    void save_ok_whenValidAndUniqueEmail() {
        User u = validUser();
        when(repo.findByEmail(eq(u.getEmail()))).thenReturn(Mono.empty());
        when(repo.save(any(User.class))).thenReturn(Mono.just(u));

        StepVerifier.create(useCase.save(u))
                .expectNext(u)
                .verifyComplete();

        verify(repo).findByEmail(u.getEmail());
        verify(repo).save(u);
    }

    @Test
    void save_fails_whenDuplicateEmail() {
        User u = validUser();

        when(repo.findByEmail(eq(u.getEmail()))).thenReturn(Mono.just(u));

        StepVerifier.create(useCase.save(u))
                .expectError(DuplicateEmailException.class)
                .verify();

        verify(repo).findByEmail(u.getEmail());
        verify(repo, never()).save(any());
    }

    @Test
    void save_fails_whenEmailFormatInvalid() {
        User u = validUser().toBuilder().email("mal-formato").build();

        StepVerifier.create(useCase.save(u))
                .expectError(InvalidEmailFormatException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void save_fails_whenBaseSalaryOutOfRange() {
        User u = validUser().toBuilder().baseSalary(new BigDecimal("20000000")).build();

        StepVerifier.create(useCase.save(u))
                .expectError(InvalidBaseSalaryRangeException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void save_fails_whenRequiredBlank() {
        User u = validUser().toBuilder().name("  ").build();

        StepVerifier.create(useCase.save(u))
                .expectError(RequiredFieldMissingException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

}
