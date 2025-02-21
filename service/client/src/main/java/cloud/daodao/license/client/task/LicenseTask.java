package cloud.daodao.license.client.task;

import cloud.daodao.license.client.config.AppConfig;
import cloud.daodao.license.client.helper.LicenseHelper;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LicenseTask {

    @Resource
    private AppConfig appConfig;

    @Resource
    private LicenseHelper licenseHelper;

    @Resource
    private ApplicationContext applicationContext;

    @Scheduled(cron = "0 0 0 * * ?")
    public void periodValidator() {
        log.info("License 有效期校验任务开始...");
        String licenseCode = appConfig.getLicenseCode();
        LicenseParam licenseParam = new LicenseParam();
        licenseParam.setLicense(licenseCode);
        LicenseData licenseData;
        try {
            licenseData = licenseHelper.licenseIntrospect(licenseParam);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("License 有效期校验任务失败，应用退出...");
            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);
        }
        // assert licenseData != null;
    }

}
