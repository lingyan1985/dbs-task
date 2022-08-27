package com.dbs.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

public class DecryptUtils {
    /**
     * Offset variable, fixed in 8-bit bytes
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * Encryption Algorithm
     */
    private static final String ALGORITHM = "DES";
    /**
     * Encryption/Decryption Algorithm - Working Mode - Padding Mode
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * Default encoding
     */
    private static final String CHARSET = "utf-8";

    public static String decrypt(String password, String data) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("Failed to Decrypt");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            // base64 decode
            byte[] decode = Base64.getDecoder().decode(data.getBytes(CHARSET));

            // Decrypt
            byte[] decrypt = cipher.doFinal(decode);

            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * Generate Key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }
}
