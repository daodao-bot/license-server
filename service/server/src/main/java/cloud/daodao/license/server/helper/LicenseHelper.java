package cloud.daodao.license.server.helper;

import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.helper.SecurityHelper;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.server.model.License;
import cloud.daodao.license.server.repository.LicenseRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class LicenseHelper {

    @Resource
    private SecurityHelper securityHelper;

    @Resource
    private LicenseRepository licenseRepository;

    public String license(String appId) {
        Optional<License> optional = licenseRepository.findByAppId(appId);
        if (optional.isEmpty()) {
            throw new AppException(ServerError.LICENSE_APP_ID_NOT_EXIST, appId);
        }
        License license = optional.get();
        String licenseCipher = license.getLicenseCipher();
        return securityHelper.aesDecrypt(licenseCipher);
    }

}
