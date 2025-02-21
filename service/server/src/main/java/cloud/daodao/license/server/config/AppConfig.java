package cloud.daodao.license.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author DaoDao
 */
@Data
@Configuration
public class AppConfig {

    @Value("${spring.application.name:}")
    private String application;

    @Value("${app.license-expires-warning-days:}")
    private List<Long> licenseExpiresWarningDays;

    @Value("${app.license-expires-warning-email:}")
    private Boolean licenseExpiresWarningEmail;

}
