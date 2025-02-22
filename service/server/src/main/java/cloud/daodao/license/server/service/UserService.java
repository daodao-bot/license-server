package cloud.daodao.license.server.service;

import cloud.daodao.license.common.annotation.Mask;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.common.server.model.user.LoginParam;
import cloud.daodao.license.common.server.model.user.TokenData;
import cloud.daodao.license.common.server.model.user.UserData;
import cloud.daodao.license.common.util.security.MaskUtil;
import cloud.daodao.license.server.helper.TokenHelper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenHelper tokenHelper;

    public UserData userInfo(String authorization) {
        String username = tokenHelper.username(authorization);
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ServerError.USERNAME_NOT_EXIST, username);
        }
        username = userDetails.getUsername();
        UserData userData = new UserData();
        userData.setUsername(username);
        return userData;
    }

    public TokenData userLogin(@Valid @NotNull LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ServerError.USERNAME_NOT_EXIST, username);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new AppException(ServerError.PASSWORD_ERROR, MaskUtil.mask(Mask.Security.PASSWORD, password));
        }
        String token = tokenHelper.createToken(username);
        TokenData tokenData = new TokenData();
        tokenData.setToken(token);
        return tokenData;
    }

    public void userLogout(String authorization) {
        tokenHelper.removeToken(authorization);
    }

}
