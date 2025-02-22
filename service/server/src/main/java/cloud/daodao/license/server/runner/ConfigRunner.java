package cloud.daodao.license.server.runner;

import cloud.daodao.license.common.config.CommonConfig;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.server.config.AppConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
public class ConfigRunner implements ApplicationRunner {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private AppConfig appConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在检查参数配置...");
        log.info("commonConfig: {}", commonConfig);
        log.info("appConfig: {}", appConfig);

        String aesKey = commonConfig.getAesKey();
        String aesIv = commonConfig.getAesIv();
        if (aesKey == null || aesKey.isEmpty() || aesIv == null || aesIv.isEmpty()) {
            throw new AppException(ServerError.CONFIG_ERROR, "AES 配置不能为空!!!");
        }
        if (aesKey.equals("0000000000000000") || aesIv.equals("0000000000000000")) {
            log.warn("AES 配置使用了不安全的默认值!!! 强烈建议修改!!!");
        }
        String aesRegex = "^[0-9A-F]{16}$";
        if (!aesKey.matches(aesRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "AES_KEY 必须匹配正则 " + aesRegex + " !!!");
        }
        if (!aesIv.matches(aesRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "AES_IV 必须匹配正则 " + aesRegex + " !!!");
        }

        String adminUsername = appConfig.getAdminUsername();
        String adminUsernameRegex = "^[0-9A-Za-z-_.]{1,32}$";
        if (adminUsername == null || adminUsername.isEmpty() || !adminUsername.matches(adminUsernameRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_USERNAME 必须匹配正则 " + adminUsernameRegex + " !!!");
        }

        String adminPassword = appConfig.getAdminPassword();
        String adminPasswordRegex = "^[0-9A-Za-z-_.]{1,16}$";
        if (adminPassword == null || adminPassword.isEmpty() || !adminPassword.matches(adminPasswordRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_PASSWORD 必须匹配正则 " + adminPasswordRegex + " !!!");
        }

        String adminAppId = appConfig.getAdminAppId();
        String adminAppIdRegex = "^[0-9A-F]{32}$";
        if (adminAppId == null || adminAppId.isEmpty()) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_APP_ID 不能为空!!!");
        }
        if (adminAppId.equals("00000000000000000000000000000000")) {
            log.warn("ADMIN_APP_ID 使用了不安全的默认值!!! 强烈建议修改!!!");
        }
        if (!adminAppId.matches(adminAppIdRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_APP_ID 必须匹配正则 " + adminAppIdRegex + " !!!");
        }

        String adminLicense = appConfig.getAdminLicense();
        String adminLicenseRegex = "^[0-9A-F]{32}$";
        if (adminLicense == null || adminLicense.isEmpty()) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_LICENSE 不能为空!!!");
        }
        if (adminLicense.equals("00000000000000000000000000000000")) {
            log.warn("ADMIN_LICENSE 使用了不安全的默认值!!! 强烈建议修改!!!");
        }
        if (!adminLicense.matches(adminLicenseRegex)) {
            throw new AppException(ServerError.CONFIG_ERROR, "ADMIN_LICENSE 必须匹配正则 " + adminLicenseRegex + " !!!");
        }

        log.info("参数配置检查完成!!!");
    }

}
