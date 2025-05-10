package org.example.aggregator;

import org.example.model.ApplicationLogEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregator for Application log entries
 */
public class ApplicationLogAggregator implements LogAggregator<ApplicationLogEntry> {
    // Map to count occurrences of each log level
    private final Map<String, Integer> levelCounts = new HashMap<>();

    @Override
    public void processLog(ApplicationLogEntry logEntry) {
        String level = logEntry.getLevel();

        // Increment count for this level
        levelCounts.put(level, levelCounts.getOrDefault(level, 0) + 1);
    }

    @Override
    public Map<String, Object> getAggregatedData() {
        // For Application logs, the result is simply the count of logs by level
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry : levelCounts.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}