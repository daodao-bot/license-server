package cloud.daodao.license.common.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mask {

    String property() default "";

    Security security() default Security.NONE;

    /**
     * 敏感类型枚举
     *
     * @author DaoDao
     */
    @Getter
    @AllArgsConstructor
    enum Security {

        NONE("", ""),

        /**
         * 姓名，默认规则保留第一个字符，其余按照字符个数替换掩码 *
         * 大猫        : 大*
         * 大脸猫      : 大**
         * 大脸猫喵喵喵 : 大*****
         */
        // NAME("([\u4E00-\u9FA5]{1})([\u4E00-\u9FA5]{1})([\u4E00-\u9FA5]{0,})", "*$2$3"),
        NAME("(?<=.).", "*"),

        /**
         * 手机
         */
        PHONE("(1\\d{2})(\\d{4})(\\d{4})", "$1****$3"),

        /**
         * 邮箱
         */
        EMAIL("([a-zA-Z0-9_-]{2})([a-zA-Z0-9_-]+)(@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+)", "$1****$3"),

        /**
         * 证
         */
        LICENCE("^([\\s\\S]{6})([\\s\\S]*)([\\s\\S]{4})$", "$1****$3"),

        /**
         * 密码
         */
        PASSWORD("^[\\s\\S]*$", "******"),

        ;

        /**
         * 正则
         */
        final String regex;

        /**
         * 替换
         */
        final String replace;

    }

}
