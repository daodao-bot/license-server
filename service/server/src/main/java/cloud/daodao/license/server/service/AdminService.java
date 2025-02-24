package cloud.daodao.license.server.service;

import cloud.daodao.license.common.server.model.admin.ConfigData;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.helper.TokenHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {

    @Resource
    private AppConfig appConfig;

    @Resource
    private TokenHelper tokenHelper;

    public ConfigData adminConfig(String authorization) {
        String username = tokenHelper.username(authorization);
        assert null != username && !username.isEmpty();
        ConfigData data = new ConfigData();
        data.setAppId(appConfig.getAdminAppId());
        data.setLicense(appConfig.getAdminLicense());
        data.setApiSecurity(appConfig.getApiSecurity());
        return data;
    }

}
