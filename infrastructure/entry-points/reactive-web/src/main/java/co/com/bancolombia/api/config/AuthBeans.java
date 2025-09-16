package co.com.bancolombia.api.config;

import co.com.bancolombia.model.auth.gateways.AuthUserRepository;
import co.com.bancolombia.model.auth.gateways.CredentialsRepository;
import co.com.bancolombia.model.auth.gateways.PasswordEncoder;
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import co.com.bancolombia.usecase.auth.AuthUseCause;
import co.com.bancolombia.usecase.user.RegisterWithPasswordUseCase;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthBeans {

    @Bean
    public AuthUseCause authUseCase(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider,
            @Value("${jwt.expires-in-seconds:3600}") long expiresIn) {
        return new AuthUseCause(authUserRepository, passwordEncoder, tokenProvider, expiresIn);
    }

    @Bean
    public RegisterWithPasswordUseCase registerWithPasswordUseCase(
            UserUseCase userUseCase,
            CredentialsRepository credentialsRepository,
            PasswordEncoder passwordEncoder) {
        return new RegisterWithPasswordUseCase(userUseCase, credentialsRepository, passwordEncoder);
    }
}