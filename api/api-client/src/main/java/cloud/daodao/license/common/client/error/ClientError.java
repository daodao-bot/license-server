package cloud.daodao.license.common.client.error;

import cloud.daodao.license.common.enums.ServiceCode;
import cloud.daodao.license.common.error.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DaoDao
 */
@Getter
@AllArgsConstructor
public enum ClientError implements ErrorEnum {

    ERROR(code("000"), "ERROR : client 服务异常"),
    INVOKE_SERVER_ERROR(code("001"), "调用 server 服务异常"),
    AES_ENCRYPT_ERROR(code("002"), "AES 加密异常"),
    AES_DECRYPT_ERROR(code("003"), "AES 解密异常"),

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
        return ServiceCode.CLIENT.code + code;
    }

}
