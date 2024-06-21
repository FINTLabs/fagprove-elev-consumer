package no.fintlabs.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ElevNotFoundException.class)
    public ResponseEntity<Void> elevNotFound(Exception e) {
        return ResponseEntity.notFound().build();
    }

}
