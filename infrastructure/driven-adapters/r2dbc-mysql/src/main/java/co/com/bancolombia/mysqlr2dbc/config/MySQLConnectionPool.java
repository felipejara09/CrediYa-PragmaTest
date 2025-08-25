package co.com.bancolombia.mysqlr2dbc.config;

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySQLConnectionPool {

    private final MySQLConnectionProperties properties;

    public MySQLConnectionPool(MySQLConnectionProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(
                MySqlConnectionConfiguration.builder()
                        .host(properties.getHost())
                        .port(properties.getPort())
                        .username(properties.getUsername())
                        .password(properties.getPassword())
                        .database(properties.getDatabase())
                        .build()
        );
    }
}