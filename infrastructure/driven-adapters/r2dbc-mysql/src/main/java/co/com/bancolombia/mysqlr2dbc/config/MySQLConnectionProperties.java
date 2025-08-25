package co.com.bancolombia.mysqlr2dbc.config;

// TODO: Load properties from the application.yaml file or from secrets manager
// import org.springframework.boot.context.properties.ConfigurationProperties;

// @ConfigurationProperties(prefix = "adapters.r2dbc")

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.datasource")
public class MySQLConnectionProperties {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
}
