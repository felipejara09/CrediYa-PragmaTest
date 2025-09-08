package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.security.RequireAuth;
import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios")
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, RequireAuth auth) {

        var login = route(POST("/api/v1/login"), handler::login);
        var crearUsuarioProtegida =
                route(POST("/api/v1/usuarios"), handler::register)
                .filter(auth.requireRoles(1L, 2L));

        var verify = route(GET("/api/v1/usuarios/verify"), handler::verify)
                .filter(auth.requireRoles(1L, 2L, 3L));

        return login.and(crearUsuarioProtegida).and(verify);
    }


}
