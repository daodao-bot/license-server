package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.No;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.UserApi;
import cloud.daodao.license.common.server.model.user.LoginParam;
import cloud.daodao.license.common.server.model.user.TokenData;
import cloud.daodao.license.common.server.model.user.UserData;
import cloud.daodao.license.server.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class UserController implements UserApi {

    @Resource
    private UserService userService;

    @Override
    public Response<UserData> userInfo(String authorization, Request<No> request) {
        UserData data = userService.userInfo(authorization);
        return new Response<>(data);
    }

    @Override
    public Response<TokenData> userLogin(Request<LoginParam> request) {
        TokenData data = userService.userLogin(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<Ok> userLogout(String authorization, Request<No> request) {
        userService.userLogout(authorization);
        return new Response<>();
    }

}
