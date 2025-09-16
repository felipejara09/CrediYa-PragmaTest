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
                .idRole(1L)
                .baseSalary(new BigDecimal("1000000"))
                .build();
    }

    @Test
    void shouldSaveUserWhenValidAndUniqueEmail() {
        User u = validUser().toBuilder().email("  JOHN.DOE@test.com ").build();

        when(repo.findByEmail("john.doe@test.com")).thenReturn(Mono.empty());
        when(repo.save(any(User.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.save(u))
                .expectNextMatches(saved -> "john.doe@test.com".equals(saved.getEmail()))
                .verifyComplete();

        verify(repo).findByEmail("john.doe@test.com");
        verify(repo).save(argThat(s -> "john.doe@test.com".equals(s.getEmail())));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        User u = validUser();
        when(repo.findByEmail(u.getEmail())).thenReturn(Mono.just(u));
        // con Mono.defer en producción ya no hace falta stubbear save

        StepVerifier.create(useCase.save(u))
                .expectError(DuplicateEmailException.class)
                .verify();

        verify(repo).findByEmail(u.getEmail());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldFailWhenEmailHasInvalidFormat() {
        User u = validUser().toBuilder().email("invalid-format").build();

        StepVerifier.create(useCase.save(u))
                .expectError(InvalidEmailFormatException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldFailWhenBaseSalaryIsOutOfRange() {
        User u = validUser().toBuilder().baseSalary(new BigDecimal("20000000")).build();

        StepVerifier.create(useCase.save(u))
                .expectError(InvalidBaseSalaryRangeException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldFailWhenRequiredFieldIsBlank() {
        User u = validUser().toBuilder().name("  ").build();

        StepVerifier.create(useCase.save(u))
                .expectError(RequiredFieldMissingException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldFailWhenLastNameIsBlank() {
        User u = validUser().toBuilder().lastName("  ").build();

        StepVerifier.create(useCase.save(u))
                .expectError(RequiredFieldMissingException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldFailWhenIdentityNumberIsBlank() {
        User u = validUser().toBuilder().identityNumber(" ").build();

        StepVerifier.create(useCase.save(u))
                .expectError(RequiredFieldMissingException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }

    @Test
    void shouldNormalizeEmailOnFindByEmailParameter() {
        User stored = validUser(); // el repo retorna lo que tenga
        when(repo.findByEmail("john.doe@test.com")).thenReturn(Mono.just(stored));

        StepVerifier.create(useCase.findByEmail("  JOHN.DOE@test.com "))
                .expectNext(stored)
                .verifyComplete();

        verify(repo).findByEmail("john.doe@test.com");
    }

    @Test
    void shouldFindByIdentityAndEmailWithNormalizedEmail() {
        User stored = validUser();
        when(repo.findByIdentityAndEmail("123", "john.doe@test.com"))
                .thenReturn(Mono.just(stored));

        StepVerifier.create(useCase.findByIdentityAndEmail("123", "  JOHN.DOE@test.com "))
                .expectNext(stored)
                .verifyComplete();

        verify(repo).findByIdentityAndEmail("123", "john.doe@test.com");
    }

    @Test
    void shouldPassWithSalaryOnBounds() {
        when(repo.findByEmail(anyString())).thenReturn(Mono.empty());
        when(repo.save(any(User.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.save(validUser().toBuilder().baseSalary(new BigDecimal("0")).build()))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(useCase.save(validUser().toBuilder().baseSalary(new BigDecimal("15000000")).build()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenBaseSalaryIsNull() {
        User u = validUser().toBuilder().baseSalary(null).build();

        StepVerifier.create(useCase.save(u))
                .expectError(RequiredFieldMissingException.class)
                .verify();

        verify(repo, never()).findByEmail(any());
        verify(repo, never()).save(any());
    }


}
