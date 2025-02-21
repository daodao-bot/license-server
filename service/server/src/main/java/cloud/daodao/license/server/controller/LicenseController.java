package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.*;
import cloud.daodao.license.common.server.api.LicenseApi;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import cloud.daodao.license.common.server.model.license.LicenseSearch;
import cloud.daodao.license.common.server.model.license.LicenseUpsert;
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
    public Response<PageData<LicenseData>> licenseSearch(Request<PageParam<LicenseSearch>> request) {
        PageData<LicenseData> data = licenseService.licenseSearch(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<LicenseData> licenseSelect(Request<IdParam> request) {
        LicenseData data = licenseService.licenseSelect(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<LicenseData> licenseUpsert(Request<LicenseUpsert> request) {
        LicenseData data = licenseService.licenseUpsert(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<Ok> licenseSending(Request<IdParam> request) {
        licenseService.licenseSending(request.getParam());
        return new Response<>();
    }

    @Override
    public Response<LicenseData> licenseIntrospect(String appId, Request<LicenseParam> request) {
        LicenseData data = licenseService.licenseIntrospect(appId, request.getParam());
        return new Response<>(data);
    }

}
