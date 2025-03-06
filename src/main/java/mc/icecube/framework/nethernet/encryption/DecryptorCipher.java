package mc.icecube.framework.nethernet.encryption;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DecryptorCipher {
    public static byte[] getKey() throws Exception {
        // Key of 0xdeadbeef hash
        // If you don't mind how was it created before you cant just use constant of the key
        // HEX: eed4c37861936c6ebb91ab9d195c1b489de46362d3bb8e30d8aab08bf217e6f3
        return sha256(ByteBuffer.allocate(Long.BYTES).order(ByteOrder.LITTLE_ENDIAN).putLong(0xdeadbeefL).array());
    }

    public static byte[] sha256(byte[] input) throws Exception {
        return MessageDigest.getInstance("SHA-256").digest(input);
    }

    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key key = new SecretKeySpec(getKey(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key key = new SecretKeySpec(getKey(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    public static byte[] checksum(byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(getKey(), "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
