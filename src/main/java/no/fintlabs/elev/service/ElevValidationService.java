package no.fintlabs.elev.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import no.fintlabs.EncryptionService;
import no.fintlabs.elev.repository.ElevEntity;
import no.fintlabs.elev.repository.ElevRepository;
import no.fintlabs.exception.ElevValidationException;
import no.fintlabs.exception.EncryptionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElevValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{8}$");

    private final ElevRepository elevRepository;
    private final EncryptionService encryptionService;

    public void validateElev(ElevEntity elev) {
        validateName(elev.getName());
        validateBirthNumber(elev.getBirthNumber());
        validateEmail(elev.getEmail());
        validateMobileNumber(elev.getMobileNumber());
    }

    private void validateName(String name) {
        if (name == null || name.length() <= 3) {
            throw new ElevValidationException("Name must not be empty and must be longer than 3 letters.");
        }
    }

    @SneakyThrows
    private void validateBirthNumber(String birthNumber) {
        if (birthNumber == null || !birthNumber.matches("\\d{11}")) {
            throw new ElevValidationException("Birth number must be a valid 11-digit number.");
        }
        if (checkIfAlreadyExists(birthNumber, elevRepository::findAllBirthNumbers)) {
            throw new ElevValidationException("Birth number already exists");
        }
    }

    @SneakyThrows
    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ElevValidationException("Email must be a valid email address.");
        }
        if (checkIfAlreadyExists(email, elevRepository::findAllEmails)) {
            throw new ElevValidationException("Email already exists");
        }
    }

    @SneakyThrows
    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !MOBILE_PATTERN.matcher(mobileNumber).matches()) {
            throw new ElevValidationException("Mobile number must be a valid 8-digit number.");
        }
        if (checkIfAlreadyExists(mobileNumber, elevRepository::findAllMobileNumbers)) {
            throw new ElevValidationException("Mobile number already exists");
        }
    }

    private boolean checkIfAlreadyExists(String value, Supplier<List<String>> valueSupplier) {
        Set<String> decryptedValues = valueSupplier.get().stream()
                .map(this::decryptSafely)
                .collect(Collectors.toSet());
        return decryptedValues.contains(value);
    }

    private String decryptSafely(String encryptedData) {
        try {
            return encryptionService.decrypt(encryptedData);
        } catch (EncryptionException e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}
