package cloud.daodao.license.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cipher {

    String property() default "";

    Security security() default Security.AES;

    enum Security {
        AES,
    }

}
