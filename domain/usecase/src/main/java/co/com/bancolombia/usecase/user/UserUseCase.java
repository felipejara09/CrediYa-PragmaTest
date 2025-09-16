package co.com.bancolombia.usecase.user;

import co.com.bancolombia.usecase.user.exception.*;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> save(User user) {
        return Mono.defer(() -> {
            String normalizedEmail = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();
            User toSave = user.toBuilder().email(normalizedEmail).build();

            validate(toSave);

            return userRepository.findByEmail(toSave.getEmail())
                    .flatMap(existing -> Mono.<User>error(new DuplicateEmailException("Email address already in use.")))
                    .switchIfEmpty(userRepository.save(toSave) );
        });
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email == null ? null : email.trim().toLowerCase());
    }

    private void validate(User u) {
        final Pattern EMAIL_RX = Pattern.compile("^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,63}$");
        final BigDecimal MIN_SAL = new BigDecimal("0");
        final BigDecimal MAX_SAL = new BigDecimal("15000000");

        required(u.getName(), "name");
        required(u.getLastName(), "lastName");
        required(u.getEmail(), "email");
        required(u.getIdentityNumber(), "identityNumber");


        if (!EMAIL_RX.matcher(u.getEmail()).matches()) {
            throw new InvalidEmailFormatException(u.getEmail());
        }
        if (u.getBaseSalary() == null) {
            throw new RequiredFieldMissingException("baseSalary");
        }
        if (u.getBaseSalary().compareTo(MIN_SAL) < 0 || u.getBaseSalary().compareTo(MAX_SAL) > 0) {
            throw new InvalidBaseSalaryRangeException(u.getBaseSalary());
        }
    }

    private void required(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new RequiredFieldMissingException(field);
    }

    public Mono<User> findByIdentityAndEmail(String identityNumber, String email) {
        return userRepository.findByIdentityAndEmail(identityNumber, email == null ? null : email.trim().toLowerCase());
    }
}