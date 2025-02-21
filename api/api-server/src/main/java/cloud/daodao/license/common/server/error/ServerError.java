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
    PRODUCT_NOT_EXIST(code("001"), "产品不存在"),
    PRODUCT_CODE_ALREADY_EXIST(code("002"), "产品编码已存在"),
    PRODUCT_NAME_ALREADY_EXIST(code("003"), "产品名称已存在"),
    MAIL_REGULAR_ERROR(code("004"), "邮箱格式错误"),
    CUSTOMER_NOT_EXIST(code("005"), "客户不存在"),
    CUSTOMER_EMAIL_ALREADY_EXIST(code("006"), "客户邮箱已存在"),
    CUSTOMER_PHONE_ALREADY_EXIST(code("007"), "客户手机已存在"),

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
