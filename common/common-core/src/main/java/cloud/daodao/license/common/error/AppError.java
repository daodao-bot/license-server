package cloud.daodao.license.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DaoDao
 */
@Getter
@AllArgsConstructor
public enum AppError implements ErrorEnum {

    /**
     * OK
     * 现在约定 OK 000000 为请求成功，前 3 个 0 区分服务，后 3 个 0 区分响应码
     */
    OK("000000", "OK"),

    /**
     * ERROR
     */
    ERROR("999999", "ERROR"),

    /**
     * TOKEN_ERROR
     */
    TOKEN_ERROR("111111", "token 错误"),

    REQUEST_SECURITY_ERROR("222222", "请求 X-Security 错误"),

    REQUEST_PARAM_ERROR("666666", "请求参数错误"),

    REQUEST_TRACE_DUPLICATE("777777", "请求 X-Trace 重复"),

    REQUEST_TIME_ERROR("888888", "请求 X-Time 错误"),


    ;

    /**
     * 错误编码
     */
    public final String code;

    /**
     * 错误信息
     */
    public final String message;

}
