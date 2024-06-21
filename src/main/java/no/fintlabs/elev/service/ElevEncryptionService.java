package no.fintlabs.elev.service;

import lombok.RequiredArgsConstructor;
import no.fintlabs.EncryptionService;
import no.fintlabs.elev.repository.ElevEntity;
import no.fintlabs.exception.EncryptionException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElevEncryptionService {

    private final EncryptionService encryptionService;

    private ElevEntity processEncryption(ElevEntity elev, EncryptionFunction<String, String> function) {
        try {
            elev.setName(function.apply(elev.getName()));
            elev.setEmail(function.apply(elev.getEmail()));
            elev.setBirthNumber(function.apply(elev.getBirthNumber()));
            elev.setMobileNumber(function.apply(elev.getMobileNumber()));
            elev.setAverageGrades(function.apply(elev.getAverageGrades()));
            return elev;
        } catch (EncryptionException e) {
            throw new RuntimeException(e);
        }
    }

    public ElevEntity encrypt(ElevEntity elev) {
        return processEncryption(elev, encryptionService::encrypt);
    }

    public ElevEntity decrypt(ElevEntity elev) {
        return processEncryption(elev, encryptionService::decrypt);
    }

    @FunctionalInterface
    public interface EncryptionFunction<T, R> {
        R apply(T t) throws EncryptionException;
    }

}
