package cloud.daodao.license.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DaoDao
 */
@Getter
@AllArgsConstructor
public enum ServiceCode {

    CLIENT("101", "客户端"),
    SERVER("102", "服务端"),

    ;

    /**
     * 服务编码
     */
    public final String code;

    /**
     * 服务名称
     */
    public final String name;

}
