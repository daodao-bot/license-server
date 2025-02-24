package cloud.daodao.license.server.constant;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.server.constant.ServerConstant;

public class FilterConstant {

    public static final String X_ORIGIN_URI = "X-Origin-URI";

    public static final String X_EXCEPTION = "X-Exception";

    public static final String X_AES_KEY = "X-AES-Key";

    public static final String X_AES_IV = "X-AES-IV";

    private static final String API_PREFIX = "/" + AppConstant.API + "/";
    public static final String[] NO_SECURITY_URI = {
            API_PREFIX + ServerConstant.USER_LOGIN,
            API_PREFIX + ServerConstant.ADMIN_CONFIG,
    };

}
