package org.example.aggregator;

import org.example.model.LogEntry;

import java.util.List;
import java.util.Map;

/**
 * Interface for log data aggregators
 */
public interface LogAggregator<T extends LogEntry> {
    /**
     * Process a log entry for aggregation
     * @param logEntry The log entry to process
     */
    void processLog(T logEntry);

    /**
     * Get the aggregated results as a map suitable for JSON conversion
     * @return Map containing the aggregated data
     */
    Map<String, Object> getAggregatedData();

    /**
     * Process a list of log entries
     * @param logEntries List of log entries to process
     */
    default void processLogs(List<T> logEntries) {
        for (T logEntry : logEntries) {
            processLog(logEntry);
        }
    }
}