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
            validate(user);

            return userRepository.findByEmail(user.getEmail())
                    .flatMap(existing -> Mono.<User>error(new DuplicateEmailException("Email address already in use.")))
                    .switchIfEmpty(Mono.defer(() -> userRepository.save(user)));
        });
    }

    public Mono<User>findByEmail(String email){return userRepository.findByEmail(email);}

    private void validate(User u) {
        final Pattern EMAIL_RX = Pattern.compile("^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,63}$");
        final BigDecimal MIN_SAL = new BigDecimal("0");
        final BigDecimal MAX_SAL = new BigDecimal("15000000");

        required(u.getName(), "name");
        required(u.getLastName(), "lastName");
        required(u.getEmail(), "email");
        required(u.getIdentityNumber(),"identityNumber");
        if (u.getBaseSalary() == null) throw new RequiredFieldMissingException("baseSalary");
        if (!EMAIL_RX.matcher(u.getEmail()).matches()) {
            throw new InvalidEmailFormatException(u.getEmail());
        }
        if (u.getBaseSalary().compareTo(MIN_SAL) < 0 || u.getBaseSalary().compareTo(MAX_SAL) > 0) {throw new InvalidBaseSalaryRangeException(u.getBaseSalary());
        }


    }

    private void required(String value, String field) {if (value == null || value.trim().isEmpty()) throw new RequiredFieldMissingException(field);}

    public Mono<User> findByIdentityAndEmail(String identityNumber, String email) {
        return userRepository.findByIdentityAndEmail(identityNumber, email);
    }


}
