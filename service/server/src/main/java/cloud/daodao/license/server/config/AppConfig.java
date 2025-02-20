package cloud.daodao.license.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author DaoDao
 */
@Data
@Configuration
public class AppConfig {

    @Value("${spring.application.name:}")
    private String application;

}
