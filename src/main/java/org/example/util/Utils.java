package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for parsing log entries
 */
public class Utils {
    // Pattern for matching key-value pairs, handling quoted values
    private static final Pattern KEY_VALUE_PATTERN =
            Pattern.compile("(\\w+)=(?:\"([^\"]*)\"|([^\\s]+))");

    // Date time formatter for ISO format timestamps
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Parse a log line into key-value pairs
     * @param logLine The log line to parse
     * @return Map of key-value pairs
     */
    public static Map<String, String> parseKeyValuePairs(String logLine) {
        Map<String, String> result = new HashMap<>();
        Matcher matcher = KEY_VALUE_PATTERN.matcher(logLine);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            result.put(key, value);
        }

        return result;
    }

    /**
     * Parse a timestamp string into a LocalDateTime object
     * @param timestamp Timestamp string in ISO format
     * @return LocalDateTime object
     */
    public static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
    }
}