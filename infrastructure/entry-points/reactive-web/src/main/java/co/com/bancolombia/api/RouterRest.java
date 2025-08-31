package co.com.bancolombia.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios")
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    operation = @Operation(
                            summary = "Registrar usuario",
                            description = "Crea un nuevo usuario",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true
                            ),
                            tags = {"Usuarios"}
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::register)
                .andRoute(GET("/api/v1/usuarios/verify-email"), handler::verifyByEmail)
                .andRoute(GET("/ping"), req -> ServerResponse.ok().bodyValue("pong"));
    }
}
