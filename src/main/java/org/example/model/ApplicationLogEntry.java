package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents an Application log entry with severity level and message
 */
public class ApplicationLogEntry extends LogEntry {
    private String level;
    private String message;

    public ApplicationLogEntry(LocalDateTime timestamp, String host, String level, String message, Map<String, String> rawData) {
        super(timestamp, host, rawData);
        this.level = level;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApplicationLogEntry{" +
                "level='" + level + '\'' +
                ", message='" + message + '\'' +
                "} " + super.toString();
    }
}