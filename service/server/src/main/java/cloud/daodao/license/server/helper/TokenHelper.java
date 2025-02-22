package cloud.daodao.license.server.helper;

import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.constant.CacheConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class TokenHelper {

    @Resource
    private AppConfig appConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String username(String authorization) {
        String token = extractToken(authorization);
        String key = CacheConstant.TOKEN + token;
        String username = stringRedisTemplate.opsForValue().get(key);
        if (null == username || username.isEmpty()) {
            throw new AppException(AppError.TOKEN_ERROR, token);
        }
        return username;
    }

    public String createToken(String username) {
        String token = generateToken();
        String key = CacheConstant.TOKEN + token;
        Duration duration = appConfig.getAdminTokenDuration();
        stringRedisTemplate.opsForValue().set(key, username, duration);
        return token;
    }

    public void removeToken(String authorization) {
        String token = extractToken(authorization);
        String key = CacheConstant.TOKEN + token;
        String username = stringRedisTemplate.opsForValue().get(key);
        if (null == username || username.isEmpty()) {
            // throw new AppException(AppError.TOKEN_ERROR, token);
        }
        stringRedisTemplate.delete(key);
    }

    public void renewToken(String authorization) {
        String token = extractToken(authorization);
        String key = CacheConstant.TOKEN + token;
        String username = stringRedisTemplate.opsForValue().get(key);
        if (null == username || username.isEmpty()) {
            throw new AppException(AppError.TOKEN_ERROR, token);
        }
        Duration duration = appConfig.getAdminTokenDuration();
        stringRedisTemplate.opsForValue().set(key, username, duration);
    }

    private static String extractToken(String authorization) {
        return authorization.replace("Bearer ", "");
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
