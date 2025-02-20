package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.LicenseData;
import cloud.daodao.license.common.server.model.LicenseParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "LicenseApi", description = "License 接口")
@HttpExchange(url = AppConstant.API)
public interface LicenseApi {

    @Operation(summary = "License 自省", description = "License 自省 license @DaoDao")
    @PostExchange(url = ServerConstant.LICENSE_INTROSPECT)
    Response<LicenseData> licenseIntrospect(@RequestBody @Valid Request<LicenseParam> request);

}
