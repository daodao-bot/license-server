package cloud.daodao.license.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Hash {

    String property() default "";

    Security security() default Security.SHA_256;

    enum Security {
        SHA_256,
    }

}
