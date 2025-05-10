package org.example.parser;

import org.example.model.ApplicationLogEntry;
import org.example.model.LogEntry;
import org.example.util.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Parser for Application log entries (Strategy pattern implementation)
 */
public class ApplicationLogParser implements LogParser {

    @Override
    public Optional<LogEntry> parse(String logLine) {
        if (!canParse(logLine)) {
            return Optional.empty();
        }

        try {
            Map<String, String> parsedData = Utils.parseKeyValuePairs(logLine);

            // Extract required fields
            LocalDateTime timestamp = Utils.parseTimestamp(parsedData.get("timestamp"));
            String host = parsedData.get("host");
            String level = parsedData.get("level");
            String message = parsedData.get("message").replace("\"", ""); // Remove quotes from message

            // Create and return the Application log entry
            return Optional.of(new ApplicationLogEntry(timestamp, host, level, message, parsedData));
        } catch (Exception e) {
            System.err.println("Error parsing Application log: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean canParse(String logLine) {
        return logLine != null &&
                logLine.contains("level=") &&
                logLine.contains("message=");
    }
}