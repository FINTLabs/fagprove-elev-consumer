package no.fintlabs.action;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
public class ActionLog {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss");

    private String id;
    private String username;
    private Action action;
    private final String timestamp;

    public ActionLog() {
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public static ActionLog of(String username, Action action) {
        ActionLog actionLog = new ActionLog();
        actionLog.setId(UUID.randomUUID().toString());
        actionLog.setUsername(username);
        actionLog.setAction(action);
        return actionLog;
    }

}
