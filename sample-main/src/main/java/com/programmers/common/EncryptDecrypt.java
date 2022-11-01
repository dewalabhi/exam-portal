package com.programmers.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class EncryptDecrypt {
    private static final Logger logger = LoggerFactory.getLogger(EncryptDecrypt.class);
    @Value("${encryptionKey}")
    private static String ENCRYPTIONDECRYPIONKEY;
    private static SecretKeySpec secretKey;

    private EncryptDecrypt() {
        throw new IllegalStateException("EncryptDecrypt class");
    }

    public static void setKey(String myKey) {
        final MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt) {
        try {
            setKey(ENCRYPTIONDECRYPIONKEY);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            logger.error("Error while encrypting: ", ex);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            setKey(ENCRYPTIONDECRYPIONKEY);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception ex) {
            logger.error("Error while decrypting: ", ex);
        }
        return null;
    }
}
