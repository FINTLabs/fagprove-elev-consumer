package no.fintlabs;

import no.fintlabs.exception.EncryptionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class EncryptionServiceTest {

    private EncryptionService encryptionService;

    private String originalString;

    @BeforeEach
    void setUp() {
        originalString = "I want to be encrypted!";
        encryptionService = new EncryptionService("otcvNHi1W8EWTR+2rNQw9btu1wm2Jqdh1gADZ4GSxA4=");
    }

    @Test
    public void encryptAndDecrypt() throws EncryptionException {
        String encryptedString = encryptionService.encrypt(originalString);
        String decryptedString = encryptionService.decrypt(encryptedString);
        assertEquals(originalString, decryptedString, "encrpytion and decryption works");
    }

    @Test
    public void encryptDoesntMatchOriginalString() throws EncryptionException {
        String encryptedString = encryptionService.encrypt(originalString);
        assertNotEquals(originalString, encryptedString, "encryption alters the original string");
    }
}
