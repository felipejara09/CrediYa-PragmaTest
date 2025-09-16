package co.com.bancolombia.api;


import co.com.bancolombia.api.security.RequireAuth;

import io.swagger.v3.oas.annotations.Operation;

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
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            summary = "Autenticación",
                            description = "Devuelve un JWT",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.LoginDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK",
                                            content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.TokenDTO.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "register",
                    operation = @Operation(
                            summary = "Crear usuario",
                            security = {@SecurityRequirement(name = "bearerAuth")},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.CreateUserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Creado",
                                            content = @Content(schema = @Schema(implementation = co.com.bancolombia.api.dto.UserDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/verify",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "verify",
                    operation = @Operation(
                            summary = "Verificar identidad/email",
                            security = {@SecurityRequirement(name = "bearerAuth")},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK"),
                                    @ApiResponse(responseCode = "404", description = "No encontrado")
                            }
                    )
            )
    })

    public RouterFunction<ServerResponse> routerFunction(Handler handler, RequireAuth auth) {

        var login = route(POST("/api/v1/login"), handler::login);
        var createProtectedUser = route(POST("/api/v1/usuarios"), handler::register).filter(auth.requireRoles(1L, 2L));
        var verify = route(GET("/api/v1/usuarios/verify"), handler::verify).filter(auth.requireRoles(1L, 2L, 3L));

        return login.and(createProtectedUser).and(verify);

    }
}
