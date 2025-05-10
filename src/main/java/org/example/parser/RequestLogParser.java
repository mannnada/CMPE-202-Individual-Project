package org.example.parser;

import org.example.model.LogEntry;
import org.example.model.RequestLogEntry;
import org.example.util.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Parser for Request log entries (Strategy pattern implementation)
 */
public class RequestLogParser implements LogParser {

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
            String requestMethod = parsedData.get("request_method");
            String requestUrl = parsedData.get("request_url").replace("\"", ""); // Remove quotes from URL
            int responseStatus = Integer.parseInt(parsedData.get("response_status"));
            int responseTimeMs = Integer.parseInt(parsedData.get("response_time_ms"));

            // Create and return the Request log entry
            return Optional.of(new RequestLogEntry(timestamp, host, requestMethod, requestUrl,
                    responseStatus, responseTimeMs, parsedData));
        } catch (Exception e) {
            System.err.println("Error parsing Request log: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean canParse(String logLine) {
        return logLine != null &&
                logLine.contains("request_method=") &&
                logLine.contains("request_url=") &&
                logLine.contains("response_status=") &&
                logLine.contains("response_time_ms=");
    }
}