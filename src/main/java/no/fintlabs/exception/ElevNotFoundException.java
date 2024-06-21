package no.fintlabs.exception;

public class ElevNotFoundException extends RuntimeException {

    public ElevNotFoundException() {
        super("Elev not found");

    }
}
