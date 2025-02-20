package cloud.daodao.license.server.service;

import cloud.daodao.license.common.server.model.LicenseData;
import cloud.daodao.license.common.server.model.LicenseParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LicenseService {

    public LicenseData licenseIntrospect(@Valid @NotNull LicenseParam param) {
        String license = param.getLicense();

        // todo license introspect

        LicenseData data = new LicenseData();
        data.setLicense(license);
        return data;
    }

}
