package cloud.daodao.license.client.service;

import cloud.daodao.license.client.helper.ServerHelper;
import cloud.daodao.license.common.client.model.TestParam;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    @Resource
    private ServerHelper serverHelper;

    public void testLicense(@Valid @NotNull TestParam param) {
        String license = param.getLicense();
        LicenseParam licenseParam = new LicenseParam();
        licenseParam.setLicense(license);
        LicenseData licenseData = serverHelper.licenseIntrospect(licenseParam);
        assert licenseData != null;
    }

}
