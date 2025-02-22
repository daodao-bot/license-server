package cloud.daodao.license.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * @author DaoDao
 */
@Data
@Configuration
public class AppConfig {

    @Value("${spring.application.name:}")
    private String application;

    @Value("${spring.security.user.name:}")
    private String adminUsername;

    @Value("${spring.security.user.password:}")
    private String adminPassword;

    @Value("${spring.security.user.roles:ADMIN}")
    private String[] adminRoles;

    @Value("${app.admin-app-id:}")
    private String adminAppId;

    @Value("${app.admin-license:}")
    private String adminLicense;

    @Value("${app.admin-token-duration:}")
    private Duration adminTokenDuration;

    @Value("${app.api-security-enabled:}")
    private Boolean apiSecurityEnabled;

    @Value("${app.license-expires-warning-days:}")
    private Long[] licenseExpiresWarningDays;

    @Value("${app.license-expires-warning-email:}")
    private Boolean licenseExpiresWarningEmail;

}
