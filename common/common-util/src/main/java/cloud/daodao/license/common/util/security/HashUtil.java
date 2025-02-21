package cloud.daodao.license.common.util.security;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * @author DaoDao
 */
public class HashUtil {

    /**
     * SHA-256 加密
     * 使用 jdk 原生加密算法，转化成十六进制字符串
     *
     * @param string String
     * @return String
     */
    public static String sha256(String string) {
        if (null == string) {
            return null;
        }
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(string.getBytes());
            byte[] bytes = digest.digest();
            hash = HexFormat.of().withUpperCase().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static String sha256(File file) {
        if (null == file) {
            return null;
        }
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        String hash = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[1024];
            int sizeRead = -1;
            while ((sizeRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }
            bis.close();
            byte[] bytes = digest.digest();
            hash = HexFormat.of().withUpperCase().formatHex(bytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return hash;
    }

}
