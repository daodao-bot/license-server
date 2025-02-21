package cloud.daodao.license.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author DaoDao
 */
@Data
@Configuration
public class CommonConfig {

    @Value("${spring.application.name:}")
    private String application;

    @Value("${app.slogan:}")
    private String slogan;

    @Value("${app.aes-key:0000000000000000}")
    private String aesKey;

    @Value("${app.aes-iv:0000000000000000}")
    private String aesIv;

}
