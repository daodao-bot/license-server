package cloud.daodao.license.common.server.error;

import cloud.daodao.license.common.enums.ServiceCode;
import cloud.daodao.license.common.error.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DaoDao
 */
@Getter
@AllArgsConstructor
public enum ServerError implements ErrorEnum {

    ERROR(code("000"), "ERROR : server 服务异常"),

    ;

    /**
     * 响应编码
     */
    public final String code;

    /**
     * 响应说明
     */
    public final String message;

    private static String code(String code) {
        return ServiceCode.SERVER.code + code;
    }

}
