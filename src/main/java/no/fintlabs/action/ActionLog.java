package no.fintlabs.action;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActionLog {

    private String id;
    private String username;
    private Action action;
    private final LocalDateTime timestamp = LocalDateTime.now();

}
