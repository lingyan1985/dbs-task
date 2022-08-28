package com.dbs.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class DecryptUtils {
    /**
     * Encryption Algorithm
     */
    private static final String ALGORITHM = "AES";
    /**
     * Key length
     */
    private static final Integer KEY_LENGTH = 128;
    /**
     * Default encoding
     */
    private static final String CHARSET = "utf-8";

    /**
     * Decrypt AES encrypted string
     *
     * @param content AES encrypted content
     * @param password Password when encrypting
     * @return Decrypted content
     */
    public static String decrypt(String content, String password) {
        try {
            //
            //Convert hexadecimal string to binary byte array
            byte[] byteArr = parseHexStr2Byte(content);

            SecretKeySpec key = generateKey(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // Decrypt
            byte[] result = cipher.doFinal(byteArr);

            return new String(result,CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**Convert hexadecimal to binary
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * Generate key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static SecretKeySpec generateKey(String password) throws Exception {
        // Create AES key generator
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(KEY_LENGTH, random);
        // Based on password, generate secret key
        SecretKey secretKey = kgen.generateKey();
        // Return the basic encoded key
        byte[] enCodeFormat = secretKey.getEncoded();
        // Convert to AES private key
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);

        return key;
    }
}
