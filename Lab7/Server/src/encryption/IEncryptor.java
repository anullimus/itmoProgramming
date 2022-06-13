package encryption;

import java.security.NoSuchAlgorithmException;

public interface IEncryptor {
    String encrypt(String messageToEncrypt) throws NoSuchAlgorithmException;
}