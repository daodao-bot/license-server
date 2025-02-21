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
    LICENSE_NOT_EXIST(code("008"), "License 不存在"),
    LICENSE_PERIOD_END_NULL(code("009"), "License 有效期结束日期为空"),
    LICENSE_PERIOD_END_INVALID(code("010"), "License 有效期结束日期无效"),
    LICENSE_APP_ID_ALREADY_EXIST(code("011"), "License AppId 已存在"),
    PRODUCT_CUSTOMER_ALREADY_EXIST(code("012"), "产品和客户的 License 已存在"),
    REQUEST_HEADER_REQUIRED_X_APP_ID(code("013"), "请求头缺少 X-App-ID"),
    APP_ID_AND_LICENSE_NOT_MATCH(code("014"), "AppId 和 License 不匹配"),
    LICENSE_INVALID(code("015"), "License 已失效"),
    LICENSE_EXPIRED(code("016"), "License 已过期"),
    LICENSE_APP_ID_NOT_EXIST(code("017"), "License AppId 不存在"),

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
