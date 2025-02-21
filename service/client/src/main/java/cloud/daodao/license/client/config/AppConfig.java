package cloud.daodao.license.client.config;

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

    @Value("${app.license-server:}")
    private String licenseServer;

    @Value("${app.license-app-id:}")
    private String licenseAppId;

    @Value("${app.license-code:}")
    private String licenseCode;


}
