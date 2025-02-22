package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.No;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.user.LoginParam;
import cloud.daodao.license.common.server.model.user.TokenData;
import cloud.daodao.license.common.server.model.user.UserData;
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
@Tag(name = "UserApi", description = "用户接口")
@HttpExchange(url = AppConstant.API)
public interface UserApi {

    @Operation(summary = "用户信息", description = "@DaoDao 用户信息")
    @PostExchange(url = ServerConstant.USER_INFO)
    Response<UserData> userInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid Request<No> request);

    @Operation(summary = "用户登录", description = "@DaoDao 用户登录")
    @PostExchange(url = ServerConstant.USER_LOGIN)
    Response<TokenData> userLogin(@RequestBody @Valid Request<LoginParam> request);

    @Operation(summary = "用户退出", description = "@DaoDao 用户退出")
    @PostExchange(url = ServerConstant.USER_LOGOUT)
    Response<Ok> userLogout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid Request<No> request);

}
