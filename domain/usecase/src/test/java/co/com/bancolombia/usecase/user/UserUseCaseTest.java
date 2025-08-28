package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserUseCase useCase; // ← Mockito crea el use case inyectando el mock via constructor

    @Test
    @DisplayName("save delega al repositorio y retorna el usuario guardado")
    void save_success() {
        User input = sample(null);
        User saved = input.toBuilder().id(1L).build();

        when(userRepository.save(input)).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.save(input))
                .expectNext(saved)
                .verifyComplete();

        verify(userRepository).save(input);
        verifyNoMoreInteractions(userRepository);
    }


    private User sample(Long id) {
        return User.builder()
                .id(id).name("Alice").lastName("Doe").email("alice@example.com")
                .identityNumber("123").dateBorn(LocalDate.of(1990,1,1))
                .address("Main 1").phoneNumber("3001234567").idRol("USER")
                .baseSalary(new BigDecimal("1000.00"))
                .build();
    }

}
