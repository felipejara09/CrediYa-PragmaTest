package co.com.bancolombia.api.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("Authentication API")
                .version("v1")
                .description("Microservicio de autenticación - WebFlux + R2DBC"));
    }
}
