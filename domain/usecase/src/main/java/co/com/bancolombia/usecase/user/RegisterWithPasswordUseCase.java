package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.auth.gateways.CredentialsRepository;
import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import co.com.bancolombia.model.user.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterWithPasswordUseCase {

    private final UserUseCase userUseCase;
    private final CredentialsRepository credentialsRepo;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> register(User user, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return Mono.error(new IllegalArgumentException("password is required"));
        }
        return userUseCase.save(user)
                .flatMap(saved -> {
                    String hash = passwordEncoder.hash(rawPassword);
                    return credentialsRepo.setPasswordHashByUserId(saved.getId(), hash).thenReturn(saved);
                });
    }
}
