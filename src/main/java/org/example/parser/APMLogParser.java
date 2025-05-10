package org.example.parser;

import org.example.model.APMLogEntry;
import org.example.model.LogEntry;
import org.example.util.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Parser for APM log entries (Strategy pattern implementation)
 */
public class APMLogParser implements LogParser {

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
            String metric = parsedData.get("metric");
            double value = Double.parseDouble(parsedData.get("value"));

            // Create and return the APM log entry
            return Optional.of(new APMLogEntry(timestamp, host, metric, value, parsedData));
        } catch (Exception e) {
            System.err.println("Error parsing APM log: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean canParse(String logLine) {
        return logLine != null &&
                logLine.contains("metric=") &&
                logLine.contains("value=") &&
                !logLine.contains("level=");
    }
}