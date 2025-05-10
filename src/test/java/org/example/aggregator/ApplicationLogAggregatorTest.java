package org.example.aggregator;

import org.example.model.ApplicationLogEntry;
import org.example.junit.jupiter.api.BeforeEach;
import org.example.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationLogAggregatorTest {
    private ApplicationLogAggregator aggregator;
    private LocalDateTime timestamp = LocalDateTime.parse("2024-02-24T16:22:15Z");

    @BeforeEach
    void setUp() {
        aggregator = new ApplicationLogAggregator();
    }

    @Test
    void testProcessLog_SingleLevel() {
        // Create Application log entry
        ApplicationLogEntry entry = new ApplicationLogEntry(timestamp, "host1", "INFO", "Test message", new HashMap<>());

        // Process the log
        aggregator.processLog(entry);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify results
        assertTrue(result.containsKey("INFO"));
        assertEquals(1, result.get("INFO"));
    }

    @Test
    void testProcessLog_MultipleLevels() {
        // Create Application log entries
        ApplicationLogEntry entry1 = new ApplicationLogEntry(timestamp, "host1", "INFO", "Info message 1", new HashMap<>());
        ApplicationLogEntry entry2 = new ApplicationLogEntry(timestamp, "host1", "INFO", "Info message 2", new HashMap<>());
        ApplicationLogEntry entry3 = new ApplicationLogEntry(timestamp, "host1", "ERROR", "Error message", new HashMap<>());
        ApplicationLogEntry entry4 = new ApplicationLogEntry(timestamp, "host1", "WARNING", "Warning message", new HashMap<>());
        ApplicationLogEntry entry5 = new ApplicationLogEntry(timestamp, "host1", "DEBUG", "Debug message", new HashMap<>());

        // Process all logs
        aggregator.processLog(entry1);
        aggregator.processLog(entry2);
        aggregator.processLog(entry3);
        aggregator.processLog(entry4);
        aggregator.processLog(entry5);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify results
        assertTrue(result.containsKey("INFO"));
        assertTrue(result.containsKey("ERROR"));
        assertTrue(result.containsKey("WARNING"));
        assertTrue(result.containsKey("DEBUG"));

        assertEquals(2, result.get("INFO"));
        assertEquals(1, result.get("ERROR"));
        assertEquals(1, result.get("WARNING"));
        assertEquals(1, result.get("DEBUG"));
    }

    @Test
    void testGetAggregatedData_EmptyData() {
        // Get aggregated data without processing any logs
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify result is empty
        assertTrue(result.isEmpty());
    }
}