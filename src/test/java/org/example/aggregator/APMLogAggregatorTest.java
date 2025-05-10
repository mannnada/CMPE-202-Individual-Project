package org.example.aggregator;

import org.example.model.APMLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class APMLogAggregatorTest {
    private APMLogAggregator aggregator;
    private LocalDateTime timestamp = LocalDateTime.parse("2024-02-24T16:22:15Z");

    @BeforeEach
    void setUp() {
        aggregator = new APMLogAggregator();
    }

    @Test
    void testProcessLog_SingleMetric() {
        // Create APM log entries
        APMLogEntry entry = new APMLogEntry(timestamp, "host1", "cpu_usage_percent", 75.0, new HashMap<>());

        // Process the log
        aggregator.processLog(entry);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify results
        assertTrue(result.containsKey("cpu_usage_percent"));

        @SuppressWarnings("unchecked")
        Map<String, Object> cpuMetrics = (Map<String, Object>) result.get("cpu_usage_percent");

        assertEquals(75.0, cpuMetrics.get("minimum"));
        assertEquals(75.0, cpuMetrics.get("median"));
        assertEquals(75.0, cpuMetrics.get("average"));
        assertEquals(75.0, cpuMetrics.get("max"));
    }

    @Test
    void testProcessLog_MultipleMetrics() {
        // Create APM log entries for CPU
        APMLogEntry entry1 = new APMLogEntry(timestamp, "host1", "cpu_usage_percent", 60.0, new HashMap<>());
        APMLogEntry entry2 = new APMLogEntry(timestamp, "host1", "cpu_usage_percent", 70.0, new HashMap<>());
        APMLogEntry entry3 = new APMLogEntry(timestamp, "host1", "cpu_usage_percent", 80.0, new HashMap<>());

        // Create APM log entries for Memory
        APMLogEntry entry4 = new APMLogEntry(timestamp, "host1", "memory_usage_percent", 30.0, new HashMap<>());
        APMLogEntry entry5 = new APMLogEntry(timestamp, "host1", "memory_usage_percent", 40.0, new HashMap<>());

        // Process all logs
        aggregator.processLog(entry1);
        aggregator.processLog(entry2);
        aggregator.processLog(entry3);
        aggregator.processLog(entry4);
        aggregator.processLog(entry5);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify CPU results
        assertTrue(result.containsKey("cpu_usage_percent"));

        @SuppressWarnings("unchecked")
        Map<String, Object> cpuMetrics = (Map<String, Object>) result.get("cpu_usage_percent");

        assertEquals(60.0, cpuMetrics.get("minimum"));
        assertEquals(70.0, cpuMetrics.get("median"));
        assertEquals(70.0, cpuMetrics.get("average"));
        assertEquals(80.0, cpuMetrics.get("max"));

        // Verify Memory results
        assertTrue(result.containsKey("memory_usage_percent"));

        @SuppressWarnings("unchecked")
        Map<String, Object> memMetrics = (Map<String, Object>) result.get("memory_usage_percent");

        assertEquals(30.0, memMetrics.get("minimum"));
        assertEquals(35.0, memMetrics.get("median")); // (30 + 40) / 2
        assertEquals(35.0, memMetrics.get("average"));
        assertEquals(40.0, memMetrics.get("max"));
    }

    @Test
    void testGetAggregatedData_EmptyData() {
        // Get aggregated data without processing any logs
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify result is empty
        assertTrue(result.isEmpty());
    }
}