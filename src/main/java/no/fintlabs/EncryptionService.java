package no.fintlabs;

import no.fintlabs.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;
    private final SecretKey secretKey;

    public EncryptionService(@Value("${fint.encryption.secret-key}") String secretKeyBase64) {
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyBase64), ALGORITHM);
    }

    public String encrypt(String data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = generateIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            byte[] combinedData = combineIVAndEncryptedData(iv, encryptedData);

            return Base64.getEncoder().encodeToString(combinedData);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String decrypt(String encryptedData) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] combinedData = Base64.getDecoder().decode(encryptedData);
            byte[] iv = extractIV(combinedData);
            byte[] encryptedBytes = extractEncryptedData(combinedData, iv.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decryptedData = cipher.doFinal(encryptedBytes);

            return new String(decryptedData);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private byte[] combineIVAndEncryptedData(byte[] iv, byte[] encryptedData) {
        byte[] combinedData = new byte[IV_SIZE + encryptedData.length];
        System.arraycopy(iv, 0, combinedData, 0, IV_SIZE);
        System.arraycopy(encryptedData, 0, combinedData, IV_SIZE, encryptedData.length);
        return combinedData;
    }

    private byte[] extractIV(byte[] combinedData) {
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(combinedData, 0, iv, 0, IV_SIZE);
        return iv;
    }

    private byte[] extractEncryptedData(byte[] combinedData, int offset) {
        byte[] encryptedBytes = new byte[combinedData.length - offset];
        System.arraycopy(combinedData, offset, encryptedBytes, 0, encryptedBytes.length);
        return encryptedBytes;
    }

}
