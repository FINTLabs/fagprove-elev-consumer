package no.fintlabs.elev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.ClaimHelper;
import no.fintlabs.action.Action;
import no.fintlabs.action.ActionLog;
import no.fintlabs.action.ActionLogKafkaProducer;
import no.fintlabs.elev.repository.ElevEntity;
import no.fintlabs.elev.repository.ElevRepository;
import no.fintlabs.elev.service.ElevService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ElevController {

    private final ElevService elevService;
    private final ElevRepository elevRepository;
    private final ActionLogKafkaProducer actionLogKafkaProducer;

    @GetMapping
    public ResponseEntity<List<ElevEntity>> getAll(@AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.GET_ALL_STUDENTS));
        return ResponseEntity.ok(elevService.getAllDecryptedElev());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElevEntity> getById(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.GET_STUDENT_BY_ID));
        return ResponseEntity.ok(elevService.getDecryptedElev(id));
    }

    @PostMapping
    public ResponseEntity<ElevEntity> create(@RequestBody ElevEntity elev, @AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.CREATE_STUDENT));
        ElevEntity encryptedElev = elevService.encryptAndSaveWithNewId(elev);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(encryptedElev.getId())
                        .toUri()
        ).body(encryptedElev);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElevEntity> update(@PathVariable String id, @RequestBody ElevEntity elev, @AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.UPDATE_STUDENT));
        return ResponseEntity.ok(elevService.updateElev(id, elev));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.DELETE_STUDENT_BY_ID));
        if (!elevRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        elevRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(@AuthenticationPrincipal Jwt jwt) {
        actionLogKafkaProducer.publishAction(ActionLog.of(ClaimHelper.getEmail(jwt), Action.DELETE_ALL_STUDENTS));
        elevService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
