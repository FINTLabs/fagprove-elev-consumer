package no.fintlabs.elev.service;

import lombok.RequiredArgsConstructor;
import no.fintlabs.EncryptionService;
import no.fintlabs.elev.repository.ElevEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ElevEncryptionService {

    private final EncryptionService encryptionService;

    private ElevEntity processEncryption(ElevEntity elev, Function<String, String> function) {
        elev.setName(function.apply(elev.getName()));
        elev.setEmail(function.apply(elev.getEmail()));
        elev.setBirthNumber(function.apply(elev.getBirthNumber()));
        elev.setMobileNumber(function.apply(elev.getMobileNumber()));
        elev.setAverageGrades(function.apply(elev.getAverageGrades()));
        return elev;
    }

    public ElevEntity encrypt(ElevEntity elev) {
        return processEncryption(elev, encryptionService::encrypt);
    }

    public ElevEntity decrypt(ElevEntity elev) {
        return processEncryption(elev, encryptionService::decrypt);
    }

}
