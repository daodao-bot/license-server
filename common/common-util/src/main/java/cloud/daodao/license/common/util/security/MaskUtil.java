package cloud.daodao.license.common.util.security;

import cloud.daodao.license.common.annotation.Mask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DaoDao
 */
public class MaskUtil {

    public static String mask(Mask.Security security, String origin) {
        if (null == origin) {
            return null;
        }
        if (origin.isEmpty()) {
            return "*";
        }
        String mask;
        if (security.equals(Mask.Security.NAME)) {
            mask = nameMask(origin);
            return mask;
        }
        Matcher matcher = Pattern.compile(security.getRegex()).matcher(origin);
        mask = matcher.replaceAll(security.getReplace());
        return mask;
    }

    private static String nameMask(String origin) {
        if (null == origin || origin.isEmpty()) {
            return origin;
        }
        int length = origin.length();
        String mask;
        if (1 == length) {
            mask = origin;
        } else if (2 == length) {
            mask = "*" + origin.charAt(length - 1);
        } else {
            mask = origin.charAt(0) + "*".repeat(Math.min(length - 2, 4)) + origin.charAt(length - 1);
        }
        return mask;
    }

}
