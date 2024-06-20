package no.fintlabs.elev;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ElevController {

    private final ElevService elevService;
    private final ElevRepository elevRepository;

    @GetMapping
    public ResponseEntity<List<ElevEntity>> getAll() {
        return ResponseEntity.ok(elevService.getAllDecryptedElev());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElevEntity> getById(@PathVariable String id) {
        return ResponseEntity.ok(elevService.getDecryptedElev(id));
    }

    @PostMapping
    public ResponseEntity<ElevEntity> create(@RequestBody ElevEntity elev) {
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
    public ResponseEntity<ElevEntity> update(@PathVariable String id, @RequestBody ElevEntity elev) {
        return ResponseEntity.ok(elevService.updateElev(id, elev));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (!elevRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        elevRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        elevService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
