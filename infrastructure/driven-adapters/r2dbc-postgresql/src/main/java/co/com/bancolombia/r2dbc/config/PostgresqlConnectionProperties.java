package co.com.bancolombia.r2dbc.config;

// TODO: Load properties from the application.yaml file or from secrets manager
 import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.app.postgresql")
public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String username,
        String password) {
}
