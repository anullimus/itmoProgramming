package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA512Encryptor {

    private SHA512Encryptor() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static String encryptThisString(String hsString) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] data = md.digest(hsString.getBytes());

            StringBuilder hashText = new StringBuilder();
            for (byte datum : data) {
                hashText.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
            }

            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
