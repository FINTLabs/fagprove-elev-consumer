package no.fintlabs.elev;

import lombok.RequiredArgsConstructor;
import no.fintlabs.exception.ElevNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElevService {

    private final ElevRepository elevRepository;
    private final ElevEncryptionService elevEncryptionService;
    private final ElevKafkaProducer elevKafkaProducer;

    public List<ElevEntity> getAllDecryptedElev() {
        // TODO: Implement decryption
        return elevRepository.findAll();
    }

    public ElevEntity getDecryptedElev(String id) {
        return elevRepository.findById(id).map(elevEntity -> {
            // TODO: Implement decryption
            return elevEntity;
        }).orElseThrow(ElevNotFoundException::new);
    }

    public ElevEntity encryptAndSaveWithNewId(ElevEntity elev) {
        elev.setId(UUID.randomUUID().toString());
        return encryptAndSave(elev);
    }

    public ElevEntity encryptAndSave(ElevEntity elev) {
        ElevEntity encryptedElev = elevEncryptionService.encrypt(elev);
        elevKafkaProducer.publishElev(encryptedElev);
        return elevRepository.save(encryptedElev);
    }


    public ElevEntity updateElev(String id, ElevEntity newElev) {
        return elevRepository.findById(id)
                .map(existingElev -> {
                    newElev.setId(id);
                    return encryptAndSave(newElev);
                }).orElseThrow(ElevNotFoundException::new); // TODO: Implement custom exceptions and handler
    }

    public void deleteElev(String id) {
        elevRepository.deleteById(id);
    }

    public void deleteAll() {
        elevRepository.deleteAll();
        elevRepository.flush();
    }
}
