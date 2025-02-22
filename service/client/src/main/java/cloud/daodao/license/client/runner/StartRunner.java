package cloud.daodao.license.client.runner;

import cloud.daodao.license.client.config.AppConfig;
import cloud.daodao.license.client.helper.LicenseHelper;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartRunner implements ApplicationRunner {

    @Resource
    private AppConfig appConfig;

    @Resource
    private LicenseHelper licenseHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String licenseCode = appConfig.getLicenseCode();
        LicenseParam licenseParam = new LicenseParam();
        licenseParam.setLicense(licenseCode);
        LicenseData licenseData = licenseHelper.licenseIntrospect(licenseParam);
        assert licenseData != null;
    }

}
