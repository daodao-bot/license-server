package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.No;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.AdminApi;
import cloud.daodao.license.common.server.model.admin.ConfigData;
import cloud.daodao.license.server.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class AdminController implements AdminApi {

    @Resource
    private AdminService adminService;

    @Override
    public Response<ConfigData> adminConfig(String authorization, Request<No> request) {
        ConfigData data = adminService.adminConfig(authorization);
        return new Response<>(data);
    }

}
