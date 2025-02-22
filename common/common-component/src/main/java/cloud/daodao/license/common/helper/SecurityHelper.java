package cloud.daodao.license.common.helper;

import cloud.daodao.license.common.annotation.Cipher;
import cloud.daodao.license.common.annotation.Hash;
import cloud.daodao.license.common.annotation.Mask;
import cloud.daodao.license.common.config.CommonConfig;
import cloud.daodao.license.common.model.Serializer;
import cloud.daodao.license.common.util.security.AesUtil;
import cloud.daodao.license.common.util.security.HashUtil;
import cloud.daodao.license.common.util.security.MaskUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
public class SecurityHelper {

    @Resource
    private CommonConfig commonConfig;

    public String aesEncrypt(String plains) {
        String aesKey = commonConfig.getAesKey();
        String aesIv = commonConfig.getAesIv();
        String cipher = null;
        try {
            cipher = AesUtil.encrypt(aesKey, aesIv, plains);
        } catch (Exception e) {
            log.error("AES encrypt error", e);
            // throw new RuntimeException(e);
        }
        return cipher;
    }

    public String aesDecrypt(String cipher) {
        String aesKey = commonConfig.getAesKey();
        String aesIv = commonConfig.getAesIv();
        String plains = null;
        try {
            plains = AesUtil.decrypt(aesKey, aesIv, cipher);
        } catch (Exception e) {
            log.error("AES decrypt error", e);
            // throw new RuntimeException(e);
        }
        return plains;
    }

    public <T extends Serializer> T encode(T t) {
        Class<? extends Serializer> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Cipher cipher = field.getDeclaredAnnotation(Cipher.class);
            Hash hash = field.getDeclaredAnnotation(Hash.class);
            Mask mask = field.getDeclaredAnnotation(Mask.class);
            if (cipher == null && hash == null && mask == null) {
                continue;
            } else {
                field.setAccessible(true);
            }
            String plainFiledName;
            if (cipher != null) {
                String cipherFieldName = field.getName();
                String property = cipher.property();
                Cipher.Security security = cipher.security();
                if (property.isEmpty()) {
                    /*
                     * 约定 cipherFieldName = plainFieldName + "Cipher"
                     */
                    plainFiledName = cipherFieldName.substring(0, cipherFieldName.length() - 6);
                } else {
                    plainFiledName = property;
                }
                /*
                 * 判断是否有对应的属性
                 */
                Field plainField = null;
                try {
                    plainField = clazz.getDeclaredField(plainFiledName);
                } catch (NoSuchFieldException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainField == null) {
                    continue;
                }
                plainField.setAccessible(true);
                String plainFieldValue = null;
                try {
                    Object o = plainField.get(t);
                    if (o != null) {
                        plainFieldValue = (String) o;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainFieldValue == null) {
                    continue;
                }
                if (security == null) {
                    security = Cipher.Security.AES;
                }
                if (security == Cipher.Security.AES) {
                    String cipherFieldValue = aesEncrypt(plainFieldValue);
                    try {
                        field.set(t, cipherFieldValue);
                    } catch (IllegalAccessException e) {
                        log.error("Encode error", e);
                        // throw new RuntimeException(e);
                    }
                }
            } else if (hash != null) {
                String hashFieldName = field.getName();
                String property = hash.property();
                Hash.Security security = hash.security();
                if (property.isEmpty()) {
                    /*
                     * 约定 hashFieldName = plainFieldName + "Hash"
                     */
                    plainFiledName = hashFieldName.substring(0, hashFieldName.length() - 4);
                } else {
                    plainFiledName = property;
                }
                /*
                 * 判断是否有对应的属性
                 */
                Field plainField = null;
                try {
                    plainField = clazz.getDeclaredField(plainFiledName);
                } catch (NoSuchFieldException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainField == null) {
                    continue;
                }
                plainField.setAccessible(true);
                String plainFieldValue = null;
                try {
                    Object o = plainField.get(t);
                    if (o != null) {
                        plainFieldValue = (String) o;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainFieldValue == null) {
                    continue;
                }
                if (security == null) {
                    security = Hash.Security.SHA_256;
                }
                if (security == Hash.Security.SHA_256) {
                    String hashFieldValue = HashUtil.sha256(plainFieldValue);
                    try {
                        field.set(t, hashFieldValue);
                    } catch (IllegalAccessException e) {
                        log.error("Encode error", e);
                        // throw new RuntimeException(e);
                    }
                }
            } else if (mask != null) {
                String maskFieldName = field.getName();
                String property = mask.property();
                Mask.Security security = mask.security();
                if (property.isEmpty()) {
                    /*
                     * 约定 maskFieldName = plainFieldName + "Mask"
                     */
                    plainFiledName = maskFieldName.substring(0, maskFieldName.length() - 4);
                } else {
                    plainFiledName = property;
                }
                /*
                 * 判断是否有对应的属性
                 */
                Field plainField = null;
                try {
                    plainField = clazz.getDeclaredField(plainFiledName);
                } catch (NoSuchFieldException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainField == null) {
                    continue;
                }
                plainField.setAccessible(true);
                String plainFieldValue = null;
                try {
                    Object o = plainField.get(t);
                    if (o != null) {
                        plainFieldValue = (String) o;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
                if (plainFieldValue == null) {
                    continue;
                }
                if (security == null) {
                    security = Mask.Security.NONE;
                }
                String maskFieldValue = MaskUtil.mask(security, plainFieldValue);
                try {
                    field.set(t, maskFieldValue);
                } catch (IllegalAccessException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
            }

        }
        return t;
    }

    public <T extends Serializer> T decode(T t) {
        Class<? extends Serializer> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Cipher cipher = field.getDeclaredAnnotation(Cipher.class);
            if (cipher == null) {
                continue;
            } else {
                field.setAccessible(true);
            }
            String plainFiledName;
            String cipherFieldName = field.getName();
            String property = cipher.property();
            Cipher.Security security = cipher.security();
            if (property.isEmpty()) {
                /*
                 * 约定 cipherFieldName = plainFieldName + "Cipher"
                 */
                plainFiledName = cipherFieldName.substring(0, cipherFieldName.length() - 6);
            } else {
                plainFiledName = property;
            }
            /*
             * 判断是否有对应的属性
             */
            Field plainField = null;
            try {
                plainField = clazz.getDeclaredField(plainFiledName);
            } catch (NoSuchFieldException e) {
                log.error("Encode error", e);
                // throw new RuntimeException(e);
            }
            if (plainField == null) {
                continue;
            }
            plainField.setAccessible(true);
            String cipherFieldValue = null;
            try {
                Object o = field.get(t);
                if (o != null) {
                    cipherFieldValue = (String) o;
                }
            } catch (IllegalAccessException e) {
                log.error("Encode error", e);
                // throw new RuntimeException(e);
            }
            if (cipherFieldValue == null || cipherFieldValue.isEmpty()) {
                continue;
            }
            if (security == null) {
                security = Cipher.Security.AES;
            }
            if (security == Cipher.Security.AES) {
                String plainFieldValue = aesDecrypt(cipherFieldValue);
                try {
                    plainField.set(t, plainFieldValue);
                } catch (IllegalAccessException e) {
                    log.error("Encode error", e);
                    // throw new RuntimeException(e);
                }
            }
        }
        return t;
    }

}
