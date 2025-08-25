package co.com.bancolombia.api;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Object> registrar(@RequestBody User user) {
        return userCase.register(user);
    }
}
