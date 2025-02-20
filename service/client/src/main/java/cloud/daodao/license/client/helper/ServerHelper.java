package cloud.daodao.license.client.helper;

import cloud.daodao.license.common.client.error.ClientError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.LicenseApi;
import cloud.daodao.license.common.server.model.LicenseData;
import cloud.daodao.license.common.server.model.LicenseParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerHelper {

    @Resource
    private LicenseApi licenseApi;

    public LicenseData licenseIntrospect(LicenseParam param) {
        Request<LicenseParam> request = new Request<>(param);
        Response<LicenseData> response;
        try {
            response = licenseApi.licenseIntrospect(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ClientError.INVOKE_SERVER_ERROR, e);
        }
        if (!response.isOk()) {
            throw new AppException(response.getCode(), response.getMessage());
        }
        return response.getData();
    }

}
