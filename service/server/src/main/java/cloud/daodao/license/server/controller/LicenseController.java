package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.LicenseApi;
import cloud.daodao.license.common.server.model.LicenseData;
import cloud.daodao.license.common.server.model.LicenseParam;
import cloud.daodao.license.server.service.LicenseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class LicenseController implements LicenseApi {

    @Resource
    private LicenseService licenseService;

    @Override
    public Response<LicenseData> licenseIntrospect(Request<LicenseParam> request) {
        LicenseData data = licenseService.licenseIntrospect(request.getParam());
        return new Response<>(data);
    }

}
