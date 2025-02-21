package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.*;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import cloud.daodao.license.common.server.model.license.LicenseSearch;
import cloud.daodao.license.common.server.model.license.LicenseUpsert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "LicenseApi", description = "License 接口")
@HttpExchange(url = AppConstant.API)
public interface LicenseApi {

    @Operation(summary = "License 搜索", description = "@DaoDao License 搜索")
    @PostExchange(url = ServerConstant.LICENSE_SEARCH)
    Response<PageData<LicenseData>> licenseSearch(@RequestBody @Valid Request<PageParam<LicenseSearch>> request);

    @Operation(summary = "License 查询", description = "@DaoDao License 查询")
    @PostExchange(url = ServerConstant.LICENSE_SELECT)
    Response<LicenseData> licenseSelect(@RequestBody @Valid Request<IdParam> request);

    @Operation(summary = "License 写入", description = "@DaoDao License 写入")
    @PostExchange(url = ServerConstant.LICENSE_UPSERT)
    Response<LicenseData> licenseUpsert(@RequestBody @Valid Request<LicenseUpsert> request);

    @Operation(summary = "License 发送", description = "@DaoDao License 发送")
    @PostExchange(url = ServerConstant.LICENSE_SENDING)
    Response<Ok> licenseSending(@RequestBody @Valid Request<IdParam> request);

    @Operation(summary = "License 自省", description = "@DaoDao License 自省 license")
    @PostExchange(url = ServerConstant.LICENSE_INTROSPECT)
    Response<LicenseData> licenseIntrospect(@RequestHeader(AppConstant.X_APP_ID) String appId, @RequestBody @Valid Request<LicenseParam> request);

}
