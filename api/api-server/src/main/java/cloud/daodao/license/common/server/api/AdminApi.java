package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.No;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.admin.ConfigData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "AdminApi", description = "管理平台接口")
@HttpExchange(url = AppConstant.API)
public interface AdminApi {

    @Operation(summary = "管理平台配置", description = "@DaoDao 管理平台配置")
    @PostExchange(url = ServerConstant.ADMIN_CONFIG)
    Response<ConfigData> adminConfig(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid Request<No> request);

}
