package no.fintlabs.exception;

public class EncryptionException extends Exception {

    public EncryptionException(Throwable cause) {
        super("Error decrypting data", cause);
    }

}
