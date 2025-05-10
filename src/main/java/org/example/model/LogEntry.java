package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Base abstract class for all log entry types
 */
public abstract class LogEntry {
    private LocalDateTime timestamp;
    private String host;
    private Map<String, String> rawData;

    public LogEntry(LocalDateTime timestamp, String host, Map<String, String> rawData) {
        this.timestamp = timestamp;
        this.host = host;
        this.rawData = rawData;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getHost() {
        return host;
    }

    public Map<String, String> getRawData() {
        return rawData;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", host='" + host + '\'' +
                ", rawData=" + rawData +
                '}';
    }
}