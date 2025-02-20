package cloud.daodao.license.common.constant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author DaoDao
 */
@Slf4j
@Data
public final class AppConstant {

    public static final String SLOGAN = "!!!";

    public static final String API = "api";

    public static final String TIME_ZONE = "GMT+8";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_EXAMPLE = "2025-01-01";
    public static final String TIME_EXAMPLE = "00:00:00";
    public static final String DATE_TIME_EXAMPLE = "2025-01-01T00:00:00";

    public static final String X_SECURITY = "X-Security";
    public static final String X_TIME = "X-Time";
    public static final String X_TRACE = "X-Trace";
    public static final String X_CLIENT = "X-Client";
    public static final String X_SIGN = "X-Sign";
    public static final String X_HASH = "X-Hash";
    public static final String X_SESSION = "X-Session";

    public static final String AES = "AES";
    public static final String RSA = "RSA";

    public static final String NAMESPACE = "license";

    public static final String BASE_PACKAGE = "cloud.daodao.license.**";

}
